package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
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

        ResponseSubmissionSpecification subSpec = getResponseSubmissionSpecificationToSaveOrUpdate(params)
        EvaluationSpecification evalSpec = getEvaluationSpecificationToSave(subSpec, params)

        List<Interaction> interactions = getInteractionsToSave(subSpec, evalSpec)

        Sequence sequenceInstance = sequenceService.addSequenceToAssignment(assignmentInstance,owner, statementInstance,interactions)

        if (sequenceInstance.hasErrors()) {
            respond assignmentInstance, model:[statementInstance: statementInstance,
                                               responseSubmissionSpecificationInstance:subSpec,
                                               evaluationSpecificationInstance:evalSpec], view:'create_sequence'
            return
        }

        attachFileIfAny(statementInstance, request)

        flash.message = message(code: 'sequence.updated.message',
                args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title.encodeAsRaw()])
        redirect action: "show", controller: "assignment", params: [id:assignmentInstance.id]

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

        ResponseSubmissionSpecification subSpec = getResponseSubmissionSpecificationToSaveOrUpdate(params, sequenceInstance)
        EvaluationSpecification evalSpec = getEvaluationSpecificationToUpdate(subSpec, params, sequenceInstance)

        List<Interaction> interactions = getInteractionsToAddAtUpdate(subSpec, evalSpec,sequenceInstance)

        sequenceService.updateStatementAndInteractionsOfSequence(sequenceInstance, user, interactions)

        if (sequenceInstance.hasErrors()) {
            respond sequenceInstance, view:'edit_sequence'
            return
        }

        attachFileIfAny(statementInstance, request)

        Assignment assignmentInstance = sequenceInstance.assignment
        flash.message = message(code: 'sequence.updated.message',
                args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title.encodeAsRaw()])
        redirect action: "show", controller: "assignment", params: [id:assignmentInstance.id]

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
        assignmentService.removeSequenceFromAssignment(sequenceInstance, assignmentInstance,user)

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

        assignmentService.swapSequences(assignmentInstance, user,sequenceInstance,sequenceInstanceList[indexInList-1])

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

        assignmentService.swapSequences(assignmentInstance, user,sequenceInstance,sequenceInstanceList[indexInList+1])
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
        if ( attachment && statementInstance.owner == user) {
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
        statementInstance
    }

    private Statement getStatementInstanceToSave(User owner, def params) {
        Statement statementInstance = new Statement(title: params.title)
        statementInstance.content = markupSanitizerService.sanitize(params.content)?.cleanString
        statementInstance.owner = owner
        statementInstance
    }


    private ResponseSubmissionSpecification getResponseSubmissionSpecificationToSaveOrUpdate(GrailsParameterMap params, Sequence sequence = null) {
        ResponseSubmissionSpecification subSpec = sequence?.responseSubmissionSpecification ?: new ResponseSubmissionSpecification()
        subSpec.choiceInteractionType = params.choiceInteractionType
        subSpec.itemCount = params.itemCount as Integer
        if (subSpec.isMultipleChoice()) {
            subSpec.expectedChoiceList = params.expectedChoiceList?.collect { it as Integer }
        } else {
            subSpec.expectedChoiceList = [params.exclusiveChoice as Integer]
        }

        subSpec.studentsProvideExplanation = params.studentsProvideExplanation as boolean
        if (subSpec.studentsProvideExplanation) {
            subSpec.studentsProvideConfidenceDegree = true
        } else {
            subSpec.studentsProvideConfidenceDegree = params.studentsProvideConfidenceDegree as boolean
        }
        subSpec
    }

    private void attachFileIfAny(Statement statementInstance, def request) {
        def file = request.getFile('myFile')
        if (file && !file.isEmpty()) {
            attachementService.addFileToStatement(file, statementInstance)
        }
    }

    private EvaluationSpecification getEvaluationSpecificationToSave(ResponseSubmissionSpecification subSpec, def params) {
        EvaluationSpecification evalSpec = null
        if (subSpec.studentsProvideExplanation) {
            evalSpec = new EvaluationSpecification(responseToEvaluateCount: params.responseToEvaluateCount as Integer)
        }
        evalSpec
    }

    private EvaluationSpecification getEvaluationSpecificationToUpdate(ResponseSubmissionSpecification subSpec, GrailsParameterMap params, Sequence sequence) {
        EvaluationSpecification evalSpec = sequence?.evaluationSpecification
        if (subSpec.studentsProvideExplanation) {
            if (!evalSpec) {
                evalSpec = new EvaluationSpecification()
            }
            evalSpec.responseToEvaluateCount = params.responseToEvaluateCount as Integer
        }
        evalSpec
    }

    private List<Interaction> getInteractionsToSave(ResponseSubmissionSpecification subSpec, EvaluationSpecification evalSpec) {
        List<Interaction> interactions
        def interaction1 = new Interaction(interactionType: InteractionType.ResponseSubmission.name(), rank: 1,
                specification: subSpec.jsonString)
        interactions = [interaction1]
        if (evalSpec) {
            def interaction2 = new Interaction(interactionType: InteractionType.Evaluation.name(), rank: 2,
                    specification: evalSpec.jsonString)
            interactions.add(interaction2)
        }
        interactions
    }

    private List<Interaction> getInteractionsToAddAtUpdate(ResponseSubmissionSpecification responseSubmissionSpecification, EvaluationSpecification evaluationSpecification, Sequence sequence) {
        List<Interaction> interactions = []

        Interaction interaction1 = sequence.responseSubmissionInteraction
        if (interaction1) {
            interaction1.specification = responseSubmissionSpecification.jsonString
            interaction1.enabled = true
        } else {
            interaction1 = new Interaction(interactionType: InteractionType.ResponseSubmission.name(), rank: 1,
                    specification: responseSubmissionSpecification.jsonString)
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
        } else {
                interaction2 = new Interaction(interactionType: InteractionType.Evaluation.name(), rank: 2,
                        specification: evaluationSpecification.jsonString)
                interactions.add(interaction2)
        }
        interactions
    }

}
