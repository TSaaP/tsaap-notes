package org.tsaap.ia.conflict

import org.tsaap.questions.LiveSessionResponse


class SocioCognitiveConflictService {

    static transactional = false

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
        response
    }
}
