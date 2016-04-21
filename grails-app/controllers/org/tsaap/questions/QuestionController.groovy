package org.tsaap.questions

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.tsaap.directory.User
import org.tsaap.lti.LmsContextHelper
import org.tsaap.lti.LmsGradeService
import org.tsaap.lti.tp.ResourceLink
import org.tsaap.lti.tp.ToolConsumer
import org.tsaap.lti.tp.dataconnector.JDBC
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService

import javax.sql.DataSource


class QuestionController {

    SpringSecurityService springSecurityService
    LiveSessionService liveSessionService
    NoteService noteService
    StatisticsService statisticsService
    ResultListService resultListService
    LmsGradeService lmsGradeService
    LmsContextHelper lmsContextHelper
    DataSource dataSource

    // for export stats
    def exportService
    def grailsApplication

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def startLiveSession() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def liveSession
        try {
            liveSession = LiveSession.get(params.liveSessId)
            liveSession.start()
        } catch (Exception e) {
            liveSession = liveSessionService.createAndStartLiveSessionForNote(currentUser, note)
        }
        render(template: '/questions/author/Started/detail', model: [note: note, liveSession: liveSession, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def stopLiveSession() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def liveSession = LiveSession.get(params.liveSessId)
        liveSession.stop()
        if(liveSession.note.context) {
            if(liveSession.note.context.source != null) {
                lmsContextHelper = new LmsContextHelper()
                Sql sql = new Sql(dataSource)
                String consumerKey
                String courseId
                def req = lmsContextHelper.selectConsumerKeyAndCourseId(sql, liveSession.note.context.id)
                consumerKey = req.get(0)
                courseId = req.get(1)
                JDBC jdbc = new JDBC("", dataSource.connection)
                ToolConsumer toolConsumer = new ToolConsumer(consumerKey, jdbc, false)
                ResourceLink resourceLink = new ResourceLink(toolConsumer, courseId)
                def grades = lmsGradeService.getUsersGradeForContext(sql, liveSession.note.context.id)
                grades.each { ltiUserId, grade ->
                    org.tsaap.lti.tp.User user = new org.tsaap.lti.tp.User(resourceLink, ltiUserId)
                    lmsGradeService.sendUserGradeToLms(resourceLink, user, grade)
                }
            }
        }
        if (liveSession.hasErrors()) {
            log.error(liveSession.errors.allErrors.toString())
        }
        render(template: "/questions/author/${liveSession.status}/detail", model: [note: note, liveSession: liveSession, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def refresh() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def liveSession = note.liveSession
        def phase = liveSession ? liveSession.findCurrentPhase() : null
        def userType = currentUser == note.author ? 'author' : 'user'
        if (!phase || liveSession.isStopped()) {
            def sessionStatus = liveSession ? liveSession.status : LiveSessionStatus.NotStarted.name()
            render(template: "/questions/${userType}/${sessionStatus}/detail", model: [note: note, liveSession: liveSession, user: currentUser])
        } else {
            render(template: "/questions/${userType}/Phase${phase.rank}/${phase.status}/detail", model: [note: note, sessionPhase: phase, user: currentUser])
        }
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def startNPhasesLiveSession() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def liveSession
        try {
            liveSession = LiveSession.get(params.liveSessId)
        } catch (Exception e) {
            liveSession = liveSessionService.createLiveSessionForNote(currentUser, note)
        }
        SessionPhase sessionPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(currentUser, liveSession)
        render(template: "/questions/author/Phase1/${sessionPhase.status}/detail", model: [note: note, sessionPhase: sessionPhase, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def startPhase() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        Integer rank = params.phaseRank as Integer
        def liveSession = LiveSession.get(params.liveSessId)
        def sessionPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(currentUser, liveSession, rank)
        render(template: "/questions/author/Phase${sessionPhase.rank}/${sessionPhase.status}/detail", model: [note: note, sessionPhase: sessionPhase, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def stopPhase() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def phase = SessionPhase.get(params.phaseId)
        def liveSession = phase.liveSession
        if (phase.stopLiveSessionWhenIsStopped()) {
            liveSessionService.closeNPhaseSubmitLiveSession(liveSession)
            if (liveSession.note.context) {
                if (liveSession.note.context.source != null) {
                    lmsContextHelper = new LmsContextHelper()
                    Sql sql = new Sql(dataSource)
                    String consumerKey
                    String courseId
                    def req = lmsContextHelper.selectConsumerKeyAndCourseId(sql, liveSession.note.contextId)
                    consumerKey = req.get(0)
                    courseId = req.get(1)
                    JDBC jdbc = new JDBC("", dataSource.connection)
                    ToolConsumer toolConsumer = new ToolConsumer(consumerKey, jdbc, false)
                    ResourceLink resourceLink = new ResourceLink(toolConsumer, courseId)
                    def grades = lmsGradeService.getUsersGradeForContext(sql, liveSession.note.contextId)
                    grades.each { ltiUserId, grade ->
                        org.tsaap.lti.tp.User user = new org.tsaap.lti.tp.User(resourceLink, ltiUserId)
                        lmsGradeService.sendUserGradeToLms(resourceLink, user, grade)
                    }
                }
            }
            render(template: "/questions/author/${liveSession.status}/detail", model: [note: note, liveSession: liveSession, user: currentUser])
        } else {
            phase.stop()
            render(template: "/questions/author/Phase${phase.rank}/${phase.status}/detail", model: [note: note, sessionPhase: phase, user: currentUser])
        }
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def refreshPhase() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def phase = SessionPhase.get(params.phaseId)
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/Phase${phase.rank}/${phase.status}/detail", model: [note: note, sessionPhase: phase, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def submitResponse(AnswersWrapperCommand answersWrapper) {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(answersWrapper.noteId)
        def liveSession = LiveSession.get(answersWrapper.liveSessId)
        String answersAsString = buildAnswerAsStringFromAnswers(answersWrapper.answers)
        if (liveSession.isStarted()) {
            def response = liveSessionService.createResponseForLiveSessionAndUser(liveSession, currentUser, answersAsString)
            if (response.hasErrors()) {
                log.error(response.errors.allErrors.toString())
            }
        }
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/${liveSession.status}/detail", model: [note: note, liveSession: liveSession, user: currentUser])
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def submitResponseInAPhase(AnswersWrapperPhaseCommand answersWrapper) {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(answersWrapper.noteId)
        def phase = SessionPhase.get(answersWrapper.phaseId)
        String answersAsString = buildAnswerAsStringFromAnswers(answersWrapper.answers)
        if (phase.isStarted()) {
            def response = liveSessionService.createResponseForSessionPhaseAndUser(
                    phase,
                    currentUser,
                    answersAsString,
                    answersWrapper.explanation,
                    answersWrapper.confidenceDegree)
            if (response.hasErrors()) {
                log.error(response.errors.allErrors.toString())
            }
        }
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/Phase${phase.rank}/${phase.status}/detail", model: [note: note, sessionPhase: phase, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def evaluateResponses(EvaluateResponsesCommand evaluateResponsesCommand) {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(evaluateResponsesCommand.noteId)
        def phase = SessionPhase.get(evaluateResponsesCommand.phaseId)
        def userType = currentUser == note.author ? 'author' : 'user'
        if (phase.isStarted()) {
            evaluateResponsesCommand.explanationIds.eachWithIndex { explanationId, i ->
                noteService.gradeNotebyUser(Note.get(explanationId), currentUser, evaluateResponsesCommand.grades[i])
            }
            render(template: "/questions/${userType}/Phase${phase.rank}/${phase.status}/detail", model: [note: note, sessionPhase: phase, user: currentUser])
        } else {
            render(template: "/questions/${userType}/${phase.liveSession.status}/detail", model: [note: note, liveSession: phase.liveSession, user: currentUser])
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def statistics() {
        User user = springSecurityService.currentUser
        LiveSession liveSession = LiveSession.get(params.id)
        NPhasesLiveSessionStatistics stats = statisticsService.getNPhasesLiveSessionStatisticsForLiveSession(liveSession)
        Map labels = statisticsService.nPhaseSessionStatsLabels()
        if(params?.format && params.format != "html"){
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=tsaapNotesStats.${params.extension}")
            exportService.export(
                    params.format,
                    response.outputStream,
                    [stats],
                    labels.keySet() as List,
                    labels,
                    [:],
                    [:]
            )
        }
        render(view: '/questions/nPhasesLiveSessionStats',model: [stats:[stats], labels: labels,user:user, liveSession: liveSession])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def results() {
        User user = springSecurityService.currentUser
        LiveSession liveSession = LiveSession.get(params.id)
        NPhasesLiveSessionResultList results = resultListService.getNPhasesLiveSessionResultListForLiveSession(user,liveSession)
        Map labels = resultListService.nPhaseSessionResultListLabels()
        if(params?.format && params.format != "html"){
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=tsaapNotesResults.${params.extension}")
            exportService.export(
                    params.format,
                    response.outputStream,
                    results.resultList,
                    labels.keySet() as List,
                    labels,
                    [:],
                    [title:results.title]
            )
        }
        render(view: '/questions/nPhasesLiveSessionResults',model: [results:results, labels: labels,user:user, liveSession: liveSession])
    }

    private String buildAnswerAsStringFromAnswers(List<String> answers) {
        StringBuilder answersAsString = new StringBuilder("[[")
        answers.each { answer ->
            if (answer) {
                def answerAsString = "\"${answer}\","
                answersAsString.append(answerAsString)
            }
        }
        if (answersAsString.length() > 2) {
            answersAsString.deleteCharAt(answersAsString.length() - 1)
        }
        answersAsString.append("]]")
        answersAsString.toString()
    }

}

class AnswersWrapperCommand {
    Long noteId
    Long liveSessId
    List<String> answers
}

class AnswersWrapperPhaseCommand {
    Long noteId
    Long phaseId
    List<String> answers
    String explanation
    Integer confidenceDegree
}

class EvaluateResponsesCommand {
    Long noteId
    Long phaseId
    List<Long> explanationIds
    List<Double> grades
}