package org.tsaap.assignments.interactions

import org.tsaap.assignments.Interaction
import org.tsaap.assignments.InteractionResponse
import org.tsaap.assignments.Sequence
import org.tsaap.assignments.Statement
import org.tsaap.assignments.statement.ChoiceSpecification
import spock.lang.Specification

/**
 * Created by franck on 31/08/2016.
 */
class InteractionResultListServiceSpec extends Specification {

    InteractionResultListService resultMatrixService = new InteractionResultListService()

    void "build matrix result from response submission interaction"() {
        given:"an interaction with its response submission spec"
        ChoiceSpecification choiceSpecification = Mock(ChoiceSpecification) {
            getItemCount() >> 4
        }
        Statement statement = Mock(Statement) {
            getChoiceSpecificationObject() >> choiceSpecification
        }
        Sequence sequence = Mock(Sequence) {
            getStatement() >> statement
        }
        Interaction interaction = Mock(Interaction) {
            isResponseSubmission() >> true
            getSequence() >> sequence
        }

        and: "learner responses "
        InteractionResponse response1 = new InteractionResponse(choiceListSpecification: null,
                interaction: interaction )
        InteractionResponse response2 = new InteractionResponse(choiceListSpecification: "[1,2]",
                interaction: interaction )
        InteractionResponse response3 = new InteractionResponse(choiceListSpecification: "[1]",
                interaction: interaction )
        InteractionResponse response4 = new InteractionResponse(choiceListSpecification: "[1,2]",
                interaction: interaction )
        InteractionResponse response5 = new InteractionResponse(choiceListSpecification: "[2,3]",
                interaction: interaction )
        InteractionResponse response6 = new InteractionResponse(choiceListSpecification: "[3]",
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
