package org.tsaap.assignments


/**
 * Class reprensenting an OpenInteractionResponse
 */
class OpenInteractionResponse {

    /**
     * Get the number of assessers
     * @return the number of assessers
     */
    int evaluationCount() {
        PeerGrading.countByOpenResponse(this)
    }
}
