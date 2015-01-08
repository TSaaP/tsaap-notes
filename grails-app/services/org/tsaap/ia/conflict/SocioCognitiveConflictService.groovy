package org.tsaap.ia.conflict

import org.tsaap.notes.Note
import org.tsaap.questions.LiveSessionResponse


class SocioCognitiveConflictService {

    static transactional = false
    public static final int MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED = 10

    /**
     * Find the best response that is in conflict with a given response
     * @param responseList the list of response to select in the matching response
     * @param response the response
     * @return the best matching response
     */
    LiveSessionResponse findResponseInResponseListWithBestConflictWithResponse(List<LiveSessionResponse> responseList,
                                                                               LiveSessionResponse response) {
        // TODO : propose a real implementation
        // dummy implementation
        if (response == null || response.explanation == null) {
            return null
        }
        response
    }

    /**
     * Get the map matching user with explanation to evaluate with round robin algorithm
     * @param responseList the list of responses containing users and explanation to evaluate
     * @return the map
     */
    Map<Long, List<Long>> explanationIdListByUserId(List<LiveSessionResponse> responseList) {
        // get the list of suerId shuffleised
        List<Long> userIdList = responseList*.userId
        Collections.shuffle(userIdList)
        // get the notes to evaluate : only those with 100% credit
        List<Long> explanationList = []
        responseList.each { LiveSessionResponse response ->
            if (response.percentCredit == 100 && response.explanation?.content?.size()> MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED) {
                explanationList << response.id
            }
        }
        // matching algorythm
        explanationIdListByUserIdRoundRobinAlgorithm(userIdList,explanationList)
    }

    /**
     * Get the map matching user with explanation to evaluate with round robin algorithm
     * @param userIdList the list of user id (the keys)
     * @param explanationList the list of explanation to distribute among all users
     * @return the map
     */
    public Map<Long, List<Long>> explanationIdListByUserIdRoundRobinAlgorithm(List<Long> userIdList,List<Long> explanationList) {
        Map<Long, List<Long>> res = [:]
        def explanationCount = explanationList.size()
        if (explanationCount < 4) { // all users are mapped with the all list
            userIdList.each { Long userId ->
                res.put(userId, explanationList)
            }
        } else { // apply round robin algorithm
            int indexExplanation = 0
            userIdList.each { Long userId ->
                List<Long> explIdList = []
                for (int i = 0; i < 3; i++) {
                    explIdList << explanationList.get(indexExplanation % explanationCount)
                    indexExplanation++
                }
                res.put(userId, explIdList)
            }
        }
        res
    }
}
