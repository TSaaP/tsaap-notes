package org.tsaap.assignments

import org.hibernate.FetchMode
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

    Interaction activeInteraction
    String state = StateType.beforeStart.name()

    static constraints = {
        activeInteraction nullable: true
        state inList: StateType.values()*.name()
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
     * get the interaction of the given type
     * @param interactionType the interaction type
     * @return the interaction
     */
    Interaction getInteractionOfType(InteractionType interactionType) {
        this."get${interactionType.name()}Interaction"()
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

    /**
     * Find all recommended responses for user
     * @param user the user
     * @return the response list
     */
    List<ChoiceInteractionResponse> findRecommendedResponsesForUser(User user) {
        def responseInteraction = responseSubmissionInteraction
        def userResponse = ChoiceInteractionResponse.findByInteractionAndLearnerAndAttempt(responseInteraction,user,1)
        def res
        if (userResponse) {
            res = responseInteraction.explanationRecommendationMap()[userResponse.id as String].collect {
                ChoiceInteractionResponse.get(it)
            }
        } else {
            res = []
        }
        res
    }

    /**
     * Find all good responses with explanations
     * @return the good responses
     */
    List<ChoiceInteractionResponse> findAllGoodResponses() {
        Interaction interaction = responseSubmissionInteraction
        ChoiceInteractionResponse.findAllByInteractionAndAttemptAndScore(interaction, 1, 100f,
                [sort: "meanGrade", order: "desc"])
    }

    /**
     * Return all bad responses with explanations for the sequence
     * Responses in returned structure can be accessed this way:
     * map[score][answerGroup][index] where
     * score is a Float eg: 100.0, 0.0, -50.0
     * answerGroup is a String eg: "1", "1,3", ""
     * index is an index on the response list for answerGroup eg: 0, 4
     * @param sessionPhase
     * @return
     */
    Map<Float, Map<String, List<ChoiceInteractionResponse>>> findAllBadResponses() {
        def list
        Map map = [:]
        Interaction interaction = responseSubmissionInteraction
        list = ChoiceInteractionResponse.withCriteria {
            eq('interaction', interaction)
            eq('attempt',1)
            lt('score', 100.0f)
            order('score', 'desc')
            'learner' {
                order('normalizedUsername', 'asc')
            }
            fetchMode('user', FetchMode.JOIN)
        }
        list.each {
            def answers = it.choiceListSpecification
            if (!map[it.score]) {
                def answerMap = [:]
                answerMap.put(answers, [it])
                map[it.score] = answerMap
            } else {
                if (!map[it.score][answers]) {
                    map[it.score][answers] = [it]
                } else {
                    map[it.score][answers].push(it)
                }
            }
        }
        map
    }

    /**
     *
     * @return true if the sequence has explanations
     */
    boolean hasExplanations() {
        responseSubmissionSpecification?.studentsProvideExplanation
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

enum StateType {
    beforeStart,
    show,
    afterStop
}