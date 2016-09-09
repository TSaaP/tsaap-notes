package org.tsaap.assignments.interactions

import grails.validation.Validateable

/**
 * A interaction specification for response submission
 */
@Validateable
class ResponseSubmissionSpecification extends JsonInteractionSpecification {

    /**
     * Default constructor
     */

    public static final String EXPECTED_CHOICE_LIST = "expectedChoiceList"

    ResponseSubmissionSpecification() {}

    /**
     * Construct a specification based on the json string description
     * @param jsonString the specification as json string
     */
    ResponseSubmissionSpecification(String jsonString) {
        super(jsonString)
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
    List<Integer> getExpectedChoiceList() {
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
        setSpecificationProperty(STUDENTS_PROVIDE_EXPLANATION,studentsProvideExplanation)
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
    void setExpectedChoiceList(List<Integer> choiceList) {
        setSpecificationProperty(EXPECTED_CHOICE_LIST, choiceList)
    }

    /**
     * Check if student can submit multiple choice
     * @return
     */
    Boolean isMultipleChoice() {
        choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()
    }

    static constraints = {
        choiceInteractionType nullable: true, inList: ChoiceInteractionType.values()*.name()
        itemCount nullable: true, max: 10
        studentsProvideExplanation nullable: false
        studentsProvideConfidenceDegree nullable: false
        expectedChoiceList nullable: false, minSize: 1
    }


    private static final String CHOICE_INTERACTION_TYPE = "choiceInteractionType"
    private static final String ITEM_COUNT = "itemCount"
    private static final String STUDENTS_PROVIDE_EXPLANATION = "studentsProvideExplanation"
    private static final String STUDENTS_PROVIDE_CONFIDENCE_DEGREE = "studentsProvideConfidenceDegree"

}

enum ChoiceInteractionType {
    EXCLUSIVE,
    MULTIPLE
}