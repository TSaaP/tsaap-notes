package org.tsaap.assignments

import org.tsaap.directory.User

class Interaction {

    String interactionType
    Integer rank
    String specification

    Date dateCreated
    Date lastUpdated

    User owner
    Sequence sequence

    static constraints = {
        interactionType inList: InteractionType.values()*.name()
    }

    static transients = ['schedule']

    /**
     * Get the schedule
     * @return the schedule
     */
    Schedule getSchedule() {
        Schedule.findByInteraction(this)
    }
}

enum InteractionType {

    ResponseSubmission("org.tsaap.interactions.ResponseSubmissionSpecification"),
    Evaluation("org.tsaap.interactions.EvaluationSpecification")

    private String specificationQualifiedType

    String getSpecificationQualifiedType() {
        specificationQualifiedType
    }

    InteractionType(String specificationQualifiedType) {
        this.specificationQualifiedType = specificationQualifiedType
    }

}