package org.tsaap.assignments

import org.tsaap.directory.User


/**
 * Class reprensenting an OpenInteractionResponse
 */
class OpenInteractionResponse extends InteractionResponse {

    /**
     * Get the number of assessers
     * @return the number of assessers
     */
    @Override
    int evaluationCount() {
        PeerGrading.countByOpenResponse(this)
    }

    /**
     * Indicate if the response is a choice response
     * @return false
     */
    @Override
    boolean isChoiceResponse() {
        false
    }

    /**
     * Get the grade from the given user for this response
     * @param user the grader
     * @return the grade if any
     */
    @Override
    Float getGradeFromUser(User user) {
        PeerGrading pg = PeerGrading.findByOpenResponseAndGrader(this,user)
        pg?.grade
    }
}
