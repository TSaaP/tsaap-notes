package org.tsaap.assignments.interactions


import org.tsaap.BootstrapTestService
import org.tsaap.assignments.Assignment
import org.tsaap.assignments.AssignmentService
import org.tsaap.assignments.ChoiceInteractionResponse
import org.tsaap.assignments.ConfidenceDegreeEnum
import org.tsaap.assignments.Interaction
import org.tsaap.assignments.PeerGrading
import org.tsaap.assignments.Sequence
import org.tsaap.assignments.StateType
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.directory.User
import spock.lang.Specification

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class InteractionServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    InteractionService interactionService
    AssignmentService assignmentService

    def setup() {
        bootstrapTestService.initializeTests()
    }

    void "test start interaction"() {

        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.lastSequence
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        expect: "the state is consistent"
        sequence.state == StateType.beforeStart.name()
        interaction.state == StateType.beforeStart.name()

        when: "the interaction is started"
        interactionService.startInteraction(interaction,interaction.owner)

        then: "the interaction enter in the new state"
        interaction.state == StateType.show.name()
        sequence.state == StateType.show.name()

    }

    void "test stop interaction"() {

        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.lastSequence
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        expect: "the active interaction is the first one"
        sequence.activeInteraction == interaction

        when: "trying to stop not started interaction"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "an exception is thrown"
        thrown(ConditionViolationException)

        when:"the interaction is started"
        interactionService.startInteraction(interaction, interaction.owner)

        and: "the interaction is stopped"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "the interaction is in consistent state"
        interaction.state == StateType.afterStop.name()

        and: "the current active interaction is the second one"
        sequence.activeInteraction == sequence.evaluationInteraction

    }

    void "test start and stop interaction by another user than the owner"() {
        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        and: "a learner"
        User paul = bootstrapTestService.learnerPaul

        when: "trying a student try to start interaction"
        interactionService.startInteraction(interaction, paul)

        then:"an expection is thrown"
        thrown(ConditionViolationException)

        when:"the interaction is started"
        interactionService.startInteraction(interaction, interaction.owner)

        and: "the interaction is stopped by a student"
        interactionService.stopInteraction(interaction, paul)

        then:"an expection is thrown"
        thrown(ConditionViolationException)
    }

    void "test start and stop interaction with second interaction disabled"() {
        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.lastSequence
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        and:"the second interaction is disabled"
        Interaction interaction2 = assignment.lastSequence.evaluationInteraction
        interaction2.enabled = false
        interaction.save()

        and:"the tird interaction is enabled"
        Interaction interaction3 = assignment.lastSequence.readInteraction


        expect: "the active interaction is the first one"
        sequence.activeInteraction == interaction

        when:"the interaction is started"
        interactionService.startInteraction(interaction, interaction.owner)

        and: "the interaction is stopped"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "the interaction is in consistent state"
        interaction.state == StateType.afterStop.name()

        and: "the current active interaction is the last enabled because the second one is disabled"
        sequence.activeInteraction == interaction3

        when: "start and stop last interaction"
        interactionService.startInteraction(interaction3, interaction3.owner)
        interactionService.stopInteraction(interaction3, interaction3.owner)

        then: "the current active interaction stay the same"
        sequence.activeInteraction == interaction3

    }

    void "test save choice interaction response when the learner is not registered i the assignment"() {
        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "a learner not registered in the assignment"
        User paul = bootstrapTestService.learnerPaul

        when:"creating the paul response"
        ChoiceInteractionResponse resp = new ChoiceInteractionResponse(
                interaction: interaction,
                learner: paul,
                choiceListSpecification: "[1,3]",
                attempt: 1
        )

        and: "saving the response"
        interactionService.saveChoiceInteractionResponse(resp)

        then: "an exception is thrown"
        thrown(ConditionViolationException)

        and: "the interaction has no response for this learner"
        !interaction.hasResponseForUser(paul)

        when: "the interaction is stopped"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "the results are calculated and set"
        interaction.results == null
        interaction.resultsByAttempt() == [:]
    }


    void "test save choice interaction response"() {

        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "a learner registered in the assignment"
        User paul = bootstrapTestService.learnerPaul
        assignmentService.registerUserOnAssignment(paul, assignment)

        when:"creating the paul response"
        ChoiceInteractionResponse resp = new ChoiceInteractionResponse(
                interaction: interaction,
                learner: paul,
                choiceListSpecification: "[1,3]",
                attempt: 1
        )

        and: "saving the response"
        interactionService.saveChoiceInteractionResponse(resp)

        then: "the response is saved with no errors"
        resp.id
        !resp.hasErrors()

        and: "the score is updated"
        resp.score == 0f

        and: "the interaction has response for this learner"
        interaction.hasResponseForUser(paul)

        when: "the interaction is stopped"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "the results are calculated and set"
        interaction.results != null
        println ">>>>>>>>> ${interaction.results}"
        interaction.resultsByAttempt()["1"] == [0.000, 100.000, 0.000, 100.000, 0.000, 0.000]
        interaction.resultsByAttempt() == ["1":[0.000, 100.000, 0.000, 100.000, 0.000, 0.000]]
    }

    void "test update of mean grade of a response"() {
        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Sequence sequence = assignment.sequences[0]
        Interaction interaction = sequence.responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "learners registered in the assignment"
        def learners = bootstrapTestService.learners
        User thom = learners[0]
        User mary = learners[1]
        User john = learners[2]

        for (int i = 0; i<4; i++) {
            assignmentService.registerUserOnAssignment(learners[i], assignment)
        }

        and: "each one with an answer"
        ChoiceInteractionResponse respThom = new ChoiceInteractionResponse(
                interaction: interaction,
                learner: thom,
                choiceListSpecification: "[2,3,5]",
                attempt: 1,
                explanation: "Thom [2,3,5] score 100- not confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveChoiceInteractionResponse(respThom)

        when: "peer grading coming from mary and john"
        def pgMary = interactionService.peerGradingFromUserOnResponse(mary, respThom, 1)
        def pgJohn = interactionService.peerGradingFromUserOnResponse(john, respThom, 5)
        println ">>>>>>>> ${pgJohn.hasErrors()}"

        then: "peer grading objets are saved in a consistent way"
        pgJohn.id
        pgJohn.grade == 5f
        pgJohn.response == respThom
        pgMary.id

        when:"update the mean grade"
        def resp = interactionService.updateMeanGradeOfResponse(respThom)

        then: "the returned response is respThom"
        resp == respThom

        and: "the mean grade is updated with the correct mean"
        resp.meanGrade == 3f

        when: "deleting the assignment"
        assignmentService.deleteAssignment(assignment,assignment.owner)

        then: "all attached objects are deleted"
        !PeerGrading.findById(pgJohn.id)
        !PeerGrading.findById(pgMary.id)
        !ChoiceInteractionResponse.findById(respThom.id)
        !Interaction.findById(interaction.id)
        !Sequence.findById(sequence.id)
        !Assignment.findById(assignment.id)
    }
}
