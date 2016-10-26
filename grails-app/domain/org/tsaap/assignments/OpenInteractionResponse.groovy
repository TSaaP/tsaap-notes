package org.tsaap.assignments


/**
 * Class reprensenting an OpenInteractionResponse
 */
class OpenInteractionResponse extends InteractionResponse {

    /**
     * Get the number of assessers
     * @return the number of assessers
     */
    int evaluationCount() {
        PeerGrading.countByOpenResponse(this)
    }

    /**
     * Indicate if the response is a choice response
     * @return false
     */
    boolean isChoiceResponse() {
        false
    }
}
