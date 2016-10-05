package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.assignments.interactions.InteractionService
import org.tsaap.assignments.interactions.InteractionSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User

class PlayerController {

    SpringSecurityService springSecurityService
    AssignmentService assignmentService
    InteractionService interactionService
    MarkupSanitizerService markupSanitizerService

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
        redirect(action: "index", controller: "player")
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Assignment assignmentInstance) {
        render view: "/assignment/player/assignment/show", model: [assignmentInstance: assignmentInstance,
                                                                   user:springSecurityService.currentUser]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def startInteraction(Interaction interactionInstance) {
        User user = springSecurityService.currentUser
        interactionService.startInteraction(interactionInstance, user)
        Sequence sequenceInstance = interactionInstance.sequence
        renderSequenceTemplate(user, sequenceInstance)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def stopInteraction(Interaction interactionInstance) {
        User user = springSecurityService.currentUser
        interactionService.stopInteraction(interactionInstance, user)
        Sequence sequenceInstance = interactionInstance.sequence
        renderSequenceTemplate(user, sequenceInstance)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateRegisteredUserCount(Assignment assignmentInstance) {
        render assignmentInstance.registeredUserCount()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateChoiceInteractionResponseCount(Interaction interactionInstance) {
        render interactionInstance.choiceInteractionResponseCount()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateSequenceDisplay(Sequence sequenceInstance) {
        def user = springSecurityService.currentUser
        renderSequenceTemplate(user,sequenceInstance)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updatePeerEvaluationCount(Interaction interactionInstance) {
        render interactionInstance.peerEvaluationCount()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def submitResponse(Interaction interactionInstance) {
        ResponseSubmissionSpecification spec = interactionInstance.interactionSpecification
        List choiceList = getChoiceListFromParams(spec, params)
        def user = springSecurityService.currentUser
        createAndSaveChoiceInteractionResponse(user, interactionInstance, choiceList, params)
        renderSequenceTemplate(user, interactionInstance.sequence)
    }

    private void renderSequenceTemplate(user, Sequence sequenceInstance) {
        def userRole = (user == sequenceInstance.assignment.owner ? 'teacher' : 'learner')
        render template: "/assignment/player/sequence/show",
                model: [userRole: userRole, sequenceInstance: sequenceInstance, user: user]
    }

    private ChoiceInteractionResponse createAndSaveChoiceInteractionResponse(user, Interaction interactionInstance, List<Integer> choiceList, params) {
        ChoiceInteractionResponse response = new ChoiceInteractionResponse(
                learner: user,
                interaction: interactionInstance,
                confidenceDegree: params.confidenceDegree as Integer,
                attempt: params.attempt as int
        )
        if (params.explanation) {
            response.explanation = markupSanitizerService.sanitize(params.explanation)?.cleanString
        }
        response.updateChoiceListSpecification(choiceList)
        interactionService.saveChoiceInteractionResponse(response)
        response
    }

    private List getChoiceListFromParams(ResponseSubmissionSpecification spec, def params) {
        List<Integer> choiceList
        if (spec.isMultipleChoice()) {
            choiceList = params.choiceList?.collect { it as Integer }
        } else {
            choiceList = [params.exclusiveChoice as Integer]
        }
        choiceList
    }

}
