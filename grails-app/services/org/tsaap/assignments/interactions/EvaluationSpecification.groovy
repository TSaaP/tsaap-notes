package org.tsaap.assignments.interactions

import grails.validation.Validateable

/**
 * Specification of an evaluation interaction.
 */
@Validateable
class EvaluationSpecification extends JsonInteractionSpecification {

    /**
     * Default constructor
     */
    EvaluationSpecification() {}

    /**
     * Construct an evaluation specification based on a json string
     * @param jsonStr
     */
    EvaluationSpecification(String jsonStr) {
        super(jsonStr)
    }

    /**
     * Get the count of responses to evaluate by each student
     * @return the count
     */
    Integer getResponseToEvaluateCount() {
        getSpecificationProperty(RESPONSE_COUNT)
    }

    /**
     * Set the count of responses to evaluate by each student
     * @param responseCount the response count
     */
    void setResponseToEvaluateCount(Integer responseCount) {
        setSpecificationProperty(RESPONSE_COUNT,responseCount)
    }

    /**
     * Validate the current specification
     * @return true if the specification is valid, false otherwise
     */
    boolean validateSpecification() {
        validate()
    }

    static constraints = {
        responseToEvaluateCount nullable: false, max: 10
    }

    private static final String RESPONSE_COUNT = "responseToEvaluateCount"

}
