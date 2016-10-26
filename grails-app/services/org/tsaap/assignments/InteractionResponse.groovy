package org.tsaap.assignments

import org.tsaap.directory.User

/**
 * Class reprensenting an InteractionResponse
 */
abstract class InteractionResponse {

    Date dateCreated
    Date lastUpdated

    User learner
    Interaction interaction
    Integer attempt = 1

    String explanation
    Integer confidenceDegree

    Float meanGrade

    static constraints = {
        confidenceDegree nullable: true
        meanGrade nullable: true
        explanation nullable: true
    }

    /**
     * Get the assignment relative to this response
     * @return the assignment
     */
    Assignment assignment() {
        interaction.sequence.assignment
    }

    /**
     * Check if first attempt is submitable
     * @return true if first attempt is submitable
     */
    boolean firstAttemptIsSubmitable() {
        interaction.state == StateType.show.name() && attempt == 1
    }

    /**
     * Check if second attempt is submitable
     * @return true if second attempt is submitable
     */
    boolean secondAttemptIsSubmitable() {
        interaction.state == StateType.afterStop.name() && attempt == 2
    }

    /**
     * Get the number of assessers
     * @return the number of assessers
     */
    int evaluationCount() {
        PeerGrading.countByResponse(this)
    }

    /**
     * Check if the response is a choice response
     * @return true if the response is a choice response false otherwise
     */
    abstract boolean isChoiceResponse();
}

enum ConfidenceDegreeEnum {
    NOT_CONFIDENT_AT_ALL,
    NOT_REALLY_CONFIDENT,
    CONFIDENT,
    TOTALLY_CONFIDENT

    String getName() {
        name()
    }

    int getIntegerValue() {
        ordinal()
    }
}