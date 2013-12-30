package org.tsaap.questions

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
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
        render(template: '/questions/author/Started/detail', model: [note: note, liveSession: liveSession,user:currentUser])
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
        render(template: "/questions/author/${liveSession.status}/detail", model: [note: note, liveSession: liveSession,user:currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def refresh() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def liveSession = note.liveSession
        def sessionStatus = liveSession ? liveSession.status : LiveSessionStatus.NotStarted.name()
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/${sessionStatus}/detail", model: [note: note, liveSession: liveSession,user:currentUser])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def submitResponse() {
        def currentUser = springSecurityService.currentUser
        def note = Note.get(params.noteId)
        def liveSession = LiveSession.get(params.liveSessId)
        def answer = params.answer
        def answerAsString = answer ? "[[\"${answer}\"]]" : "[[]]"
        def response = liveSessionService.createResponseForLiveSessionAndUser(liveSession,currentUser,answerAsString)
        if (response.hasErrors()) {
            log.error(response.errors.allErrors.toString())
        }
        def userType = currentUser == note.author ? 'author' : 'user'
        render(template: "/questions/${userType}/${liveSession.status}/detail", model: [note: note, liveSession: liveSession,user:currentUser])
    }


}
