package org.tsaap.assignments

import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
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

    static transients = ['schedule','interactionSpecification']

    /**
     * Get the schedule
     * @return the schedule
     */
    Schedule getSchedule() {
        Schedule.findByInteraction(this)
    }

    InteractionSpecification getInteractionSpecification() {
        if (interactionType == InteractionType.ResponseSubmission.name()) {
            return new ResponseSubmissionSpecification(specification)
        } else {
            return new EvaluationSpecification(specification)
        }
    }

    /**
     * Indicate if the interaction is a response submission
     * @return true if the interaction is a response submission
     */
    boolean isResponseSubmission() {
        interactionType == InteractionType.ResponseSubmission.name()
    }

    /**
     * Indicate if the interaction is an evaluation
     * @return true if the interaction is an evaluation
     */
    boolean isEvaluation() {
        interactionType == InteractionType.Evaluation.name()
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