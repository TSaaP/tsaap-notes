/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        if (params?.format && params.format != "html") {
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
        render(view: '/questions/nPhasesLiveSessionStats', model: [stats: [stats], labels: labels, user: user, liveSession: liveSession])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def results() {
        User user = springSecurityService.currentUser
        LiveSession liveSession = LiveSession.get(params.id)
        NPhasesLiveSessionResultList results = resultListService.getNPhasesLiveSessionResultListForLiveSession(user, liveSession)
        Map labels = resultListService.nPhaseSessionResultListLabels()
        if (params?.format && params.format != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=tsaapNotesResults.${params.extension}")
            exportService.export(
                    params.format,
                    response.outputStream,
                    results.resultList,
                    labels.keySet() as List,
                    labels,
                    [:],
                    [title: results.title]
            )
        }
        render(view: '/questions/nPhasesLiveSessionResults', model: [results: results, labels: labels, user: user, liveSession: liveSession])
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