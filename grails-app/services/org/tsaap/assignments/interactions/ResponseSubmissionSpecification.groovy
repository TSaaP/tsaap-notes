package org.tsaap.assignments.interactions

import grails.validation.Validateable
import groovy.json.JsonOutput

/**
 * A interaction specification for response submission
 */
@Validateable
class ResponseSubmissionSpecification extends JsonInteractionSpecification {

    /**
     * Default constructor
     */
    ResponseSubmissionSpecification() {
        setChoiceInteractionType(ChoiceInteractionType.EXCLUSIVE.name())
        setExpectedChoiceList([new InteractionChoice(1, 100f)])
    }

    /**
     * Construct a specification based on the json string description
     * @param jsonString the specification as json string
     */
    ResponseSubmissionSpecification(String jsonString) {
        super(jsonString)
        getExpectedChoiceListFromListOrMap()
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
     * Get the flag indicating that students must provide explanation
     * @return true if students must provide explanation
     */
    Boolean getStudentsProvideExplanation() {
        getSpecificationProperty(STUDENTS_PROVIDE_EXPLANATION)
    }

    /**
     * Get the flag indicating that students must provide confidence degree
     * @return true if students must provide confidence degree
     */
    Boolean getStudentsProvideConfidenceDegree() {
        getSpecificationProperty(STUDENTS_PROVIDE_CONFIDENCE_DEGREE)
    }

    /**
     * Get the list of expected choices
     * @return
     */
    List<InteractionChoice> getExpectedChoiceList() {
        getSpecificationProperty(EXPECTED_CHOICE_LIST)
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
     * Set the flag indicating if students must provide explanation
     * @param studentsProvideExplanation the boolean value
     */
    void setStudentsProvideExplanation(Boolean studentsProvideExplanation) {
        setSpecificationProperty(STUDENTS_PROVIDE_EXPLANATION, studentsProvideExplanation)
    }

    /**
     * Set the flag indicating if students must provide confidence degree
     * @param studentsProvideConfidenceDegree the boolean value
     */
    void setStudentsProvideConfidenceDegree(Boolean studentsProvideConfidenceDegree) {
        setSpecificationProperty(STUDENTS_PROVIDE_CONFIDENCE_DEGREE, studentsProvideConfidenceDegree)
    }

    /**
     * Set the expected choice list
     * @param choiceList the choice list
     */
    void setExpectedChoiceList(List<InteractionChoice> choiceList) {
        setSpecificationProperty(EXPECTED_CHOICE_LIST, choiceList)
    }

    /**
     * Check if student can submit multiple choice
     * @return
     */
    Boolean isMultipleChoice() {
        choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()
    }

    /**
     * Check if the specification has choices
     * @return
     */
    Boolean hasChoices() {
        choiceInteractionType
    }

    /**
     * Calculate the total score obtained when adding score from each expected choice. The total score is expected to be 100.
     * @return the total score
     */
    Float getTotalScoreFromExpectedChoice() {
        def totalScore = 0
        expectedChoiceList.each { totalScore += it.score }
        totalScore
    }

    @Override
    String getJsonString() {
        if (validateSpecification()) {
            def map = specificationProperties
            if (getExpectedChoiceList()) {
                map.put(EXPECTED_CHOICE_LIST, getExpectedChoiceList().specificationProperties)
            }
            return JsonOutput.toJson(map)
        }
        null
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
    InteractionChoice choiceWithIndexInExpectedChoiceList(Integer index) {
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

    static constraints = {
        choiceInteractionType nullable: true, inList: ChoiceInteractionType.values()*.name()
        itemCount nullable: true, max: 10
        studentsProvideExplanation nullable: false, validator: { val, obj ->
            if (!obj.choiceInteractionType && !val) {
                return ['studentsMustGiveExplanation']
            }

        }
        studentsProvideConfidenceDegree nullable: false
        expectedChoiceList nullable: true, validator: { val, obj ->
            if (obj.choiceInteractionType) {
                if (val?.size() < 1) {
                    return ['cannotBeEmpty']
                }
            }
        }
    }

    private void getExpectedChoiceListFromListOrMap() {
        expectedChoiceList = expectedChoiceList.collect {
            if (it instanceof Map) {
                new InteractionChoice(specificationProperties: it)
            } else {
                new InteractionChoice(it, ((it as Float) * 100 / (itemCount as Float)) as Float)
            }
        }
    }


    private static final String CHOICE_INTERACTION_TYPE = "choiceInteractionType"
    private static final String ITEM_COUNT = "itemCount"
    private static final String STUDENTS_PROVIDE_EXPLANATION = "studentsProvideExplanation"
    private static final String STUDENTS_PROVIDE_CONFIDENCE_DEGREE = "studentsProvideConfidenceDegree"
    public static final String EXPECTED_CHOICE_LIST = "expectedChoiceList"

}

enum ChoiceInteractionType {
    EXCLUSIVE,
    MULTIPLE
}

/**
 * Class representing an interaction item
 */
@Validateable
class InteractionChoice extends JsonInteractionSpecification {


    private static final String INDEX = "index"
    private static final String SCORE = "score"

    InteractionChoice() {}

    /**
     * Create an interaction choice object
     * @param index the index of the choice in the list of choice starting from 1
     * @param score the asscociated to this choice (max = 100)
     */
    InteractionChoice(Integer index, Float score) {
        this.index = index
        this.score = score
    }

    Integer getIndex() {
        return getSpecificationProperty(INDEX)
    }

    void setIndex(Integer index) {
        setSpecificationProperty(INDEX, index)
    }

    Float getScore() {
        return getSpecificationProperty(SCORE)
    }

    void setScore(Float score) {
        setSpecificationProperty(SCORE, score)
    }


    static constraints = {
        index nullable: false
        score nullable: false, max: 100 as Float
    }
}