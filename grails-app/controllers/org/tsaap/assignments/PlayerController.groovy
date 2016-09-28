package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.assignments.interactions.InteractionService
import org.tsaap.directory.User

class PlayerController {

    SpringSecurityService springSecurityService
    AssignmentService assignmentService
    InteractionService interactionService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        User user = springSecurityService.currentUser
        def learnerAssignments =  assignmentService.findAllAssignmentsForLearner(user, params)
        def count = assignmentService.countAllAssignmentsForLearner(user)
        render view: "/assignment/player/index", model: [learnerAssignmentList: learnerAssignments,
                                                         learnerAssignmentListCount:count]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def register() {
        String globalId = params.globalId
        Assignment assignmentInstance = assignmentService.findAssignmentByGlobalId(globalId)
        User user = springSecurityService.currentUser
        assignmentService.registerUserOnAssignment(user, assignmentInstance)
        render view: "/assignment/player/assignment/show", model: [assignmentInstance: assignmentInstance]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Assignment assignmentInstance) {
        render view: "/assignment/player/assignment/show", model: [assignmentInstance: assignmentInstance,
                                                                   user:springSecurityService.currentUser]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def startInteraction(Interaction interactionInstance) {
        interactionService.startInteraction(interactionInstance, springSecurityService.currentUser)
        Assignment assignment = interactionInstance.sequence.assignment
        render view: "/assignment/player/assignment/show", model: [assignmentInstance: assignment,
                                                                   user:springSecurityService.currentUser]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def stopInteraction(Interaction interactionInstance) {
        interactionService.stopInteraction(interactionInstance, springSecurityService.currentUser)
        Assignment assignment = interactionInstance.sequence.assignment
        render view: "/assignment/player/assignment/show", model: [assignmentInstance: assignment,
                                                                   user:springSecurityService.currentUser]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateRegisteredUserCount(Assignment assignmentInstance) {
        render assignmentInstance.registeredUserCount()
    }

}
