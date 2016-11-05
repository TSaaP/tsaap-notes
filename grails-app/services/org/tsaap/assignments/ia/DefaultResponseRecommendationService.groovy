package org.tsaap.assignments.ia

import grails.transaction.Transactional
import org.tsaap.assignments.InteractionResponse
import org.tsaap.contracts.Contract

@Transactional
class DefaultResponseRecommendationService implements ResponseRecommendationService {

    private static final int MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED = 10
    private static final String RECOMMENDATION_COUNT_INVALID = "recommendation count not between 1 and 3: "

    /**
     * Build the explanation recommendation mapping
     * @param responseList the responses containing explanations
     * @param max the max number of recommended response for each response
     * @return the mapping as a map
     */
    Map<String, List<Long>> getRecommendedResponseIdByResponseId(List<InteractionResponse> responseList) {
        // sort response by level of confidence degree
        responseList = responseList.sort(false) { -it.confidenceDegree }
        // the good responses
        List<InteractionResponse> goodResponseList = []
        // the good responses candidates for conflict
        List<InteractionResponse> goodResponseConflictList = []
        // the bad responses
        List<InteractionResponse> badResponseList = []
        // the bad responses candidates for conflict
        List<InteractionResponse> badResponseConflictList = []
        // all responses candidates for conflict
        List<InteractionResponse> responseConflictList = []
        // the way to set this two lists and to create two maps of responses (the good and the one)
        for (int i = 0; i < responseList.size(); i++) {
            InteractionResponse response = responseList[i]
            if (response.score == 100f) {
                goodResponseList << response
                if (response.explanation?.size() > MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED) {
                    goodResponseConflictList << response
                    responseConflictList << response
                }
            } else {
                badResponseList << response
                if (response.explanation?.size() > MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED) {
                    badResponseConflictList << response
                    responseConflictList << response
                }
            }
        }
        def map1 = responseValByResponseKey(goodResponseList, badResponseConflictList, goodResponseConflictList, responseConflictList)
        def map2 = responseValByResponseKey(badResponseList, goodResponseConflictList, badResponseConflictList, responseConflictList)
        map1 + map2
    }

    private Map<String, List<Long>> responseValByResponseKey(
            List<InteractionResponse> responseKeyList,
            List<InteractionResponse> mainResponseValueList,
            List<InteractionResponse> secondaryResponseValueList,
            List<InteractionResponse> allResponseValueList) {
        if (allResponseValueList.isEmpty()) {
            return [:]
        }
        Map<String, List<Long>> res = [:]
        // first recommendation
        List<InteractionResponse> currentResponseValueList = mainResponseValueList
        int indexValues = 0
        findAResponseValForEachResponseKey(currentResponseValueList, allResponseValueList, responseKeyList, res, indexValues)
        // second recommendation
        indexValues = 1 // schift in order to limit the fact that a learner assesses his own response
        currentResponseValueList = secondaryResponseValueList
        findAResponseValForEachResponseKey(currentResponseValueList, allResponseValueList, responseKeyList, res, indexValues)
        // third recommendation
        indexValues = 1 // schift in order to limit risk of recommendation duplication
        currentResponseValueList = mainResponseValueList
        findAResponseValForEachResponseKey(currentResponseValueList, allResponseValueList, responseKeyList, res, indexValues)
        res
    }

    private void findAResponseValForEachResponseKey(List<InteractionResponse> currentResponseValueList,
                                                    List<InteractionResponse> allResponseValueList,
                                                    List<InteractionResponse> responseKeyList,
                                                    Map<String, List<Long>> res,
                                                    int indexValues) {
        if (!currentResponseValueList) {
            currentResponseValueList = allResponseValueList
        }
        int valuesCount = currentResponseValueList.size()
        responseKeyList.each { InteractionResponse keyResponse -> // round robin in currentResponseValueList
            InteractionResponse valResponse = currentResponseValueList.get(indexValues % valuesCount)
            indexValues++
            List<Long> recommendations = res.get(keyResponse.id as String)
            if (recommendations == null) {
                res.put(keyResponse.id as String, [valResponse.id])
            } else if (!recommendations.contains(valResponse.id)) {
                recommendations << valResponse.id
            }
        }
    }

    /**
     * Build the explanation recommendation mapping
     * @param responseList the responses containing explanations
     * @param recommendationCount the number of recommended responses to associate to one given response
     * @return the mapping as a map
     */
    Map<String, List<Long>> getRecommendedResponseIdByResponseIdForOpenQuestion(List<InteractionResponse> responseList, int recommendationCount = 3) {
        Contract.requires(recommendationCount > 0 && recommendationCount < 4, RECOMMENDATION_COUNT_INVALID + recommendationCount)
        Map<String, List<Long>> res = [:]
        def responseKeyList = new ArrayList<InteractionResponse>()
        responseKeyList.addAll(responseList)
        Collections.shuffle(responseKeyList)
        def responseValueList = responseList.findAll { it.explanation?.length() > MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED }
        int valuesCount = responseValueList.size()
        // apply round robin algorithm
        int indexValues = 0
        responseKeyList.each { InteractionResponse keyResponse ->
            List<Long> explIdList = []
            for (int i = 0; i < recommendationCount; i++) {
                explIdList << responseValueList.get(indexValues % valuesCount).id
                indexValues++
            }
            res.put(keyResponse.id as String, explIdList)
        }
        res
    }

}

