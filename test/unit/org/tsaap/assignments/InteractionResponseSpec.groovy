package org.tsaap.assignments

import grails.test.mixin.TestFor
import org.tsaap.assignments.interactions.ChoiceInteractionType
import org.tsaap.assignments.interactions.InteractionChoice
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(InteractionResponse)
class InteractionResponseSpec extends Specification {

    void "test getting choic list from choice list specification"() {

        given:"a choice interaction response with a choice list specification in json"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[1,3]")

        when: "getting the choice list object"
        def choiceList = response.choiceList()

        then: "the choice list is a lis of integers"
        choiceList instanceof List<Integer>
        choiceList == [1,3]
    }

    void "test update choice list specification"() {

        given:"a choice interaction response with a choice list specification in json"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[1,3]")

        when: "updating the choice list"
        response.updateChoiceListSpecification([2,4])

        then: "the choice list specification is correctly updated"
        response.choiceListSpecification == "[2,4]"

    }

    void "test update score with all good answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with all good choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[1,3]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 100"
        response.score == 100

    }

    void "test update score with partial good answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with partial good choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[3]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 50"
        response.score == 50

    }

    void "test update score with no good answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with all good choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[4]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 0"
        response.score == 0

    }

    void "test update score with no answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with no choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: null,
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 0"
        response.score == 0

    }

    void "test update score with all answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with all choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[1,2,3,4]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 0"
        response.score == 0

    }

    void "test update score with one good and one bad answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with all choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[1,2]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 0"
        response.score == 0

    }

    void "test update score with two good and one bad answers"() {

        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(1,50), new InteractionChoice(3,50)]
            getItemCount() >> 4
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with all choices"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[1,2,3]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 50"
        response.score == 50

    }

    void "test update score with 5 items and one excusive choice as good response"() {
        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(3,100)]
            getItemCount() >> 5
            getChoiceInteractionType() >> ChoiceInteractionType.EXCLUSIVE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with choice 4"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[5]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 0"
        response.score == 0
    }

    void "test update score with 5 items and one multiple choice as good response"() {
        given:"an interaction with its response submission spec"
        ResponseSubmissionSpecification responseSubmissionSpec = Mock() {
            getExpectedChoiceList() >> [new InteractionChoice(3,100)]
            getItemCount() >> 5
            getChoiceInteractionType() >> ChoiceInteractionType.MULTIPLE.name()
        }
        Interaction interaction = Mock() {
            getInteractionSpecification() >> responseSubmissionSpec
        }

        and: "a learner response with choice 4"
        InteractionResponse response = new InteractionResponse(choiceListSpecification: "[5]",
                interaction: interaction )

        when: "updating score"
        response.updateScore()

        then: "the obtained score is 0"
        response.score == 100f/5
    }

}
