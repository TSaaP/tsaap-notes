package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.assignments.statement.ChoiceInteractionType
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.assignments.statement.ChoiceSpecification
import org.tsaap.assignments.statement.ChoiceItemSpecification
import org.tsaap.assignments.statement.ExplanationChoice
import org.tsaap.attachement.AttachementService
import org.tsaap.directory.User
import grails.transaction.Transactional
import org.tsaap.skin.SkinUtil

@Transactional(readOnly = true)
class SequenceController {

  SpringSecurityService springSecurityService
  AssignmentService assignmentService
  SequenceService sequenceService
  MarkupSanitizerService markupSanitizerService
  AttachementService attachementService

  static allowedMethods = [save: "POST", update: "PUT"]


  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  @Transactional
  def saveSequence() {
    Assignment assignmentInstance = Assignment.get(params.assignment_instance_id)
    if (assignmentInstance == null) {
      notFound()
      return
    }
    User owner = springSecurityService.currentUser
    Statement statementInstance = getStatementInstanceToSave(owner, params)
    Sequence sequenceInstance = sequenceService.createAndAddSequenceToAssignment(assignmentInstance, owner, statementInstance)

    if (sequenceInstance.hasErrors()) {
      respond assignmentInstance, model: [sequenceInstance: sequenceInstance],
          view: '/assignment/sequence/' +
              SkinUtil.getView(params, session, 'create_sequence')
      return
    }

    List<FakeExplanationDto> fakeExplanationDtos = getFakeExplanationDtos(params)
    sequenceService.updateFakeExplanationListToStatement(fakeExplanationDtos, statementInstance, owner)

    attachFileIfAny(statementInstance, request)

    flash.message = message(code: 'sequence.updated.message',
        args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title.encodeAsRaw()])
    
    redirect controller: "sequence", action: "editSequence", params: [id: sequenceInstance.id]
  }


  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def editSequence(Sequence sequenceInstance) {
    respond sequenceInstance, view: "/assignment/sequence/" +
        SkinUtil.getView(params, session, 'edit_sequence')
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  @Transactional
  def updateSequence() {
    Sequence sequenceInstance = Sequence.get(params.sequence_instance_id)
    if (sequenceInstance == null) {
      notFound()
      return
    }
    User user = springSecurityService.currentUser
    Statement statementInstance = getStatementInstanceToUpdate(sequenceInstance, params)
    sequenceService.saveOrUpdateStatement(statementInstance, sequenceInstance)

    if (sequenceInstance.hasErrors()) {
      respond sequenceInstance, view: '/assignment/sequence/' + SkinUtil.getView(params, session, 'edit_sequence')
      return
    }

    List<FakeExplanationDto> fakeExplanationDtos = getFakeExplanationDtos(params)
    sequenceService.updateFakeExplanationListToStatement(fakeExplanationDtos, statementInstance, user)

    attachFileIfAny(statementInstance, request)

    Assignment assignmentInstance = sequenceInstance.assignment
    flash.message = message(
        code: 'sequence.updated.message.variant',
        args: [
            sequenceInstance.title.encodeAsRaw()]
    )
    redirect action: "editSequence", controller: "sequence", params: [id: sequenceInstance.id]

  }


  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  @Transactional
  def deleteSequence(Sequence sequenceInstance) {
    if (sequenceInstance == null) {
      notFound()
      return
    }
    def assignmentInstance = sequenceInstance.assignment
    def user = springSecurityService.currentUser
    assignmentService.removeSequenceFromAssignment(sequenceInstance, assignmentInstance, user)

    flash.message = message(code: 'sequence.deleted.message',
        args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title.encodeAsRaw()])
    redirect assignmentInstance

  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  @Transactional
  def upSequence(Sequence sequenceInstance) {
    if (sequenceInstance == null) {
      notFound()
      return
    }
    Assignment assignmentInstance = sequenceInstance.assignment
    User user = springSecurityService.currentUser
    def sequenceInstanceList = assignmentInstance.sequences
    def indexInList = sequenceInstanceList.indexOf(sequenceInstance)

    assignmentService.swapSequences(assignmentInstance, user, sequenceInstance, sequenceInstanceList[indexInList - 1])

    redirect(controller: 'assignment', action: 'show', id: assignmentInstance.id, fragment: "sequence_"+sequenceInstance.id)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  @Transactional
  def downSequence(Sequence sequenceInstance) {
    if (sequenceInstance == null) {
      notFound()
      return
    }
    Assignment assignmentInstance = sequenceInstance.assignment
    User user = springSecurityService.currentUser
    def sequenceInstanceList = assignmentInstance.sequences
    def indexInList = sequenceInstanceList.indexOf(sequenceInstance)

    assignmentService.swapSequences(assignmentInstance, user, sequenceInstance, sequenceInstanceList[indexInList + 1])
    redirect(controller: 'assignment', action: 'show', id: assignmentInstance.id, fragment: "sequence_"+sequenceInstance.id)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  @Transactional
  def removeAttachement(Statement statementInstance) {
    if (statementInstance == null) {
      notFound()
      return
    }
    def attachment = statementInstance.attachment
    def user = springSecurityService.currentUser
    if (attachment && statementInstance.owner == user) {
      attachementService.detachAttachement(attachment)
    }

    render """<input type="file" id="myFile" name="myFile" title="Image: gif, jpeg and png only"
       style="margin-top: 5px"/>"""
  }


  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def findAllFakeExplanation(Statement statementInstance) {
    if (statementInstance == null) {
      notFound()
      return
    }
    def user = springSecurityService.currentUser
    List<FakeExplanation> explanations = sequenceService.findAllFakeExplanationsForStatement(statementInstance, user)
    render(contentType: "application/json") {
      if (explanations) {
        for (e in explanations) {
          element content: e.content, correspondingItem: e.correspondingItem
        }
      } else {
        []
      }
    }
  }


  protected void notFound() {
    flash.message = message(code: 'default.not.found.message',
        args: [message(code: 'assignment.label', default: 'Assignment'), params.id])
    redirect action: "index", method: "GET", controller: 'assignment'
  }

  private Statement getStatementInstanceToUpdate(Sequence sequenceInstance, def params) {
    Statement statementInstance = sequenceInstance.statement
    statementInstance.choiceSpecification = getChoiceSpecification(params, sequenceInstance)?.jsonString
    populateStatementFromParams(params, statementInstance)
  }

  private Statement getStatementInstanceToSave(User owner, def params) {
    Statement statementInstance = new Statement(owner: owner)
    statementInstance.choiceSpecification = getChoiceSpecification(params)?.jsonString
    populateStatementFromParams(params, statementInstance)
  }

  private Statement populateStatementFromParams(params, Statement statementInstance) {
    statementInstance.title = params.title
    statementInstance.content = markupSanitizerService.sanitize(params.content)?.cleanString
    statementInstance.expectedExplanation = markupSanitizerService.sanitize(params.expectedExplanation)?.cleanString
    statementInstance.questionType = getQuestionType(params)
    statementInstance
  }

  private List<FakeExplanationDto> getFakeExplanationDtos(params) {
    def wrapper = new FakeExplanationDtosWrapper()
    bindData(wrapper, params)
    wrapper.fakeExplanations.findAll { it != null }
  }


  private QuestionType getQuestionType(def params) {
    boolean hasChoices = params.hasChoices.toBoolean()
    QuestionType questionType

    if (hasChoices) {
      if (params.choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()) {
        questionType = QuestionType.MultipleChoice
      } else {
        questionType = QuestionType.ExclusiveChoice
      }
    } else {
      questionType = QuestionType.OpenEnded
    }

    questionType
  }

  private ChoiceSpecification getChoiceSpecification(def params, Sequence sequence = null) {
    ChoiceSpecification choiceSpecification = sequence?.statement?.choiceSpecificationObject ?: new ChoiceSpecification()
    boolean hasChoices = params.hasChoices.toBoolean()
    List<ExplanationChoice> explanationChoiceList
    if (hasChoices) {
      choiceSpecification.choiceInteractionType = params.choiceInteractionType
      choiceSpecification.itemCount = params.itemCount as Integer
      if (params.choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()) {
        def expectedChoiceList = params.expectedChoiceList
        def countExpectedChoice = expectedChoiceList?.size() as Float
        choiceSpecification.expectedChoiceList = expectedChoiceList.collect {
          new ChoiceItemSpecification(it as Integer,
              (100 / countExpectedChoice) as Float)
        }
      } else {
        def exclusiveChoice = params.exclusiveChoice as Integer

        if (exclusiveChoice != null) {
          choiceSpecification.expectedChoiceList = [new ChoiceItemSpecification(exclusiveChoice, 100f)]
        } else {
          choiceSpecification.expectedChoiceList = []
        }
      }
      explanationChoiceList = getExplanationChoiceListFromChoiceQuestionType(params)
    } else {
      choiceSpecification.choiceInteractionType = QuestionType.OpenEnded.name()
      explanationChoiceList = getExplanationChoiceListFromOpenEndedQuestionType(params)
      // when transform multiple or exclusive sequence in openEnded sequence
      choiceSpecification.specificationProperties.remove("itemCount")
      choiceSpecification.specificationProperties.remove("expectedChoiceList")
    }

    if (explanationChoiceList.size() > 0) {
      choiceSpecification.explanationChoiceList = explanationChoiceList
    }

    choiceSpecification
  }

  private List<ExplanationChoice> getExplanationChoiceListFromChoiceQuestionType(def params) {
    List<ExplanationChoice> explanationChoiceList = new ArrayList<>();
    if (params.explanations) {
      params.explanations.eachWithIndex { String explanation, int index ->
        if (explanation != null && !explanation.trim().isEmpty()) {
          explanationChoiceList.add(new ExplanationChoice(index + 1, explanation))
        }
      }
    }

    explanationChoiceList
  }

  private List<ExplanationChoice> getExplanationChoiceListFromOpenEndedQuestionType(def params) {
    List<ExplanationChoice> explanationChoiceList = new ArrayList<>()
    if (params.openEndedExplanations) {
      String[] scores = params.scores
      params.openEndedExplanations.eachWithIndex { String explanation, int index ->
        if (explanation != null && !explanation.trim().isEmpty()) {
          if (scores != null && scores.size() > 0) {
            Float score = scores[index] != null && !scores[index].trim().isEmpty() ? Float.parseFloat(scores[index]) : 0
            explanationChoiceList.add(new ExplanationChoice(index + 1, explanation, score))
          }
        }
      }
    }
    explanationChoiceList
  }


  private void attachFileIfAny(Statement statementInstance, def request) {
    def file = request.getFile('myFile')
    if (file && !file.isEmpty()) {
      attachementService.addFileToStatement(file, statementInstance)
    }
  }

}

class FakeExplanationDtosWrapper {
  List<FakeExplanationDto> fakeExplanations = []
}

