package org.tsaap.questions

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gcontracts.PreconditionViolation
import org.tsaap.directory.User
import org.tsaap.notes.Note
import org.tsaap.notes.NoteKind
import org.tsaap.questions.impl.DefaultQuestion
import org.tsaap.questions.impl.gift.GiftQuestionService
import spock.lang.Shared
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LiveSessionService)
@Mock([LiveSession, User, Note])
class LiveSessionServiceSpec extends Specification {
    @Shared
    User user = new User(firstName: "Paul", lastName: "D", username: "learner_paul", password: "password", email: "paul@nomail.com")
    @Shared
    User user2 = new User(firstName: "Paul", lastName: "D", username: "learner_paul2", password: "password", email: "paul2@nomail.com")

    @Shared
    Note note = new Note(content: "::it\'s a question:: what ? {=this ~ not this}", author: user, kind: NoteKind.QUESTION.ordinal())
    @Shared
    Note note2 = new Note(content: "it's not a question", author: user)

    void "test the creation of a live session for a given note"() {
        given: "a note"
        SpringSecurityService springSecurityService = Mock(SpringSecurityService)
        springSecurityService.encodePassword(user.password) >> user.password
        GiftQuestionService giftQuestionService = Mock(GiftQuestionService)
        user.springSecurityService = springSecurityService

        when: "the note is a question"
        giftQuestionService.getQuestionFromGiftText(note.content) >> new DefaultQuestion()
        note.giftQuestionService = giftQuestionService

        and: "the author of the note  create a live session"
        LiveSession liveSession = service.createLiveSessionForNote(note.author, note)

        then: "the live session is created in status NotStarted"
        liveSession.dateCreated != null
        liveSession.status == LiveSessionStatus.NotStarted.name()

        when: "a note that is not a question"
        note2.giftQuestionService = giftQuestionService

        and: "trying to create a live session"
        service.createLiveSessionForNote(note2.author, note2)

        then: "an precondition violation occurs"
        thrown(PreconditionViolation)

        when: "the user trying to create the live session is not the note author"
        service.createLiveSessionForNote(user2, note)

        then: "an precondition violation occurs"
        thrown(PreconditionViolation)
    }


}
