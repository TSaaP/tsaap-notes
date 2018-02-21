package org.tsaap.assignments

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.tsaap.assignments.statement.ChoiceInteractionType
import org.tsaap.assignments.statement.ChoiceSpecification
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
    boolean firstAttemptIsSubmitable(User learner) {
        interaction.stateForLearner(learner) == StateType.show.name() && attempt == 1
    }

    /**
     * Check if second attempt is submitable
     * @return true if second attempt is submitable
     */
    boolean secondAttemptIsSubmitable(User learner) {
        interaction.stateForLearner(learner) == StateType.afterStop.name() && attempt == 2
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
        ChoiceSpecification choiceSpecification = interaction.sequence.statement.getChoiceSpecificationObject()
        List<Integer> expectedChoices = choiceSpecification.expectedChoiceList*.index
        if (choiceSpecification.choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()) {
            Float res = 0
            for (int i = 1; i <= choiceSpecification.itemCount ; i++) {
                if (choiceList().contains(i) && expectedChoices.contains(i)) {
                    res += 100f/choiceSpecification.itemCount
                } else if (!choiceList().contains(i) && !expectedChoices.contains(i)) {
                    res += 100f/choiceSpecification.itemCount
                } else {
                    res -= 100f/choiceSpecification.itemCount
                }
            }
            score = (res < 0) ? 0 : res
        } else if (choiceSpecification.choiceInteractionType == ChoiceInteractionType.EXCLUSIVE.name()) {
            def goodChoice = expectedChoices.get(0)
            score = choiceList().contains(goodChoice) ? 100 : 0
        }

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
     * Get the grade from the given user for this response
     * @param user the grader
     * @return the grade if any as a string
     */
    String getGradeFromUserAsString(User user) {
        def grade = getGradeFromUser(user)
        if (grade) {
            return (grade as Integer).toString()
        }
        return "-1"
    }

    /**
     * Count the number of evaluations for the current response
     * @return
     */
    Integer evaluationCount() {
        PeerGrading.countByResponseAndGradeNotEqual(this,-1f)
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

