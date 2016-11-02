package org.tsaap.assignments

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User


class InteractionResponse {

    Date dateCreated
    Date lastUpdated

    User learner
    Interaction interaction
    Integer attempt = 1

    String explanation
    Integer confidenceDegree

    Float meanGrade

    String choiceListSpecification

    Float score


    static constraints = {
        confidenceDegree nullable: true
        meanGrade nullable: true
        explanation nullable: true
        score nullable: true
        choiceListSpecification nullable: true
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
     * Get the choice list
     * @return the choice list
     */
    List<Integer> choiceList() {
        if (!choiceListSpecification) {
            return []
        }
        JsonSlurper jsonSlurper = new JsonSlurper()
        jsonSlurper.parseText(choiceListSpecification)
    }

    /**
     * Update the choice list specification
     * @param choiceList the input choice list
     */
    void updateChoiceListSpecification(List<Integer> choiceList) {
        if (choiceList != null) {
            choiceListSpecification = JsonOutput.toJson(choiceList)
        }
    }


    /**
     * Calculate and update the score property
     * @return the score value
     */
    Float updateScore() {
        ResponseSubmissionSpecification spec = interaction.interactionSpecification
        List<Integer> expectedChoices = spec.expectedChoiceList*.index
        Float res = 0
        for (int i = 1; i <= spec.itemCount ; i++) {
            if (choiceList().contains(i) && expectedChoices.contains(i)) {
                res += 100f/spec.itemCount
            } else if (!choiceList().contains(i) && !expectedChoices.contains(i)) {
                res += 100f/spec.itemCount
            } else {
                res -= 100f/spec.itemCount
            }
        }
        score = (res < 0) ? 0 : res
        score
    }

    /**
     * Indicate if the response is a choice response
     * @return true
     */
    boolean isChoiceResponse() {
        interaction.interactionSpecification.hasChoices()
    }

    /**
     * Get the grade from the given user for this response
     * @param user the grader
     * @return the grade if any
     */
    Float getGradeFromUser(User user) {
        PeerGrading pg = PeerGrading.findByResponseAndGrader(this,user)
        pg?.grade
    }

    /**
     * Count the number of evaluations for the current response
     * @return
     */
    Integer evaluationCount() {
        PeerGrading.countByResponse(this)
    }

    static mapping = {
        table name: "choice_interaction_response"
    }
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

