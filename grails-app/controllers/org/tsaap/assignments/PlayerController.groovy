package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.InteractionService
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.directory.User

class PlayerController {

    SpringSecurityService springSecurityService
    AssignmentService assignmentService
    InteractionService interactionService
    SequenceService sequenceService
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
    def ltiLaunch(Assignment assignmentInstance) {
        User user = springSecurityService.currentUser
        if (user.isLearner() && !user.isRegisteredInAssignment(assignmentInstance)) {
            assignmentService.registerUserOnAssignment(user,assignmentInstance)
        } else if (user.isTeacher() && user != assignmentInstance.owner) {
            assignmentService.registerUserOnAssignment(user, assignmentInstance)
        }
        render view: "/assignment/player/assignment/show", model: [assignmentInstance: assignmentInstance,
                                                                   user:user]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def startInteraction(Interaction interactionInstance) {
        User user = springSecurityService.currentUser
        interactionService.startInteraction(interactionInstance, user)
        Sequence sequenceInstance = interactionInstance.sequence
        renderSequenceTemplate(user, sequenceInstance)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def initializeInteractionsAndStartFirst(Sequence sequenceInstance) {
        User user = springSecurityService.currentUser
        List<Interaction> interactions =
            getDynamicsInteractions(sequenceInstance, params)

        sequenceInstance = sequenceService.saveSequence(sequenceInstance, user, sequenceInstance.assignment, sequenceInstance.statement)
        sequenceService.addSequenceInteractions(sequenceInstance, user, interactions)
        interactionService.startInteraction(interactions.get(0), user)

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
    def updateInteractionResponseCount(Interaction interactionInstance) {
        render interactionInstance.interactionResponseCount()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateSequenceDisplay(Sequence sequenceInstance) {
        def user = springSecurityService.currentUser
        renderSequenceTemplate(user,sequenceInstance)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateSecondAttemptCount(Interaction interactionInstance) {
        render interactionInstance.interactionResponseCount(2)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def updateEvaluationCount(Interaction interactionInstance) {
        render interactionInstance.evaluationCount()
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def submitResponse(Interaction interactionInstance) {
        def user = springSecurityService.currentUser
        InteractionResponse response = new InteractionResponse(
                learner: user,
                interaction: interactionInstance,
                confidenceDegree: params.confidenceDegree as Integer,
                attempt: params.attempt as int
        )
        if (params.explanation) {
            response.explanation = markupSanitizerService.sanitize(params.explanation)?.cleanString
        }
        if (interactionInstance.sequence.statement.hasChoices()) {
            List choiceList = getChoiceListFromParams(interactionInstance.sequence.statement, params)
            response.updateChoiceListSpecification(choiceList)
        }
        interactionService.saveInteractionResponse(response)
        renderSequenceTemplate(user, interactionInstance.sequence)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def createOrUpdatePeerGrading() {
        def params = params
        User grader = User.get(params.grader_id as long)
        InteractionResponse response = InteractionResponse.get(params.response_id as long)
        Float grade = params.grade as Float
        PeerGrading peerGrading = interactionService.peerGradingFromUserOnResponse(grader, response, grade)
        render "${peerGrading.hasErrors() ? 'error' : 'success'}"
    }

    private void renderSequenceTemplate(user, Sequence sequenceInstance) {
        def userRole = (user == sequenceInstance.assignment.owner ? 'teacher' : 'learner')
        render template: "/assignment/player/sequence/${userRole}/${sequenceInstance.state}",
                model: [userRole: userRole, sequenceInstance: sequenceInstance, user: user],
                layout: "ajax"
    }


    protected List<Integer> getChoiceListFromParams(Statement statement, def params) {
        List<Integer> choiceList = []
        if (statement.isMultipleChoice()) {
            params.choiceList?.each {
                if (it && it != "null") {
                    choiceList << (it as Integer)
                }
            }
        } else if (params.exclusiveChoice && params.exclusiveChoice != "null") {
                choiceList = [params.exclusiveChoice as Integer]
        }
        choiceList
    }


    private List<Interaction> getDynamicsInteractions (Sequence sequence, def params) {
        boolean studentsProvideExplanation = true
        int responseToEvaluateCount = EvaluationSpecification.MAX_RESPONSE_TO_EVALUATE_COUNT
        if (sequence.executionIsFaceToFace()) {
            studentsProvideExplanation = params.studentsProvideExplanation?.toBoolean()
            responseToEvaluateCount = params.responseToEvaluateCount?.toInteger()
        }
        sequenceService.createInteractionsForSequence(sequence,studentsProvideExplanation,responseToEvaluateCount)
    }



}
