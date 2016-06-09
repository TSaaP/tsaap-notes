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


package org.tsaap.notes

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gcontracts.PreconditionViolation
import org.tsaap.directory.User
import org.tsaap.questions.impl.gift.GiftQuestionService
import spock.lang.Specification

/**
 * Created by franck on 28/10/2014.
 */
@TestFor(ContextService)
@Mock(Note)
class NoteServiceSpec extends Specification {

    NoteService noteService
    User user
    Context targetContext
    Note note

    def setup() {
        noteService = new NoteService()

        user = Mock(User)
        note = Mock(Note) {
            getAuthor() >> user
            getContent() >> "a content"
            getFragmentTag() >> Mock(Tag)
            getNoteKind() >> NoteKind.STANDARD
            getKind() >> NoteKind.STANDARD.ordinal()
        }
        targetContext = Mock(Context) {
            isOpen() >> true
        }
        noteService.giftQuestionService = Mock(GiftQuestionService) {
            getQuestionFromGiftText(note.content) >> null
        }
    }

    def "duplication of a note is only possible for the owner of the note"() {
        given: "a note"
        note

        and: "a user who is not the owner of the context"
        User aUser = Mock(User)

        when: "the user try to duplicate the scope"
        noteService.duplicateNoteInContext(note, targetContext, aUser)

        then: "an exception is thrown"
        thrown(PreconditionViolation)
    }

    def "duplication of a note in a target context must duplicate content, author, fragment tag and kind"() {
        given: "a note"
        note

        when: "the context is duplicated"
        Note newNote = noteService.duplicateNoteInContext(note, targetContext, user)

        then: "the duplicated note is associated with the target context"
        newNote.context == targetContext

        and: "the duplicated context has the same properties than the original"
        newNote.author == user
        newNote.content == note.content
        newNote.fragmentTag == note.fragmentTag
        newNote.kind == note.kind

        and: "the new note has no errors"
        !newNote.hasErrors()
    }

}
