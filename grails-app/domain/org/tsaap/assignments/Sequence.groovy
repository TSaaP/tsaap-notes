package org.tsaap.assignments

import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User

class Sequence {

    Integer rank

    Date dateCreated
    Date lastUpdated

    User owner
    Assignment assignment
    Statement statement

    static constraints = {

    }

    static transients = ['interactions', 'content', 'title',
                         'responseSubmissionSpecification', 'evaluationSpecification', 'responseSubmissionInteraction',
                         'evaluationInteraction', 'readInteraction']

    /**
     * Find all interactions
     * @return the interactions
     */
    List<Interaction> getInteractions() {
        Interaction.findAllBySequence(this, [sort: 'rank', order: 'asc'])
    }

    /**
     * Get the response submission interaction
     * @return the response submission interaction
     */
    Interaction getResponseSubmissionInteraction() {
        def result = null
        if (interactions?.size() > 0) {
            for (int i = 0; i < interactions.size(); i++) {
                if (interactions[i].isResponseSubmission()) {
                    result = interactions[i]
                    break
                }
            }
        }
        result
    }

    /**
     * Get the evaluation interaction
     * @return the evaluation interaction
     */
    Interaction getEvaluationInteraction() {
        def result = null
        if (interactions?.size() > 0) {
            for (int i = 0; i < interactions.size(); i++) {
                if (interactions[i].isEvaluation()) {
                    result = interactions[i]
                    break
                }
            }
        }
        result
    }

    /**
     * Get the read interaction
     * @return the read interaction
     */
    Interaction getReadInteraction() {
        def result = null
        if (interactions?.size() > 0) {
            for (int i = 0; i < interactions.size(); i++) {
                if (interactions[i].isRead()) {
                    result = interactions[i]
                    break
                }
            }
        }
        result
    }

    /**
     * Get the response submission specification
     * @return the response submission specification
     */
    ResponseSubmissionSpecification getResponseSubmissionSpecification() {
        def result = null
        if (interactions?.size() > 0) {
            for (int i = 0; i < interactions.size(); i++) {
                if (interactions[i].isResponseSubmission()) {
                    result = interactions[i].interactionSpecification
                    break
                }
            }
        }
        result
    }

    /**
     * Get the evaluation specification
     * @return the evaluation specification
     */
    EvaluationSpecification getEvaluationSpecification() {
        def result = null
        if (interactions?.size() > 0) {
            for (int i = 0; i < interactions.size(); i++) {
                if (interactions[i].isEvaluation()) {
                    result = interactions[i].interactionSpecification
                    break
                }
            }
        }
        result
    }

    /**
     * Get the title of the statement
     * @return the title
     */
    String getTitle() {
        statement?.title
    }

    /**
     * Get the content of the statement
     * @return the content
     */
    String getContent() {
        statement?.content
    }


}
