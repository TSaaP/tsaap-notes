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
    Boolean phasesAreScheduled = false

    static constraints = {

    }

    static transients = ['interactions', 'content', 'title',
                         'responseSubmissionSpecification', 'evaluationSpecification', 'responseSubmissionInteraction',
                         'evaluationInteraction', 'readInteraction', 'defaultStartDate']

    /**
     * Find all interactions
     * @return the interactions
     */
    List<Interaction> getInteractions() {
        if (id == null) {
            return null
        }
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
     * Get the start date of phase 1
     * @return the start date of phase 1 if any
     */
    Date getStartDatePhase1() {
        if (interactions?.size() > 0) {
            return interactions?.get(0)?.schedule?.startDate
        }
        getDefaultStartDate()
    }

    /**
     * Get the start date of phase 2
     * @return the start date of phase 2 if any
     */
    Date getStartDatePhase2() {
        if (interactions?.size() > 1) {
            interactions?.get(1)?.schedule?.startDate
        }
        getDefaultStartDate() + 1
    }

    /**
     * Get the start date of phase 3
     * @return the start date of phase 3 if any
     */
    Date getStartDatePhase3() {
        if (interactions?.size() > 2) {
            interactions?.get(2)?.schedule?.startDate
        }
        getDefaultStartDate() + 1
    }

    /**
     * Get the end date of phase 1
     * @return the end date of phase 1 if any
     */
    Date getEndDatePhase1() {
        if (interactions?.size() > 0) {
            return interactions?.get(0)?.schedule?.endDate
        }
        null
    }

    /**
     * Get the end date of phase 2
     * @return the end date of phase 2 if any
     */
    Date getEndDatePhase2() {
        if (interactions?.size() > 1) {
            interactions?.get(1)?.schedule?.endDate
        }
        null
    }

    /**
     * Get the end date of phase 3
     * @return the end date of phase 3 if any
     */
    Date getEndDatePhase3() {
        if (interactions?.size() > 2) {
            interactions?.get(2)?.schedule?.endDate
        }
        null
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

    private Date getDefaultStartDate() {
        def res
        if (assignment?.schedule?.startDate) {
            res = assignment?.schedule?.startDate
        } else {
            res = new Date()
        }
        res
    }


}
