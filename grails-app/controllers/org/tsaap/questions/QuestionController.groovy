package org.tsaap.questions

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import grails.web.RequestParameter
import org.tsaap.notes.Note


class QuestionController {

    SpringSecurityService springSecurityService
    LiveSessionService liveSessionService

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
        def sessionStatus = liveSession ? liveSession.status : LiveSessionStatus.NotStarted.name()
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/${sessionStatus}/detail", model: [note: note, liveSession: liveSession, user: currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def submitResponse(AnswersWrapperCommand answersWrapper) {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(answersWrapper.noteId)
        def liveSession = LiveSession.get(answersWrapper.liveSessId)
        println params
        StringBuilder answersAsString = new StringBuilder("[[")
        answersWrapper.answers.each { answer ->
            if (answer) {
                def answerAsString = "\"${answer}\","
                answersAsString.append(answerAsString)
            }
        }
        if (answersAsString.length() > 2) {
            answersAsString.deleteCharAt(answersAsString.length() - 1)
        }
        answersAsString.append("]]")

        def response = liveSessionService.createResponseForLiveSessionAndUser(liveSession, currentUser, answersAsString.toString())
        if (response.hasErrors()) {
            log.error(response.errors.allErrors.toString())
        }
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/${liveSession.status}/detail", model: [note: note, liveSession: liveSession, user: currentUser])
    }


}

class AnswersWrapperCommand {
    Long noteId
    Long liveSessId
    List<String> answers
}