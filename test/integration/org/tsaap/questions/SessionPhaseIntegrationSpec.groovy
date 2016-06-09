package org.tsaap.questions

import org.tsaap.BootstrapTestService
import org.tsaap.directory.User
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService
import spock.lang.Specification

/**
 *
 */
class SessionPhaseIntegrationSpec extends Specification {

    NoteService noteService
    BootstrapTestService bootstrapTestService
    LiveSessionService liveSessionService
    User user
    Note note
    User user1
    User user2
    User user3

    def initiliseUsers() {

        user1 = User.findByUsername("user1")
        if (!user1) {
            user1 = new User(firstName: "user1", lastName: "u1", username: "user1", password: "password", email: "user1@nomail.com").save()
        }
        user2 = User.findByUsername("user2")
        if (!user2) {
            user2 = new User(firstName: "user1", lastName: "u2", username: "user2", password: "password", email: "user2@nomail.com").save()
        }
        user3 = User.findByUsername("user3")
        if (!user3) {
            user3 = new User(firstName: "user3", lastName: "u3", username: "user3", password: "password", email: "user3@nomail.com").save()
        }
    }

    def setup() {

        bootstrapTestService.initializeTests()
        initiliseUsers()
        user = bootstrapTestService.learnerPaul
        note = noteService.addQuestion(user, "::a question:: What ? {=this ~that}")
    }

    def cleanup() {
        if (user1.username == "user1")
            user1.delete()
        if (user2.username == "user2")
            user2.delete()
        if (user3.username == "user3")
            user3.delete()
    }

    void "test something"() {
    }


    void "test the number of feedback related to the number of submitted evaluation for phases"() {

        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        when: "trying to create a response for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase, user1, '[["0"]]', "an explanation1", 5)

        then: "count number of response of this sessionPhase"
        response.sessionPhase == firstPhase
        firstPhase.responseCount() == 1

        when: "the first phase is stopped"
        firstPhase.stop()

        and: "trying again"
        SessionPhase secondPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 2)

        and: "trying to create a response for that learner for this session"
        response = liveSessionService.createResponseForSessionPhaseAndUser(secondPhase, user1, '[["0"]]', "an explanation1", 5)

        then: "count number of response of this sessionPhase"
        secondPhase.responseCount() == 1

        when: "the second phase is stopped"
        secondPhase.stop()


        and: "trying again"
        SessionPhase thirdPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 3)

        then: "count number of response of this sessionPhase"

        thirdPhase.responseCount() == 0

        when: "trying grade to an explanation"

        noteService.gradeNotebyUser(response.explanation, user1, 2d)

        then: "count number of response of this sessionPhase"

        thirdPhase.responseCount() == 1

        when: "trying to grade an explanation"

        noteService.gradeNotebyUser(response.explanation, user2, 2d)

        then: "count number of response of this sessionPhase"

        thirdPhase.responseCount() == 2

        when: "trying to grade an explanation"

        noteService.gradeNotebyUser(response.explanation, user3, 2d)

        then: "count number of response of this sessionPhase"

        thirdPhase.responseCount() == 3


    }


}
