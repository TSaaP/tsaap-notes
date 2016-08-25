package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

import java.text.DateFormat

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
        respond Assignment.list(params), model:[assignmentInstanceCount: Schedule.count()]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Assignment assignmentInstance) {
        respond assignmentInstance
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        [assignmentInstance:new Assignment(params), scheduleInstance: new Schedule(params)]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    @Transactional
    def save() {
        Assignment assignmentInstance = new Assignment(params)
        assignmentInstance.owner = springSecurityService.currentUser
        assignmentInstance.validate()
        if (assignmentInstance.hasErrors()) {
            respond assignmentInstance.errors, view:'create'
            return
        }
        Date startDate = null
        Date endDate = null
        if (params.startDate) {
            startDate = new Date().parse(message(code:'date.startDate.format'),params.('startDate'))
        }
        if (params.endDate) {
            endDate = new Date().parse(message(code:'date.endDate.format'),params.('endDate'))
        }
        Schedule scheduleInstance = new Schedule(startDate:startDate, endDate:endDate)
        scheduleInstance.validate()
        if (scheduleInstance.hasErrors()) {
            respond scheduleInstance.errors, view:'create'
            return
        }

        assignmentService.saveAssignment(assignmentInstance, scheduleInstance)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'assignment.label', default: 'Assignment'), scheduleInstance.id])
                redirect assignmentInstance
            }
            '*' { respond assignmentInstance, [status: CREATED] }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(Assignment assignmentInstance) {
        respond assignmentInstance
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    @Transactional
    def update(Assignment assignmentInstance, Schedule scheduleInstance) {
        if (assignmentInstance == null) {
            notFound()
            return
        }

        if (assignmentInstance.hasErrors()) {
            respond assignmentInstance.errors, view:'edit'
            return
        }

        if (scheduleInstance.hasErrors()) {
            respond scheduleInstance.errors, view:'edit'
            return
        }

        scheduleInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'assignment.label', default: 'Assignment'), scheduleInstance.id])
                redirect assignmentInstance
            }
            '*'{ respond assignmentInstance, [status: OK] }
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
}
