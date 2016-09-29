package org.tsaap.assignments.interactions

import org.tsaap.assignments.ChoiceInteractionResponse
import org.tsaap.assignments.Interaction
import spock.lang.Specification

/**
 * Created by franck on 31/08/2016.
 */
class InteractionResultListServiceSpec extends Specification {

    InteractionResultListService resultMatrixService = new InteractionResultListService()

    void "build matrix result from response submission interaction"() {
        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getItemCount() >> 4
        }
        Interaction interaction = Mock() {
            isResponseSubmission() >> true
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "learner responses "
        ChoiceInteractionResponse response1 = new ChoiceInteractionResponse(choiceListSpecification: null,
                interaction: interaction )
        ChoiceInteractionResponse response2 = new ChoiceInteractionResponse(choiceListSpecification: "[1,2]",
                interaction: interaction )
        ChoiceInteractionResponse response3 = new ChoiceInteractionResponse(choiceListSpecification: "[1]",
                interaction: interaction )
        ChoiceInteractionResponse response4 = new ChoiceInteractionResponse(choiceListSpecification: "[1,2]",
                interaction: interaction )
        ChoiceInteractionResponse response5 = new ChoiceInteractionResponse(choiceListSpecification: "[2,3]",
                interaction: interaction )
        ChoiceInteractionResponse response6 = new ChoiceInteractionResponse(choiceListSpecification: "[3]",
                interaction: interaction )
        def responses = [response1,response2,response3,response4,response5,response6]

        when: "building the result matrix"
        List<Float> matrix = resultMatrixService.buildResultListForInteractionAndResponses(interaction, responses)

        then: "the obtained score is 100"
        matrix[0]  == (100/6).setScale(3, BigDecimal.ROUND_HALF_UP)
        matrix[1] == 300/6
        matrix[2] == 300/6
        matrix[3]  == (200/6).setScale(3, BigDecimal.ROUND_HALF_UP)
        matrix[4] == 0

    }

}
