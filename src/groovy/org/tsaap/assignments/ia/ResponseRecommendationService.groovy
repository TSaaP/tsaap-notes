package org.tsaap.assignments.ia

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
    Map<String, List<Long>> getRecommendedResponseIdByResponseIdForOpenQuestion(List<InteractionResponse> responseList)

}