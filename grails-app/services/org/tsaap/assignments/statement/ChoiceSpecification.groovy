package org.tsaap.assignments.statement

import grails.validation.Validateable
import groovy.json.JsonOutput
import org.tsaap.assignments.JsonDefaultSpecification
import org.tsaap.assignments.QuestionType

/**
 * Created by qsaieb on 01/03/2017.
 */
@Validateable
class ChoiceSpecification extends JsonDefaultSpecification {

    private static final String CHOICE_INTERACTION_TYPE = "choiceInteractionType"
    private static final String ITEM_COUNT = "itemCount"
    public static final String EXPECTED_CHOICE_LIST = "expectedChoiceList"
    public static final String EXPLANATION_CHOICE_LIST = "explanationChoiceList"

    ChoiceSpecification () {
      super()
    }

    ChoiceSpecification (String jsonString) {
        super(jsonString)
        getExpectedChoiceListFromListOrMap()
        getExplanationChoiceListFromListOrMap()
    }

    static constraints = {
        choiceInteractionType nullable: true, inList: [ChoiceInteractionType.MULTIPLE.name(), ChoiceInteractionType.EXCLUSIVE.name(), QuestionType.OpenEnded.name()]
        itemCount nullable: true, max: 10
        expectedChoiceList nullable: true, validator: { val, obj ->
            if (obj.choiceInteractionType && obj.choiceInteractionType != QuestionType.OpenEnded.name()) {
                if (val?.size() < 1) {
                    return ['cannotBeEmpty']
                }
            }
        }
        explanationChoiceList nullable: true
    }

    @Override
    String getJsonString() {
        if (validateSpecification()) {
            def map = [:]
            map.putAll(specificationProperties)
            if (getExpectedChoiceList()) {
                ArrayList al = getExpectedChoiceList()*.specificationProperties
                map.put(EXPECTED_CHOICE_LIST, al)
            }
            if (getExplanationChoiceList()) {
                map.put(EXPLANATION_CHOICE_LIST, getExplanationChoiceList()*.specificationProperties)
            }
            return JsonOutput.toJson(map)
        }
        null
    }


    /**
     * Validate the current specification
     * @return
     */
    boolean validateSpecification() {
        validate()
    }

    /**
     * Get the choice interaction type (exclusive or multiple)
     * @return the choice interaction type
     */
    String getChoiceInteractionType() {
        getSpecificationProperty(CHOICE_INTERACTION_TYPE)
    }

    /**
     * Get the item count
     * @return the item count
     */
    Integer getItemCount() {
        getSpecificationProperty(ITEM_COUNT)
    }

    /**
     * Get the list of expected choices
     * @return
     */
    List<ChoiceItemSpecification> getExpectedChoiceList() {
        getSpecificationProperty(EXPECTED_CHOICE_LIST)
    }

    /**
     * Get the list of explanations choices
     * @return
     */
    List<ExplanationChoice> getExplanationChoiceList() {
        getSpecificationProperty(EXPLANATION_CHOICE_LIST)
    }

    /**
     * Set the list of explanations choices
     * @return
     */
   void setExplanationChoiceList(List<ExplanationChoice> explanationChoiceList) {
       setSpecificationProperty(EXPLANATION_CHOICE_LIST, explanationChoiceList)
    }



    /**
     * Set the choice interaction type
     * @param choiceInteractionType the choice interaction type (exclusive or multiple)
     */
    void setChoiceInteractionType(String choiceInteractionType) {
      setSpecificationProperty(CHOICE_INTERACTION_TYPE, choiceInteractionType)
    }

    /**
     * Set the item count
     * @param itemCount the item count
     */
    void setItemCount(Integer itemCount) {
        setSpecificationProperty(ITEM_COUNT, itemCount)
    }

    /**
     * Set the expected choice list
     * @param choiceList the choice list
     */
    void setExpectedChoiceList(List<ChoiceItemSpecification> choiceList) {
        setSpecificationProperty(EXPECTED_CHOICE_LIST, choiceList)
    }


    /**
     * Check if a choice with given index is in the expected choice list
     * @param index
     * @return
     */
    boolean expectedChoiceListContainsChoiceWithIndex(Integer index) {
        def res = false
        if (expectedChoiceList) {
            for (int i = 0; i < expectedChoiceList.size(); i++) {
                if (expectedChoiceList[i].index == index) {
                    res = true
                    break
                }
            }
        }
        res
    }


    /**
     * Return the choice with the given index in the expected choice
     * @param index the index
     * @return the interaction choice
     */
    ChoiceItemSpecification choiceWithIndexInExpectedChoiceList(Integer index) {
        def res = null
        if (expectedChoiceList) {
            for (int i = 0; i < expectedChoiceList.size(); i++) {
                if (expectedChoiceList[i].index == index) {
                    res = expectedChoiceList[i]
                    break
                }
            }
        }
        res
    }
    /**
     * Return the explanation with the given index in the expected choice.
     *
     * @param index the index
     * @return the explnation choice
     */
    ExplanationChoice explanationWithIndexInExplanationChoiceList(Integer index) {
        def res = null
        if (explanationChoiceList) {
            for (int i = 0; i < explanationChoiceList.size(); i++) {
                if (explanationChoiceList[i].index == index) {
                    res = explanationChoiceList[i]
                    break
                }
            }
        }
        res
    }

    private void getExpectedChoiceListFromListOrMap() {
        expectedChoiceList = expectedChoiceList.collect {
            if (it instanceof Map) {
                new ChoiceItemSpecification(specificationProperties: it)
            } else {
                new ChoiceItemSpecification(it, ((it as Float) * 100 / (itemCount as Float)) as Float)
            }
        }
    }

    private void getExplanationChoiceListFromListOrMap() {
        explanationChoiceList = explanationChoiceList.collect {
            if (it instanceof Map) {
                new ExplanationChoice(specificationProperties: it)
            } else {
                new ExplanationChoice(it.index, it.explanation, it.score)
            }
        }
    }



    /**
     * Calculate the total score obtained when adding score from each expected choice.
     * The total score is expected to be 100.
     *
     * @return the total score
     */
    Float getTotalScoreFromExpectedChoice() {
        Float totalScore = 0
        expectedChoiceList.each { totalScore += it.score }
        totalScore.round(4)
    }


}