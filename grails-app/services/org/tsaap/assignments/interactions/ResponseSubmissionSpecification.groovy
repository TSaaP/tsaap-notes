package org.tsaap.assignments.interactions

import grails.validation.Validateable
import groovy.json.JsonOutput
import org.tsaap.assignments.JsonDefaultSpecification

/**
 * A interaction specification for response submission
 */
@Validateable
class ResponseSubmissionSpecification extends JsonDefaultSpecification {

    /**
     * Construct a specification based on the json string description
     * @param jsonString the specification as json string
     */
    ResponseSubmissionSpecification(String jsonString) {
        super(jsonString)
    }
    ResponseSubmissionSpecification () {}

    static constraints = {
        studentsProvideExplanation nullable: false
        studentsProvideConfidenceDegree nullable: false
    }


    /**
     * Validate the current specification
     * @return
     */
    boolean validateSpecification() {
        validate()
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


    @Override
    String getJsonString() {
        if (validateSpecification()) {
            return JsonOutput.toJson(specificationProperties)
        }
        null
    }

    private static final String STUDENTS_PROVIDE_EXPLANATION = "studentsProvideExplanation"
    private static final String STUDENTS_PROVIDE_CONFIDENCE_DEGREE = "studentsProvideConfidenceDegree"
}

/**
 * Class representing an interaction item
 */
