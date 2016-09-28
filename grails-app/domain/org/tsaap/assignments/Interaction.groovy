package org.tsaap.assignments

import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User

class Interaction {

    public static final String EMPTY_SPECIFICATION = "empty"
    String interactionType
    Integer rank
    String specification
    Boolean enabled = true

    Date dateCreated
    Date lastUpdated

    User owner
    Sequence sequence
    String state = StateType.beforeStart.name()

    static hasOne = [schedule:Schedule]

    static constraints = {
        interactionType inList: InteractionType.values()*.name()
        schedule nullable: true
        state inList: StateType.values()*.name()
    }

    static transients = ['interactionSpecification']


    InteractionSpecification getInteractionSpecification() {
        if (isResponseSubmission()) {
            return new ResponseSubmissionSpecification(specification)
        } else if (isEvaluation()) {
            return new EvaluationSpecification(specification)
        }
        null
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

    /**
     * Indicate if the interaction is read
     * @return true if the interaction is read
     */
    boolean isRead() {
        interactionType == InteractionType.Read.name()
    }

    /**
     * Indicate id interaction is disabled
     * @return true if interaction is disabled
     */
    boolean disabled() {
        !enabled
    }

    /**
     * Calculate the number of choice interaction responses for the current interaction
     * @return the number of responses
     */
    Integer choiceInteractionResponseCount() {
        ChoiceInteractionResponse.countByInteraction(this)
    }

    /**
     * Calculate the number of peer evaluations for the current interaction
     * @return the number of peer evaluations
     */
    Integer peerEvaluationCount() {
        // TODO
        0
    }

    /**
     * Check if a user has already given a response for the current interaction
     * @param user the user
     * @return true if user has already given a response
     */
    boolean hasResponseForUser(User user) {
        ChoiceInteractionResponse.countByInteractionAndLearner(this, user) > 0
    }
}

enum InteractionType {
    ResponseSubmission,
    Evaluation,
    Read
}