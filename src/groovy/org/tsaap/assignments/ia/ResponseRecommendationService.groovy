package org.tsaap.assignments.ia

import org.tsaap.assignments.Interaction
import org.tsaap.assignments.InteractionResponse

/**
 * Interface representing the recommendation engine for responses
 */
interface ResponseRecommendationService {

    /**
     * Build the explanation recommendation mapping
     * @param responseList the responses containing explanations
     * @param max the max number of recommended response for each response
     * @return the mapping as a map
     */
    Map<String, List<Long>> getRecommendedResponseIdByResponseId(List<InteractionResponse> responseList)

    /**
     * Build the explanation recommendation mapping
     * @param responseList the responses containing explanations
     * @param max the max number of recommended response for each response
     * @return the mapping as a map
     */
    Map<String, List<Long>> getRecommendedResponseIdByResponseIdForOpenQuestion(List<InteractionResponse> responseList, int max)

    /**
     * Find all responses for a given interaction ordered by evaluation count
     * @param interaction the interaction
     * @param attempt indicate the attempt relative to the fetched responses
     * @param limit limit the sise of the returning list
     * @return the list of responses
     */
    List<InteractionResponse> findAllResponsesOrderedByEvaluationCount(Interaction interaction, int attempt, int limit)

}