package org.tsaap.assignments.ia

import org.tsaap.assignments.ChoiceInteractionResponse
import org.tsaap.directory.User

/**
 * Interface representing the recommendation engine for responses
 */
interface ResponseRecommendationService {

    /**
     * Find recommanded responses for a given user
     * @param user the user
     * @param responses the list of all responses where to choose the recommended ones in
     * @param max the max number of return responses
     * @return the list of recommanded responses
     */
    List<ChoiceInteractionResponse> findRecommendedResponsesForUser(User user, ChoiceInteractionResponse userResponse, List<ChoiceInteractionResponse> responses, Integer max)

}