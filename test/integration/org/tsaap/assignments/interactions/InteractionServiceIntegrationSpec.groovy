package org.tsaap.assignments.interactions

import org.tsaap.BootstrapService
import org.tsaap.BootstrapTestService
import org.tsaap.assignments.*
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.directory.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class InteractionServiceIntegrationSpec extends Specification {

    BootstrapService bootstrapService
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

        and: "the current active interaction remain the same"
        sequence.activeInteraction == interaction

    }

    void "test update active interaction"() {
        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.lastSequence
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        expect: "the active interaction is the first one and is started"
        sequence.activeInteraction == interaction

        when:"the interaction is not started"
        interaction.state == StateType.beforeStart.name()

        and:"the active interaction is updated"
        interactionService.setNextActiveInteraction(interaction, interaction.owner)

        then: "an exception is thrown"
        thrown(ConditionViolationException)

        when:"the interaction is started but not closed"
        interactionService.startInteraction(interaction, interaction.owner)

        and:"the active interaction is updated"
        interactionService.setNextActiveInteraction(interaction, interaction.owner)

        then: "an exception is thrown"
        thrown(ConditionViolationException)

        when:"the interaction is closed"
        interactionService.stopInteraction(interaction, interaction.owner)

        and:"the active interaction is updated"
        interactionService.setNextActiveInteraction(interaction, interaction.owner)

        then: "the active interaction is now the second one"
        sequence.activeInteraction == assignment.lastSequence.evaluationInteraction

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


    void "test save choice interaction response when the learner is not registered i the assignment"() {
        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "a learner not registered in the assignment"
        User paul = bootstrapTestService.learnerPaul

        when:"creating the paul response"
        InteractionResponse resp = new InteractionResponse(
                interaction: interaction,
                learner: paul,
                choiceListSpecification: "[1,3]",
                attempt: 1
        )

        and: "saving the response"
        interactionService.saveInteractionResponse(resp)

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
        InteractionResponse resp = new InteractionResponse(
                interaction: interaction,
                learner: paul,
                choiceListSpecification: "[1,3]",
                attempt: 1
        )

        and: "saving the response"
        interactionService.saveInteractionResponse(resp)

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
        User paul = learners[3]

        for (int i = 0; i<4; i++) {
            assignmentService.registerUserOnAssignment(learners[i], assignment)
        }

        and: "each one with an answer"
        InteractionResponse respThom = new InteractionResponse(
                interaction: interaction,
                learner: thom,
                choiceListSpecification: "[2,3,5]",
                attempt: 1,
                explanation: "Thom [2,3,5] score 100- not confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respThom)

        when: "peer grading coming from mary and john"
        def pgMary = interactionService.peerGradingFromUserOnResponse(mary, respThom, 1)
        def pgJohn = interactionService.peerGradingFromUserOnResponse(john, respThom, 5)
        def pgPaul = interactionService.peerGradingFromUserOnResponse(paul, respThom, -1)
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
        !InteractionResponse.findById(respThom.id)
        !Interaction.findById(interaction.id)
        !Sequence.findById(sequence.id)
        !Assignment.findById(assignment.id)
    }

    void "test save open interaction response when the learner is not registered i the assignment"() {
        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "a learner not registered in the assignment"
        User paul = bootstrapTestService.learnerPaul

        when:"creating the paul response"
        InteractionResponse resp = new InteractionResponse(
                interaction: interaction,
                learner: paul,
                attempt: 1
        )

        and: "saving the response"
        interactionService.saveInteractionResponse(resp)

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


    void "test save open interaction response"() {

        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment4WithOpenInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "a learner registered in the assignment"
        User paul = bootstrapTestService.learnerPaul
        assignmentService.registerUserOnAssignment(paul, assignment)

        when:"creating the paul response"
        InteractionResponse resp = new InteractionResponse(
                interaction: interaction,
                learner: paul,
                explanation: "an explanation",
                attempt: 1
        )

        and: "saving the response"
        interactionService.saveInteractionResponse(resp)

        then: "the response is saved with no errors"
        resp.id
        !resp.hasErrors()

        and: "the interaction has response for this learner"
        interaction.hasResponseForUser(paul)

        when: "the interaction is stopped"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "the results are not calculated and set"
        interaction.results == null
        println ">>>>>>>>> ${interaction.results}"
    }

    void "test update of mean grade of an open response"() {
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
        User paul = learners[3]

        for (int i = 0; i<4; i++) {
            assignmentService.registerUserOnAssignment(learners[i], assignment)
        }

        and: "each one with an answer"
        InteractionResponse respThom = new InteractionResponse(
                interaction: interaction,
                learner: thom,
                attempt: 1,
                explanation: "Thom explanation not confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respThom)

        when: "peer grading coming from mary and john"
        def pgMary = interactionService.peerGradingFromUserOnResponse(mary, respThom, 1)
        def pgJohn = interactionService.peerGradingFromUserOnResponse(john, respThom, 5)
        def pgPaul = interactionService.peerGradingFromUserOnResponse(paul, respThom, -1)
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
        !InteractionResponse.findById(respThom.id)
        !Interaction.findById(interaction.id)
        !Sequence.findById(sequence.id)
        !Assignment.findById(assignment.id)
    }

    void "test build Response Based On Teacher Expected Explanation For A Sequence with multiple choice and face to face"() {

        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]

        expect: "explanation is not provided"
        !sequence.statement.expectedExplanation

        and: "sequence is choice based"
        sequence.statement.hasChoices()

        when: "trying to generate expected response"
        def resp = interactionService.buildResponseBasedOnTeacherExpectedExplanationForASequence(
                sequence,
                assignment.owner
        )

        then: "no response is provided"
        !resp

        when: "an expected explanation is provided"
        sequence.statement.expectedExplanation = "the expected explanation"

        and: "sequence is face to face"
        sequence.executionContext = ExecutionContextType.FaceToFace.name()

        and: "trying to generate expected response"
        resp = interactionService.buildResponseBasedOnTeacherExpectedExplanationForASequence(
                sequence,
                assignment.owner
        )

        then: "the response is generated as expected"
        resp.interaction == sequence.responseSubmissionInteraction
        resp.attempt == 1
        resp.confidenceDegree == ConfidenceDegreeEnum.CONFIDENT.ordinal()
        resp.learner == assignment.owner
        resp.score == 100f
    }

    void "test build Response Based On Teacher Expected Explanation For A Sequence with open-ended question and distance"() {

        given: "an assignment with sequence and expected explanation"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.statement.expectedExplanation = "the expected explanation"

        and: "sequence is distance"
        sequence.executionContext = ExecutionContextType.Distance.name()

        and: "sequence is open-ended based"
        sequence.statement.questionType = QuestionType.OpenEnded

        when: "trying to generate expected response"
        def resp = interactionService.buildResponseBasedOnTeacherExpectedExplanationForASequence(
                sequence,
                assignment.owner,
                ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT
        )

        then: "the response is generated as expected"
        resp.interaction == sequence.responseSubmissionInteraction
        resp.attempt == 2
        resp.confidenceDegree == ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.ordinal()
        resp.learner == assignment.owner
        resp.score == null
    }

    void testBuildResponsesBasedOnTeacherFakeExplanationsForASequence() {

        given: "an assignment with sequence and fake explanations"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        new FakeExplanation(content: "fake explanation 1", correspondingItem: 2, statement: sequence.statement, author: sequence.owner).save(failOnError: true)
        new FakeExplanation(content: "fake explanation 2", correspondingItem: 1, statement: sequence.statement, author: sequence.owner).save(failOnError: true)

        and: "sequence is distance"
        sequence.executionContext = ExecutionContextType.Distance.name()

        when: "trying to generate fake responses"
        def resps = interactionService.buildResponsesBasedOnTeacherFakeExplanationsForASequence(
                sequence,
                ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT
        )

        then: "the responses are generated as expected"
        resps[0].id
        !resps[0].hasErrors()
        resps[0].interaction == sequence.responseSubmissionInteraction
        resps[0].attempt == 2
        resps[0].confidenceDegree == ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.ordinal()
        resps[0].learner.id == bootstrapService.fakeUserList[0].id
        resps[0].score > 0f
        resps[1].id
        !resps[1].hasErrors()
        resps[1].interaction == sequence.responseSubmissionInteraction
        resps[1].attempt == 2
        resps[1].confidenceDegree == ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.ordinal()
        resps[1].learner.id == bootstrapService.fakeUserList[1].id
        resps[1].score == 0f


    }


}
