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
                    view: '/assignment/sequence/create_sequence'
            return
        }

        attachFileIfAny(statementInstance, request)

        flash.message = message(code: 'sequence.updated.message',
                args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title.encodeAsRaw()])
        redirect action: "show", controller: "assignment", params: [id: assignmentInstance.id]
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSequence(Sequence sequenceInstance) {
        respond sequenceInstance, view: "/assignment/sequence/edit_sequence"
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
        sequenceService.saveOrUpdateStatement(statementInstance,sequenceInstance)

        if (sequenceInstance.hasErrors()) {
            respond sequenceInstance, view: '/assignment/sequence/edit_sequence'
            return
        }

        attachFileIfAny(statementInstance, request)

        Assignment assignmentInstance = sequenceInstance.assignment
        flash.message = message(code: 'sequence.updated.message',
                args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title.encodeAsRaw()])
        redirect action: "show", controller: "assignment", params: [id: assignmentInstance.id]

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

        redirect assignmentInstance

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
        redirect assignmentInstance
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

    protected void notFound() {
        flash.message = message(code: 'default.not.found.message',
                args: [message(code: 'assignment.label', default: 'Assignment'), params.id])
        redirect action: "index", method: "GET", controller: 'assignment'
    }

    private Statement getStatementInstanceToUpdate(Sequence sequenceInstance, def params) {
        Statement statementInstance = sequenceInstance.statement
        statementInstance.title = params.title
        statementInstance.content = markupSanitizerService.sanitize(params.content)?.cleanString
        statementInstance.questionType = getQuestionType(params)
        statementInstance.choiceSpecification = getChoiceSpecification(params, sequenceInstance)?.jsonString

        statementInstance
    }

    private Statement getStatementInstanceToSave(User owner, def params) {
        Statement statementInstance = new Statement(title: params.title)
        statementInstance.content = markupSanitizerService.sanitize(params.content)?.cleanString
        statementInstance.owner = owner
        statementInstance.questionType = getQuestionType(params)
        statementInstance.choiceSpecification = getChoiceSpecification(params)?.jsonString

        statementInstance
    }

    private QuestionType getQuestionType(def params)  {
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
      ChoiceSpecification choiceSpecification =  sequence?.statement?.choiceSpecificationObject ?: new ChoiceSpecification()
      boolean hasChoices = params.hasChoices.toBoolean()
        List<ExplanationChoice> explanationChoiceList = null;
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

    private List<ExplanationChoice> getExplanationChoiceListFromChoiceQuestionType (def params) {
        List<ExplanationChoice> explanationChoiceList = new ArrayList<>();
        if (params.explanations) {
            params.explanations.eachWithIndex{ String explanation, int index ->
                if (explanation != null && !explanation.trim().isEmpty()) {
                    explanationChoiceList.add(new ExplanationChoice(index + 1, explanation))
                }
            }
        }

        explanationChoiceList
    }

    private List<ExplanationChoice> getExplanationChoiceListFromOpenEndedQuestionType (def params) {
        List<ExplanationChoice> explanationChoiceList = new ArrayList<>();
        if (params.openEndedExplanations) {
            String [] scores = params.scores
            params.openEndedExplanations.eachWithIndex{ String explanation, int index ->
                if (explanation != null && !explanation.trim().isEmpty()) {
                    if (scores != null && scores.size() > 0) {
                        Float score = scores[index] != null  && !scores[index].trim().isEmpty() ? Float.parseFloat(scores[index]) : 0
                        explanationChoiceList.add(new ExplanationChoice(index + 1, explanation, score))
                    }
                }
            }
        }
        explanationChoiceList
    }

    private ResponseSubmissionSpecification getResponseSubmissionSpecificationToSaveOrUpdate(GrailsParameterMap params, Sequence sequence = null) {
      ResponseSubmissionSpecification subSpec = sequence?.responseSubmissionSpecification ?: new ResponseSubmissionSpecification()
      subSpec
    }

    private void attachFileIfAny(Statement statementInstance, def request) {
        def file = request.getFile('myFile')
        if (file && !file.isEmpty()) {
            attachementService.addFileToStatement(file, statementInstance)
        }
    }

    private EvaluationSpecification getEvaluationSpecificationToSave(ResponseSubmissionSpecification subSpec,
                                                                     def params) {
        EvaluationSpecification evalSpec = null
        evalSpec = new EvaluationSpecification()
        evalSpec
    }

    private EvaluationSpecification getEvaluationSpecificationToUpdate(ResponseSubmissionSpecification subSpec, GrailsParameterMap params, Sequence sequence) {
        EvaluationSpecification evalSpec = sequence?.evaluationSpecification
        if (subSpec.studentsProvideExplanation) {
            if (!evalSpec) {
                evalSpec = new EvaluationSpecification()
            }
            evalSpec.responseToEvaluateCount = params.responseToEvaluateCount as Integer ?: 1
        }
        evalSpec
    }

    private List<Interaction> getInteractionsToSave(
            def params, ResponseSubmissionSpecification subSpec, EvaluationSpecification evalSpec, Assignment assignment) {
        List<Interaction> interactions
        def interaction1 = new Interaction(interactionType: InteractionType.ResponseSubmission.name(), rank: 1,
                specification: subSpec.jsonString,
                schedule: getNewSchedule(params, 1))
        interactions = [interaction1]
        def interaction2 = new Interaction(interactionType: InteractionType.Evaluation.name(), rank: 2,
                specification: evalSpec.jsonString, schedule: getNewSchedule(params, 2))
        if (!subSpec.studentsProvideExplanation) {
            interaction2.enabled = false
        }
        interactions.add(interaction2)
        def interaction3 = new Interaction(interactionType: InteractionType.Read.name(), rank: 3, specification: Interaction.EMPTY_SPECIFICATION,
                schedule: getNewSchedule(params, 3))
        interactions.add(interaction3)
        interactions
    }


    private List<Interaction> getInteractionsToAddAtUpdate(
            def params, ResponseSubmissionSpecification responseSubmissionSpecification, EvaluationSpecification evaluationSpecification, Sequence sequence) {
        List<Interaction> interactions = []

        Interaction interaction1 = sequence.responseSubmissionInteraction
        if (interaction1) {
            interaction1.specification = responseSubmissionSpecification.jsonString
            interaction1.enabled = true
            interaction1.schedule.startDate = getStartDate(params, "startDate1")
            interaction1.schedule.endDate = getEndDate(params, "endDate1")
        } else {
            interaction1 = new Interaction(interactionType: InteractionType.ResponseSubmission.name(), rank: 1,
                    specification: responseSubmissionSpecification.jsonString,
                    schedule: getNewSchedule(params, 1))
            interactions.add(interaction1)
        }


        Interaction interaction2 = sequence.evaluationInteraction
        if (!responseSubmissionSpecification.studentsProvideExplanation) {
            if (interaction2) {
                interaction2.enabled = false
            }
        } else if (interaction2) {
            interaction2.specification = evaluationSpecification.jsonString
            interaction2.enabled = true
            interaction2.schedule.startDate = getStartDate(params, "startDate2")
            interaction2.schedule.endDate = getEndDate(params, "endDate2")
        } else {
            interaction2 = new Interaction(interactionType: InteractionType.Evaluation.name(), rank: 2,
                    specification: evaluationSpecification.jsonString,
                    schedule: getNewSchedule(params, 2))
            interactions.add(interaction2)
        }

        Interaction interaction3 = sequence.readInteraction
        if (interaction3) {
            interaction3.schedule.startDate = getStartDate(params, "startDate3")
            interaction3.schedule.endDate = getEndDate(params, "endDate3")
        } else {
            interaction3 = new Interaction(interactionType: InteractionType.Read.name(), rank: 3, specification: Interaction.EMPTY_SPECIFICATION,
                    schedule: getNewSchedule(params, 3))
            interactions.add(interaction3)
        }

        interactions
    }

    private Schedule getNewSchedule(def params, int indexInteraction) {
        Date startDate = getStartDate(params, "startDate${indexInteraction}")
        Date endDate = getEndDate(params, "endDate${indexInteraction}")
        def res = new Schedule(startDate: startDate, endDate: endDate)
        res
    }

    private Date getStartDate(def params, String paramName) {
        if (params.("$paramName")) {
            return new Date().parse(message(code: 'date.startDate.format'), params.("$paramName"))
        }
        null
    }

    private Date getEndDate(def params, String paramName) {
        if (params.("$paramName")) {
            return new Date().parse(message(code: 'date.endDate.format'), params.("$paramName"))
        }
        null
    }

}
