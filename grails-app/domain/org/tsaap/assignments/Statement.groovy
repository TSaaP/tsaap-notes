package org.tsaap.assignments

import org.tsaap.assignments.statement.ChoiceSpecification
import org.tsaap.attachement.Attachement
import org.tsaap.directory.User

class Statement {

    String title
    String content
    String expectedExplanation
    String choiceSpecification
    QuestionType questionType
    Statement parentStatement

    Date dateCreated
    Date lastUpdated

    User owner

    static constraints = {
        title blank: false
        content blank: false
        questionType nullable: false
        choiceSpecification nullable: true, validator: { val, obj ->
            if ((obj.questionType == QuestionType.MultipleChoice || obj.questionType == QuestionType.ExclusiveChoice)
                    && !val) return ['choiceSpecificationMustBeSet']
        }
        parentStatement nullable: true
        expectedExplanation nullable: true
    }

    static transients = ['choiceSpecificationObject', 'fakeExplanations']

    /**
     * Get the list of fake explanations for this statement
     * @return the list of fake explanations
     */
    List<FakeExplanation> getFakeExplanations() {
        FakeExplanation.findAllByStatement(this)
    }

    /**
     * Get the choice specification object
     * @return the choice specification
     */
    ChoiceSpecification getChoiceSpecificationObject() {
        if (choiceSpecification) {
            new ChoiceSpecification(choiceSpecification);
        } else {
            null
        }
    }

    /**
     *
     * @return true if statement describes a choice question
     */
    boolean hasChoices() {
        questionType == QuestionType.ExclusiveChoice || questionType == QuestionType.MultipleChoice
    }

    /**
     *
     * @return true if statement describes an open-ended question
     */
    boolean isOpenEnded() {
        questionType == QuestionType.OpenEnded
    }

    /**
     *
     * @return true if statement describes a multiple choice question
     */
    boolean isMultipleChoice() {
        questionType == QuestionType.MultipleChoice
    }

    /**
     *
     * @return true if statement describes an exclusive choice question
     */
    boolean isExclusiveChoice() {
        questionType == QuestionType.ExclusiveChoice
    }

    /**
     * Get the attachment
     * @return the attachment
     */
    Attachement getAttachment() {
        if (id == null) {
            return null
        }
        Attachement.findByStatement(this)
    }
}

/**
 * Created by qsaieb on 01/03/2017.
 */
public enum QuestionType {
    Undefined,
    ExclusiveChoice,
    MultipleChoice,
    OpenEnded
}