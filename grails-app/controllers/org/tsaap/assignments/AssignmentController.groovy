package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AssignmentController {

    SpringSecurityService springSecurityService
    AssignmentService assignmentService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

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

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.id])
                redirect assignmentInstance
            }
            '*' { respond assignmentInstance, [status: CREATED] }
        }
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

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.id])
                redirect assignmentInstance
            }
            '*'{ respond assignmentInstance, [status: OK] }
        }
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

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'assignment.label', default: 'Assignment'), assignmentInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }


    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'assignment.label', default: 'Assignment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
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
