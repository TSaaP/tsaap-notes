package org.tsaap.assignments.ia

import grails.transaction.Transactional
import org.tsaap.assignments.ChoiceInteractionResponse
import org.tsaap.contracts.Contract
import org.tsaap.directory.User

@Transactional
class DefaultResponseRecommendationService implements ResponseRecommendationService {

    private Map<Long, List<ChoiceInteractionResponse>> recommendationsByResponse
    private static final int MIN_SIZE_OF_EXPLANATION_TO_BE_EVALUATED = 10

    @Override
    List<ChoiceInteractionResponse> findRecommendedResponsesForUser(User user, ChoiceInteractionResponse userResponse, List<ChoiceInteractionResponse> responses, Integer max = 1) {
        Contract.requires(userResponse?.learner == user, "User is not the response author")
        Contract.requires(max in 1..3, "Max recommendations must be between 1 and 3")
        if (!responses || (responses.size() == 1 && responses[0] == userResponse)) {
            return []
        }
        def recommendationsMap = getRecommendationsByResponse(responses,max)
        recommendationsMap.get(userResponse.id)
    }

    Map<Long, List<ChoiceInteractionResponse>> getRecommendationsByResponse(List<ChoiceInteractionResponse> responseList, Integer max = 1) {
        if (!recommendationsByResponse) {
            // sort response by level of confidence degree
            responseList = responseList.sort(false) { -it.confidenceDegree }
            // the good responses
            List<ChoiceInteractionResponse> goodResponseList = []
            // the good responses candidates for conflict
            List<ChoiceInteractionResponse> goodResponseConflictList = []
            // the bad responses
            List<ChoiceInteractionResponse> badResponseList = []
            // the bad responses candidates for conflict
            List<ChoiceInteractionResponse> badResponseConflictList = []
            // all responses candidates for conflict
            List<ChoiceInteractionResponse> responseConflictList = []
            // the way to set this two lists and to create two maps of responses (the good and the one)
            for(int i=0; i < responseList.size() ; i++) {
                ChoiceInteractionResponse response = responseList[i]
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
            def map1 = responseValByResponseKey(goodResponseList, badResponseConflictList,goodResponseConflictList,responseConflictList, max)
            def map2 = responseValByResponseKey(badResponseList, goodResponseConflictList,badResponseConflictList,responseConflictList, max)
            recommendationsByResponse = map1 + map2
        }
        recommendationsByResponse
    }

    public Map<Long, List<ChoiceInteractionResponse>> responseValByResponseKey(
            List<ChoiceInteractionResponse> responseKeyList,
            List<ChoiceInteractionResponse> mainResponseValueList,
            List<ChoiceInteractionResponse> secondaryResponseValueList,
            List<ChoiceInteractionResponse> allResponseValueList,
            Integer max) {
        if (allResponseValueList.isEmpty()) {
            return [:]
        }
        Map<Long, List<ChoiceInteractionResponse>> res = [:]
        // first recommendation
        List<ChoiceInteractionResponse> currentResponseValueList = mainResponseValueList
        int indexValues = 0
        findAResponseValForEachResponseKey(currentResponseValueList, allResponseValueList, responseKeyList, res, indexValues)
        if (max > 1) { // second recommendation
            indexValues = 1 // schift in order to limit risk of recommendation duplication
            currentResponseValueList = (max == 2) ? secondaryResponseValueList : mainResponseValueList
            findAResponseValForEachResponseKey(currentResponseValueList, allResponseValueList, responseKeyList, res, indexValues)
        }
        if (max > 2) { // third recommendation
            indexValues = 2 // schift in order to limit risk of recommendation duplication
            currentResponseValueList = mainResponseValueList
            findAResponseValForEachResponseKey(currentResponseValueList, allResponseValueList, responseKeyList, res, indexValues)
        }
        res
    }

    private void findAResponseValForEachResponseKey(List<ChoiceInteractionResponse> currentResponseValueList,
                                                    List<ChoiceInteractionResponse> allResponseValueList,
                                                    List<ChoiceInteractionResponse> responseKeyList,
                                                    Map<Long,List<ChoiceInteractionResponse>> res,
                                                    int indexValues) {
       if (!currentResponseValueList) {
            currentResponseValueList = allResponseValueList
        }
        int valuesCount = currentResponseValueList.size()
        responseKeyList.each { ChoiceInteractionResponse keyResponse -> // round robin in currentResponseValueList
            ChoiceInteractionResponse valResponse = currentResponseValueList.get(indexValues % valuesCount)
            indexValues++
            List<ChoiceInteractionResponse> recommendations = res.get(keyResponse.id)
            if (recommendations == null) {
                res.put(keyResponse.id, [valResponse])
            } else {
                if (!recommendations.contains(valResponse)) {
                    recommendations << valResponse
                }
            }
        }
    }
}
