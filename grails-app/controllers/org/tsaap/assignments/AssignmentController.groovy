package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.attachement.AttachementService
import org.tsaap.directory.User

@Transactional(readOnly = true)
class AssignmentController {

    SpringSecurityService springSecurityService
    AssignmentService assignmentService
    AttachementService attachementService

    static allowedMethods = [save: "POST", update: "PUT"]

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'lastUpdated'
        params.order = params.order ?: 'desc'
        User owner = springSecurityService.currentUser
        respond assignmentService.findAllAssignmentsForOwner(owner, params),
                model:[assignmentInstanceCount: assignmentService.countAllAssignmentsForOwner(owner)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Assignment assignmentInstance) {
        respond assignmentInstance
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        [assignmentInstance:new Assignment()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    @Transactional
    def save() {
        Assignment assignmentInstance = new Assignment(params)
        assignmentInstance.owner = springSecurityService.currentUser
        assignmentInstance.validate()
        if (assignmentInstance.hasErrors()) {
            respond assignmentInstance, view:'create'
            return
        }

        assignmentService.saveAssignment(assignmentInstance)

        flash.message = message(code: 'assignment.created.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.title])
        redirect assignmentInstance

    }



    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(Assignment assignmentInstance) {
        [assignmentInstance: assignmentInstance]
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
        if (assignmentInstance.hasErrors()) {
            respond assignmentInstance, view:'edit'
            return
        }

        assignmentService.saveAssignment(assignmentInstance)

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

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def duplicate(Assignment assignmentInstance) {
        if (assignmentInstance == null) {
            notFound()
            return
        }
        def user = springSecurityService.currentUser
        Assignment duplicatedAssignment = assignmentService.duplicate(assignmentInstance, user)

        flash.message = message(code: 'assignment.duplicate.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.title])
        redirect action: "edit", method: "GET", id: duplicatedAssignment.id
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def addSequence(Assignment assignmentInstance) {
        if (assignmentInstance == null) {
            notFound()
            return
        }
        Statement statementInstance = new Statement()
        render(view: "sequence/create_sequence", model: [assignmentInstance: assignmentInstance, statementInstance:statementInstance])
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
