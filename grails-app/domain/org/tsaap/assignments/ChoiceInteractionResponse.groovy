package org.tsaap.assignments

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User


class ChoiceInteractionResponse extends InteractionResponse {

    String choiceListSpecification

    Float score


    static constraints = {
        score nullable: true
        choiceListSpecification nullable: true
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
    @Override
    boolean isChoiceResponse() {
        true
    }

    /**
     * Get the grade from the given user for this response
     * @param user the grader
     * @return the grade if any
     */
    @Override
    Float getGradeFromUser(User user) {
        PeerGrading pg = PeerGrading.findByResponseAndGrader(this,user)
        pg?.grade
    }
}

