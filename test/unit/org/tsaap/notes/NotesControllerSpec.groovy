package org.tsaap.notes

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.tsaap.attachement.Attachement
import org.tsaap.directory.User
import org.tsaap.questions.LiveSession
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(NotesController)
@Mock([Note, User, Bookmark, NoteService, Attachement, NoteTag, NoteMention, Score, LiveSession])
class NotesControllerSpec extends Specification {

    User user
    Note note
    SpringSecurityService springSecurityService = Mock(SpringSecurityService)

    def setup() {
        controller.springSecurityService = springSecurityService
        user = new User(firstName: "moghite", lastName: "kacimi", username: "akac", email: "akac@mail.com", password: "password")
        user.springSecurityService = springSecurityService
        springSecurityService.encodePassword(user.password) >> user.password
        user.save()
        springSecurityService.currentUser >> user
        note = new Note(author: user, content: "standard note", kind: NoteKind.STANDARD.ordinal())
        note.save(flush: true)

    }

    def cleanup() {
        if (user)
            user.delete()
        if (note)
            note.delete()
    }

    void "Bookmark and unBookmaek notes"() {

        when: "Bookmark note by user"
        params.noteId = note.id
        controller.bookmarkNote()

        then:""
        Bookmark.count() == 1
        response.redirectedUrl == '/notes/index?max=5'

        when:"Bookmark the same note by user"
        response.reset()
        params.noteId = note.id
        controller.unbookmarkNote()

        then:""
        Bookmark.count() == 0
    }

    void "test deleteNote action"() {

        when:"try to remove this note"
        params.noteId = note.id
        controller.deleteNote()

        then:"the instance is deleted"
        Note.count == 0
        response.redirectedUrl == '/notes/index?max=5'
    }

    void "mark note Liked"() {

        when:"Like note"
        params.noteId = note.id
        controller.markAsLikedNote()

        then:"the note is mark as liked note"
        Score.count() == 1
        response.redirectedUrl == '/notes/index?max=5'
    }

}
