package org.tsaap.assignments

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.grails.plugins.sanitizer.MarkupSanitizerService
import org.tsaap.assignments.interactions.InteractionService
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.directory.User
import org.tsaap.skin.SkinUtil

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
    def learnerAssignments = assignmentService.findAllAssignmentsForLearner(user, params)
    def count = assignmentService.countAllAssignmentsForLearner(user)
    render view: "/assignment/player/" + SkinUtil.getView(params, session, 'index'),
        model: [
            learnerAssignmentList     : learnerAssignments,
            learnerAssignmentListCount: count
        ]
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def register() {
    String globalId = params.globalId
    Assignment assignmentInstance = assignmentService.findAssignmentByGlobalId(globalId)
    User user = springSecurityService.currentUser
    assignmentService.registerUserOnAssignment(user, assignmentInstance)
    redirect(action: "playFirstSequence", controller: "player", id:"${assignmentInstance.id}")
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def show(Assignment assignmentInstance) {
    render view: "/assignment/player/assignment/" + SkinUtil.getView(params, session, 'show'),
        model: [
            assignmentInstance: assignmentInstance,
            user              : springSecurityService.currentUser
        ]
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def playFirstSequence(Assignment assignmentInstance) {
    if (!assignmentInstance) {
      response.status = 404;
      return
    }

    internalPlaySequence(
        assignmentInstance,
        assignmentInstance.sequences ? assignmentInstance.sequences.first() : null
    )
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def playSequence(Sequence sequenceInstance) {
    if (!sequenceInstance) {
      response.status = 404;
      return
    }


    internalPlaySequence(sequenceInstance.assignment, sequenceInstance)
  }

  private internalPlaySequence(Assignment assignment, Sequence sequence) {
    render view: "/assignment/player/" + SkinUtil.getView(params, session, 'playSequence'),
        model: [
            assignmentInstance: assignment,
            sequenceInstance  : sequence,
            user              : springSecurityService.currentUser
        ]
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def ltiLaunch(Assignment assignmentInstance) {
    User user = springSecurityService.currentUser
    if (user.isLearner() && !user.isRegisteredInAssignment(assignmentInstance)) {
      assignmentService.registerUserOnAssignment(user, assignmentInstance)
    } else if (user.isTeacher() && user != assignmentInstance.owner) {
      assignmentService.registerUserOnAssignment(user, assignmentInstance)
    }

    internalPlaySequence(assignmentInstance, null)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def startInteraction(Interaction interactionInstance) {
    User user = springSecurityService.currentUser
    interactionService.startInteraction(interactionInstance, user)
    Sequence sequenceInstance = interactionInstance.sequence

    if (params.reloadPage) {
      chain(action: 'playSequence', id: sequenceInstance.id)
    } else {
      renderSequenceTemplate(user, sequenceInstance)
    }
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def startNextInteraction(Interaction interactionInstance) {
    User user = springSecurityService.currentUser
    interactionService.startNextInteraction(interactionInstance, user)
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
    if (sequenceInstance.executionIsFaceToFace()) {
      interactionService.startInteraction(interactions.get(0), user)
    } else {
      sequenceService.startSequenceInBlendedOrDistanceContext(sequenceInstance, user)
    }
    interactionService.buildInteractionResponsesFromTeacherExplanationsForASequence(user, sequenceInstance)

    if (params.reloadPage) {
      chain(action: 'playSequence', id: sequenceInstance.id)
    } else {
      renderSequenceTemplate(user, sequenceInstance)
    }

  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def stopInteraction(Interaction interactionInstance) {
    User user = springSecurityService.currentUser
    interactionService.stopInteraction(interactionInstance, user)
    Sequence sequenceInstance = interactionInstance.sequence

    if (params.reloadPage) {
      chain(action: 'playSequence', id: sequenceInstance.id)
    } else {
      renderSequenceTemplate(user, sequenceInstance)
    }
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def stopSequence(Sequence sequenceInstance) {
    User user = springSecurityService.currentUser
    sequenceService.stopSequence(sequenceInstance, user)

    if (params.reloadPage) {
      chain(action: 'playSequence', id: sequenceInstance.id)
    } else {
      renderSequenceTemplate(user, sequenceInstance)
    }
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def reopenSequence(Sequence sequenceInstance) {
    User user = springSecurityService.currentUser
    sequenceService.reopenSequence(sequenceInstance, user)
    renderSequenceTemplate(user, sequenceInstance)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def publishResultsForSequence(Sequence sequenceInstance) {
    User user = springSecurityService.currentUser
    sequenceService.publishResultsForSequence(sequenceInstance, user)
    renderSequenceTemplate(user, sequenceInstance)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def unpublishResultsForSequence(Sequence sequenceInstance) {
    User user = springSecurityService.currentUser
    sequenceService.unpublishResultsForSequence(sequenceInstance, user)
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

    if (sequenceInstance.executionIsBlendedOrDistance()) {
      if (sequenceInstance.userHasCompletedPhase2(user)) {
        sequenceInstance.updateActiveInteractionForLearner(user, 2)
      }
    }

    renderSequenceTemplate(user, sequenceInstance)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def updateResultsAndSequenceDisplay(Sequence sequenceInstance) {
    def user = springSecurityService.currentUser
    try {
      sequenceService.updateAllResults(sequenceInstance, user)
    } catch (ConditionViolationException cve) {
      log.error(cve.message)
    }
    renderSequenceTemplate(user, sequenceInstance)
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
    User user = springSecurityService.currentUser
    processSubmittedResponse(user, interactionInstance, 1, params)
    def sequence = interactionInstance.sequence
    if (sequence.executionIsBlendedOrDistance()) {
      sequence.updateActiveInteractionForLearner(user, 1)
    }
    renderSequenceTemplate(user, sequence)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def submitGradesAndSecondAttempt(Interaction interactionInstance) {
    User user = springSecurityService.currentUser
    Sequence sequence = interactionInstance.sequence
    processSubmittedGrades(user, interactionInstance, params)
    if (sequence.allowsSecondAttemptInLongProcess()) {
      processSubmittedResponse(user, sequence.responseSubmissionInteraction, 2, params)
    }
    if (sequence.executionIsBlendedOrDistance()) {
      sequence.updateActiveInteractionForLearner(user, 2)
    }
    renderSequenceTemplate(user, sequence)
  }

  private def processSubmittedResponse(User user, Interaction interactionInstance, int attempt, def params) {
    InteractionResponse response = new InteractionResponse(
        learner: user,
        interaction: interactionInstance,
        confidenceDegree: params.confidenceDegree as Integer,
        attempt: attempt
    )
    if (params.explanation) {
      response.explanation = markupSanitizerService.sanitize(params.explanation)?.cleanString
    }
    if (interactionInstance.sequence.statement.hasChoices()) {
      List choiceList = getChoiceListFromParams(interactionInstance.sequence.statement, params)
      response.updateChoiceListSpecification(choiceList)
    }
    interactionService.saveInteractionResponse(response)
  }


  private def processSubmittedGrades(User user, Interaction interactionInstance, def params) {
    params.each {
      if (it.key.startsWith("grade_")) {
        InteractionResponse response = InteractionResponse.get(it.key.split("_")[1] as Long)
        def gradeAsString = it.value
        if (gradeAsString != "null") {
          Float grade = gradeAsString as Float
          interactionService.peerGradingFromUserOnResponse(user, response, grade)
        }
      }
    }
  }


  private void renderSequenceTemplate(user, Sequence sequenceInstance) {
    def userRole = (user == sequenceInstance.assignment.owner ? 'teacher' : 'learner')
    render template: "/assignment/player/sequence/${userRole}/${SkinUtil.getView(params, session, sequenceInstance.state)}",
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


  private List<Interaction> getDynamicsInteractions(Sequence sequence, def params) {
    boolean studentsProvideExplanation = true
    int responseToEvaluateCount = params.responseToEvaluateCount?.toInteger() ?: 0
    if (sequence.executionIsFaceToFace()) {
      studentsProvideExplanation = params.studentsProvideExplanation?.toBoolean()
    }
    sequenceService.createInteractionsForSequence(sequence, studentsProvideExplanation, responseToEvaluateCount)
  }


}
