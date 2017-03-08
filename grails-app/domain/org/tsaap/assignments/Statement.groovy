package org.tsaap.assignments

import org.tsaap.assignments.statement.ChoiceSpecification
import org.tsaap.attachement.Attachement
import org.tsaap.directory.User

class Statement {

    String title
    String content
    String choiceSpecification
    QuestionType questionType

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
    }

    static transients = ['choiceSpecificationObject']


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

    boolean hasChoices () {
        questionType == QuestionType.ExclusiveChoice || questionType == QuestionType.MultipleChoice
    }

    boolean isOpenEnded () {
        questionType == QuestionType.OpenEnded
    }

    boolean isMultipleChoice () {
        questionType == QuestionType.MultipleChoice
    }

    boolean isExclusiveChoice () {
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
    OpenEnded;
}