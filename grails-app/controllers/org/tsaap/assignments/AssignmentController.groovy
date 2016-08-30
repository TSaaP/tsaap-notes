package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.grails.plugins.sanitizer.MarkupSanitizerResult
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.attachement.AttachementService
import org.tsaap.directory.User

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AssignmentController {

    SpringSecurityService springSecurityService
    AssignmentService assignmentService
    SequenceService sequenceService
    MarkupSanitizerService markupSanitizerService
    AttachementService attachementService

    static allowedMethods = [save: "POST", update: "PUT"]

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'lastUpdated'
        params.order = params.order ?: 'desc'
        respond Assignment.list(params), model:[assignmentInstanceCount: Schedule.count()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Assignment assignmentInstance) {
        respond assignmentInstance
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        Date now = new Date()
        [assignmentInstance:new Assignment(), scheduleInstance: new Schedule(startDate: now, endDate: now+1)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    @Transactional
    def save() {
        Assignment assignmentInstance = new Assignment(params)
        assignmentInstance.owner = springSecurityService.currentUser
        assignmentInstance.validate()
        Schedule scheduleInstance = new Schedule(startDate:getStartDate(params), endDate:getEndDate(params))
        scheduleInstance.validate()
        if (assignmentInstance.hasErrors() || scheduleInstance.hasErrors()) {
            respond assignmentInstance, model:[scheduleInstance:scheduleInstance], view:'create'
            return
        }

        assignmentService.saveAssignment(assignmentInstance, scheduleInstance)

        flash.message = message(code: 'assignment.created.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.title])
        redirect assignmentInstance

    }



    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(Assignment assignmentInstance) {
        Date now = new Date()
        Schedule scheduleInstance = assignmentInstance.schedule ?: new Schedule(startDate: now, endDate: now+1)
        [assignmentInstance: assignmentInstance, scheduleInstance: scheduleInstance]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    @Transactional
    def update() {
        Assignment assignmentInstance = Assignment.get(params.id)
        if (assignmentInstance == null) {
            notFound()
            return
        }
        assignmentInstance.title = params.title
        assignmentInstance.validate()
        Schedule scheduleInstance = assignmentInstance.schedule ?: new Schedule()
        scheduleInstance.startDate = getStartDate(params)
        scheduleInstance.endDate = getEndDate(params)
        scheduleInstance.validate()
        if (assignmentInstance.hasErrors() || scheduleInstance.hasErrors()) {
            respond assignmentInstance, model:[scheduleInstance:scheduleInstance], view:'edit'
            return
        }

        assignmentService.saveAssignment(assignmentInstance, scheduleInstance)

        flash.message = message(code: 'assignment.updated.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.title])
        redirect assignmentInstance

    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def delete(Assignment assignmentInstance) {

        if (assignmentInstance == null) {
            notFound()
            return
        }
        def user = springSecurityService.currentUser
        assignmentService.deleteAssignment(assignmentInstance, user, true)

        flash.message = message(code: 'assignment.deleted.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.title])
        redirect action: "index", method: "GET"

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def addSequence(Assignment assignmentInstance) {
        if (assignmentInstance == null) {
            notFound()
            return
        }
        Statement statementInstance = new Statement()
        render(view: "create_sequence", model: [assignmentInstance: assignmentInstance, statementInstance:statementInstance])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    @Transactional
    def saveSequence() {
        Assignment assignmentInstance = Assignment.get(params.assignment_instance_id)
        if (assignmentInstance == null) {
            notFound()
            return
        }
        User owner = springSecurityService.currentUser
        Statement statementInstance = new Statement(title: params.title)
        statementInstance.content = markupSanitizerService.sanitize(params.content)?.cleanString
        statementInstance.owner = owner

        Sequence sequenceInstance = sequenceService.addSequenceToAssignment(assignmentInstance,owner, statementInstance)

        if (sequenceInstance.hasErrors()) {
            respond assignmentInstance, model:[statementInstance: statementInstance], view:'create_sequence'
            return
        }

        def file = request.getFile('myFile')
        if (file && !file.isEmpty()) {
            attachementService.addFileToStatement(file, statementInstance)
        }

        flash.message = message(code: 'sequence.updated.message', args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title])
        redirect action: "show", controller: "assignment", params: [id:assignmentInstance.id]

    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def editSequence(Sequence sequenceInstance) {
        respond sequenceInstance, view: "edit_sequence"
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
        Statement statementInstance = sequenceInstance.statement
        statementInstance.title = params.title
        statementInstance.content = markupSanitizerService.sanitize(params.content)?.cleanString

        sequenceService.updateStatementSequence(sequenceInstance, user)

        if (sequenceInstance.hasErrors()) {
            respond sequenceInstance, view:'edit_sequence'
            return
        }

        def file = request.getFile('myFile')
        if (file && !file.isEmpty()) {
            attachementService.addFileToStatement(file, statementInstance)
        }

        Assignment assignmentInstance = sequenceInstance.assignment
        flash.message = message(code: 'sequence.updated.message', args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title])
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

        flash.message = message(code: 'sequence.deleted.message', args: [message(code: 'sequence.label', default: 'Question'), sequenceInstance.title])
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
        flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignment.label', default: 'Assignment'), params.id])
        redirect action: "index", method: "GET"
    }

    private Date getStartDate(def params) {
        if (params.startDate) {
            return new Date().parse(message(code: 'date.startDate.format'), params.('startDate'))
        }
        null
    }

    private Date getEndDate(def params) {
        if (params.endDate) {
            return new Date().parse(message(code: 'date.endDate.format'), params.('endDate'))
        }
        null
    }
}
