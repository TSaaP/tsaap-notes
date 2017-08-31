package org.tsaap.assignments

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.tsaap.assignments.ia.ResponseRecommendationService
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionResultListService
import org.tsaap.assignments.interactions.InteractionService
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User

class Interaction {

    public static final String EMPTY_SPECIFICATION = "empty"
    String interactionType
    Integer rank
    String specification

    Date dateCreated
    Date lastUpdated

    User owner
    Sequence sequence
    String state = StateType.beforeStart.name()

    String results
    String explanationRecommendationMapping


    static constraints = {
        interactionType inList: InteractionType.values()*.name()
        state inList: StateType.values()*.name()
        results nullable: true
        explanationRecommendationMapping nullable: true
    }

    static transients = ['interactionSpecification', 'interactionResultListService', 'responseRecommendationService', 'interactionService']

    /**
     * Get the result matrix as a list of float
     * @return the result matrix as a list of float for each attempt
     */
    Map<String, List<Float>> resultsByAttempt() {
        if (!results) {
            return [:]
        }
        JsonSlurper jsonSlurper = new JsonSlurper()
        jsonSlurper.parseText(results)
    }

    /**
     * Get the results for the last attempt
     * @return the results
     */
    List<Float> resultsOfLastAttempt() {
        def resultsByAttempt = resultsByAttempt()
        List<Float> res
        if (resultsByAttempt["2"]) {
            res = resultsByAttempt["2"]
        } else if (resultsByAttempt["1"]) {
            res = resultsByAttempt["1"]
        } else {
            res = []
        }
        res
    }

    InteractionService interactionService

    /**
     * Process to perform after stop
     */
    void doAfterStop() {
        state = StateType.afterStop.name()
        if (isResponseSubmission()) {
            if (sequence.statement.hasChoices()) {
                updateResults()
            }
            if (interactionSpecification.studentsProvideExplanation) {
                updateExplanationRecommendationMapping()
            }
            save()
        }
        if (isEvaluation()) {
            def respSubmInter = sequence.responseSubmissionInteraction
            if (sequence.statement.hasChoices()) {
                respSubmInter.updateResults(2)
                respSubmInter.save()
            }
            int attemptEvaluated = sequence.executionIsFaceToFace() ? 1 : 2
            respSubmInter.findAllEvaluatedResponses(attemptEvaluated).each {
                interactionService.updateMeanGradeOfResponse(it)
            }
        }
    }

    /**
     * Find all evaluated responses for the current interaction
     * @return the list of evaluated responses
     */
    List<InteractionResponse> findAllEvaluatedResponses(int attempt = 1) {
        InteractionResponse.findAllByInteractionAndAttempt(this, attempt)
    }


    /**
     * Get the interaction specification object
     * @return the interaction specification
     */
    JsonSpecification getInteractionSpecification() {
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
     * Calculate the number of choice interaction responses for the current interaction
     * @return the number of responses
     */
    Integer interactionResponseCount(int attempt = 1) {
        Integer res = InteractionResponse.countByInteractionAndAttempt(this, attempt)
        res
    }

    /**
     * Count the number of evaluations
     * @return the count
     */
    Integer evaluationCount() {
        def count = PeerGrading.executeQuery(
                '''
                select count(distinct pg.grader) from PeerGrading pg
                where pg.response in (from InteractionResponse resp where resp.interaction = ?)
                ''',
                [this])
        count[0]
    }

    /**
     * Check if a user has already given a response for the current interaction
     * @param user the user
     * @return true if user has already given a response
     */
    boolean hasResponseForUser(User user, int attempt = 1) {
        InteractionResponse.countByInteractionAndLearnerAndAttempt(this, user, attempt) > 0
    }


    /**
     * Get the response for the given user
     * @param user the user
     * @return the response
     */
    InteractionResponse responseForUser(User user, int attempt = 1) {
        InteractionResponse.findByInteractionAndLearnerAndAttempt(this, user, attempt)
    }

    /**
     * Get the last response for the given user
     * @param user the user
     * @return the last response
     */
    InteractionResponse lastAttemptResponseForUser(User user) {
        def res = InteractionResponse.findByInteractionAndLearnerAndAttempt(this, user, 2)
        if (!res) {
            res = InteractionResponse.findByInteractionAndLearnerAndAttempt(this, user, 1)
        }
        res
    }

    /**
     *
     * @return the explanation recommendation map
     */
    Map<String, List<Long>> explanationRecommendationMap() {
        if (!explanationRecommendationMapping) {
            return [:]
        }
        JsonSlurper jsonSlurper = new JsonSlurper()
        def res = jsonSlurper.parseText(explanationRecommendationMapping)
        res
    }

    /**
     * return the state of the current interaction for the given learner
     * @param user the learner
     * @return the state of the current interaction for the given learner
     */
    String stateForLearner(User user) {
        String state = this.state
        if (sequence.executionIsBlendedOrDistance() && !sequence.isStopped()) {
            if (this == sequence.activeInteractionForLearner(user)) {
                if (sequence.executionIsBlended() && this.isRead()) {
                    state = this.state
                } else {
                    state = StateType.show.name()
                }
            } else  {
                state = StateType.afterStop.name()
            }
        }
        state
    }

    /**
     * return the state of the current interaction for the given teacher
     * @param user the teacher
     * @return the state of the current interaction for the given teacher
     */
    String stateForTeacher(User user) {
        String state = this.state
        if (sequence.executionIsBlendedOrDistance()) {
            state = StateType.afterStop.name()
        }
        if (sequence.executionIsBlended() && isRead()) {
            state = this.state
        }
        state
    }

    ResponseRecommendationService responseRecommendationService


    private void updateExplanationRecommendationMapping(Integer attempt = 1) {
        def responses = InteractionResponse.findAllByInteractionAndAttempt(this, attempt)
        if (responses) {
            def mapping
            if (sequence.statement.hasChoices()) {
                mapping = responseRecommendationService.getRecommendedResponseIdByResponseId(responses)
            } else {
                int recommendationCount = sequence.evaluationSpecification.responseToEvaluateCount
                mapping = responseRecommendationService.getRecommendedResponseIdByResponseIdForOpenQuestion(responses,recommendationCount)
            }
            explanationRecommendationMapping = JsonOutput.toJson(mapping)
        }
    }

    InteractionResultListService interactionResultListService

    private void updateResults(Integer attempt = 1) {
        def responses = InteractionResponse.findAllByInteractionAndAttempt(this, attempt)
        if (responses) {
            def resList = interactionResultListService.buildResultListForInteractionAndResponses(this, responses)
            def resMap = resultsByAttempt() + [(attempt.toString()): resList]
            results = JsonOutput.toJson(resMap)
        }
    }


}

enum InteractionType {
    ResponseSubmission,
    Evaluation,
    Read
}