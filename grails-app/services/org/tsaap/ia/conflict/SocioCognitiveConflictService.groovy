package org.tsaap.ia.conflict

import org.tsaap.notes.Note
import org.tsaap.questions.LiveSessionResponse


class SocioCognitiveConflictService {

    static transactional = false
    public static final int MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED = 10

    /**
     * Get the map matching between two conflict response
     * @param responseList the list of responses containing users and explanation to evaluate
     * @return the map
     */
    Map<Long, Long> responseConflictIdByResponseId(List<LiveSessionResponse> responseList) {
        // sort response by level of confidence degree
        responseList = responseList.sort(false) { -it.confidenceDegree }
        // get the list of response Id
        List<Long> responseIdList = responseList*.id
        // the good responses with highest level of confidence
        List<Long> goodResponseIdList = []
        // the bad responses with highest level of confidence
        List<Long> badResponseIdList = []
        // the map for the good responses
        Map<Long, Long> goodResponseMap = [:]
        // the map for the bad responses
        Map<Long, Long> badResponseMap = [:]
        // the way to set this two lists and two create two maps of responses (the good and the one)
        responseList.each { LiveSessionResponse response ->
            if (response.percentCredit == 100) {
                goodResponseIdList << response.id
            } else {
                badResponseIdList << response.id
            }
        }
        def map
        // if one of the list is empty, only one value list
        if (goodResponseIdList.size() == 0) {
            map = responseValByResponseKey(badResponseIdList, badResponseIdList)
        } else if (badResponseIdList.size() == 0) {
            map = responseValByResponseKey(goodResponseIdList, goodResponseIdList)
        } else {
            def map1 = responseValByResponseKey(goodResponseIdList, badResponseIdList)
            def map2 = responseValByResponseKey(badResponseIdList, goodResponseIdList)
            map = map1 + map2
        }
        map

    }

    public Map<Long, Long> responseValByResponseKey(List<Long> responseKeyList, List<Long> responseValueList) {
        Map<Long, Long> res = [:]
        def valuesCount = Math.min(4, responseValueList.size())
        // apply round robin algorithm
        int indexValues = 0
        responseKeyList.each { Long keyId ->
            Long valId = responseValueList.get(indexValues % valuesCount)
            indexValues++
            res.put(keyId, valId)
        }
        res
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
            if (response.percentCredit == 100 && response.explanation?.content?.size() > MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED) {
                explanationList << response.id
            }
        }
        // matching algorythm
        explanationIdListByUserIdRoundRobinAlgorithm(userIdList, explanationList)
    }

    /**
     * Get the map matching user with explanation to evaluate with round robin algorithm
     * @param userIdList the list of user id (the keys)
     * @param explanationList the list of explanation to distribute among all users
     * @return the map
     */
    public Map<Long, List<Long>> explanationIdListByUserIdRoundRobinAlgorithm(List<Long> userIdList, List<Long> explanationList) {
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
