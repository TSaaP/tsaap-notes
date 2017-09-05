package org.tsaap.assignments.ia

import org.tsaap.BootstrapTestService
import org.tsaap.assignments.*
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionService
import org.tsaap.directory.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class DefaultResponseRecommendationServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    InteractionService interactionService
    AssignmentService assignmentService
    DefaultResponseRecommendationService responseRecommendationService


    def setup() {
        bootstrapTestService.initializeTests()
    }


    void "test  response recommendation mapping"() {

        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "learners registered in the assignment"
        def learners = bootstrapTestService.learners
        User thom = learners[0]
        User mary = learners[1]
        User john = learners[2]
        User erik = learners[3]

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
        InteractionResponse respMary = new InteractionResponse(
                interaction: interaction,
                learner: mary   ,
                choiceListSpecification: "[1,4]",
                attempt: 1,
                explanation: "Mary [1,4] score 0- very confident",
                confidenceDegree: ConfidenceDegreeEnum.TOTALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respMary)
        InteractionResponse respJohn = new InteractionResponse(
                interaction: interaction,
                learner: john   ,
                choiceListSpecification: "[2,3,5]",
                attempt: 1,
                explanation: "John [2,3,5] score 100- very confident",
                confidenceDegree: ConfidenceDegreeEnum.TOTALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respJohn)
        InteractionResponse respErik = new InteractionResponse(
                interaction: interaction,
                learner: erik   ,
                choiceListSpecification: "[1,4]",
                attempt: 1,
                explanation: "Erik [1,4] score 0- not really confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respErik)

        expect: "score of learners is consistent"
        respThom.score == 100f
        println "> Thom 100 - ${respThom.id}"
        respMary.score == 0f
        println "> Mary 0 - ${respMary.id}"
        respJohn.score == 100f
        println "> John 100 - ${respJohn.id}"
        respErik.score == 0f
        println "> Erik 0 - ${respErik.id}"

        when: "building the explanation recommendation mapping with "
        def mapping = responseRecommendationService.getRecommendedResponseIdByResponseId(InteractionResponse.findAllByInteraction(interaction))

        then:"the algorithm provides a one to one recommendation as expected"
        println ">>>>>>>>>>>>> $mapping"
        mapping[respThom.id as String][0] == respErik.id
        mapping[respErik.id as String][0] == respThom.id
        mapping[respMary.id as String][0] == respJohn.id
        mapping[respJohn.id as String][0] == respMary.id
        mapping[respThom.id as String][1] == respJohn.id
        mapping[respMary.id as String][1] == respErik.id
        mapping[respJohn.id as String][1] == respThom.id
        mapping[respErik.id as String][1] == respMary.id
        mapping[respThom.id as String][2] == respMary.id
        mapping[respMary.id as String][2] == respThom.id
        mapping[respJohn.id as String][2] == respErik.id
        mapping[respErik.id as String][2] == respJohn.id

        when: "stopping the response interaction"
        interactionService.stopInteraction(interaction,interaction.owner)

        then:"esplanation mapping is saved"
        interaction.explanationRecommendationMapping

        and: "the corresponding map is buildable"
        interaction.explanationRecommendationMap()


        when:"asking for a recommendation in the evaluation interaction for a user"
        def evalInter = assignment.sequences[0].evaluationInteraction
        def recommendations = evalInter.sequence.findRecommendedResponsesForUser(thom)

        then: "the list of responses corresponding to the response user are found"
        recommendations[0] == respErik
        recommendations[1] == respJohn
        recommendations[2] == respMary

    }

    void "test  response recommendation mapping on open question"() {

        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment4WithOpenInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and:"the evaluation interaction spec"
        EvaluationSpecification evaluationSpecification = assignment.sequences[0].evaluationSpecification

        and: "learners registered in the assignment"
        def learners = bootstrapTestService.learners
        User thom = learners[0]
        User mary = learners[1]
        User john = learners[2]
        User erik = learners[3]

        for (int i = 0; i<4; i++) {
            assignmentService.registerUserOnAssignment(learners[i], assignment)
        }

        and: "each one with an answer"
        InteractionResponse respThom = new InteractionResponse(
                interaction: interaction,
                learner: thom,
                attempt: 1,
                explanation: "Thom explanation- not confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respThom)
        InteractionResponse respMary = new InteractionResponse(
                interaction: interaction,
                learner: mary   ,
                attempt: 1,
                explanation: "Mary explanation very confident",
                confidenceDegree: ConfidenceDegreeEnum.TOTALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respMary)
        InteractionResponse respJohn = new InteractionResponse(
                interaction: interaction,
                learner: john   ,
                attempt: 1,
                explanation: "John explanation very confident",
                confidenceDegree: ConfidenceDegreeEnum.TOTALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respJohn)
        InteractionResponse respErik = new InteractionResponse(
                interaction: interaction,
                learner: erik   ,
                attempt: 1,
                explanation: "Erik explanation not really confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respErik)

        expect:"the responseSubmission provides no choices"
        !interaction.sequence.statement.hasChoices()

        when: "building the explanation recommendation mapping with default value for recommendations number"
        def mapping = responseRecommendationService.getRecommendedResponseIdByResponseIdForOpenQuestion(
                InteractionResponse.findAllByInteraction(interaction),3)

        then:"the algorithm provides a one to one recommendation as expected"
        println ">>>>>>>>>>>>> open response mapping $mapping "
        mapping[respThom.id as String].size() == 3
        mapping[respErik.id as String].size() == 3
        mapping[respJohn.id as String].size() == 3
        mapping[respMary.id as String].size() == 3

        when: "building the explanation recommendation mapping with recommendation number set from evaluationSpecification "
        mapping = responseRecommendationService.getRecommendedResponseIdByResponseIdForOpenQuestion(
                InteractionResponse.findAllByInteraction(interaction),
                evaluationSpecification.responseToEvaluateCount)

        then:"the algorithm provides a one to one recommendation as expected"
        println ">>>>>>>>>>>>> open response mapping $mapping "
        mapping[respThom.id as String].size() == evaluationSpecification.responseToEvaluateCount
        mapping[respErik.id as String].size() == evaluationSpecification.responseToEvaluateCount
        mapping[respJohn.id as String].size() == evaluationSpecification.responseToEvaluateCount
        mapping[respMary.id as String].size() == evaluationSpecification.responseToEvaluateCount

        when: "stopping the response interaction"
        interactionService.stopInteraction(interaction,interaction.owner)

        then:"explanation mapping is saved"
        interaction.explanationRecommendationMapping

        and: "the corresponding map is buildable"
        interaction.explanationRecommendationMap()


        when:"asking for a recommendation in the evaluation interaction for a user"
        def evalInter = assignment.sequences[0].evaluationInteraction
        def recommendations = evalInter.sequence.findRecommendedResponsesForUser(thom)

        then: "the list of responses corresponding to the response user are found"
        println ">>>>>>>>>>>>> $recommendations of user thom ${thom.id} with response ${respThom.id}"
        recommendations[0]

    }

    void "test findAllResponseIdOrderedByEvaluationCount"() {
        given: "an assignment with sequence and interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions

        and:"the response submission interaction"
        Interaction interaction = assignment.sequences[0].responseSubmissionInteraction
        interactionService.startInteraction(interaction, interaction.owner)

        and: "learners registered in the assignment"
        def learners = bootstrapTestService.learners
        User thom = learners[0]
        User mary = learners[1]
        User john = learners[2]
        User erik = learners[3]

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
        InteractionResponse respMary = new InteractionResponse(
                interaction: interaction,
                learner: mary   ,
                choiceListSpecification: "[1,4]",
                attempt: 1,
                explanation: "Mary [1,4] score 0- very confident",
                confidenceDegree: ConfidenceDegreeEnum.TOTALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respMary)
        InteractionResponse respJohn = new InteractionResponse(
                interaction: interaction,
                learner: john   ,
                choiceListSpecification: "[2,3,5]",
                attempt: 1,
                explanation: "John [2,3,5] score 100- very confident",
                confidenceDegree: ConfidenceDegreeEnum.TOTALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respJohn)
        InteractionResponse respErik = new InteractionResponse(
                interaction: interaction,
                learner: erik   ,
                choiceListSpecification: "[1,4]",
                attempt: 1,
                explanation: "Erik [1,4] score 0- not really confident",
                confidenceDegree: ConfidenceDegreeEnum.NOT_REALLY_CONFIDENT.integerValue
        )
        interactionService.saveInteractionResponse(respErik)

        and: "some peer-grades have been distributed"
        PeerGrading pgFromThomOnMary = new PeerGrading(grader: thom, response: respMary, grade: 2).save()
        PeerGrading pgFromMaryOnJohn = new PeerGrading(grader: mary, response: respJohn, grade: 3).save()
        PeerGrading pgFromJohnOnMary = new PeerGrading(grader: john, response: respMary, grade: 4).save()
        PeerGrading pgFromErikOnThom = new PeerGrading(grader: erik, response: respThom, grade: 1).save()
        PeerGrading pgFromErikOnMary = new PeerGrading(grader: erik, response: respMary, grade: 2).save()
        PeerGrading pgFromThomOnJohn = new PeerGrading(grader: thom, response: respJohn, grade: 1).save()

        when:"searching responses id ordered by evaluation count with limit of 4"
        List<InteractionResponse> res = responseRecommendationService.findAllResponsesOrderedByEvaluationCount(interaction,1,4)

        then:"the list contains the four expected elements"
        res == [respErik, respThom, respJohn, respMary]

        when:"searching responses id ordered by evaluation count with limit of 2"
        res = responseRecommendationService.findAllResponsesOrderedByEvaluationCount(interaction,1,2)

        then:"the list contains the two expected elements"
        res == [respErik, respThom]

        when:"searching responses id ordered by evaluation count with limit of 6"
        res = responseRecommendationService.findAllResponsesOrderedByEvaluationCount(interaction,1,6)

        then:"the list contains the four expected elements"
        res == [respErik, respThom, respJohn, respMary]
    }
}
