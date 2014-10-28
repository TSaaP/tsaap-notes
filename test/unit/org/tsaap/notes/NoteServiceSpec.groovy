/*
 * Copyright 2014 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.tsaap.notes

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gcontracts.PreconditionViolation
import org.tsaap.directory.User
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
        }
        targetContext = Mock(Context)
    }

    def "duplication of a note is only possible for the owner of the note"() {
        given:"a note"
        note

        and:"a user who is not the owner of the context"
        User aUser = Mock(User)

        when:"the user try to duplicate the scope"
        noteService.duplicateNoteInContext(note,targetContext,aUser)

        then:"an exception is thrown"
        thrown(PreconditionViolation)
    }

    def "duplication of a note in a target context must duplicate content, author and fragment tag "() {
        given:"a note"
        note

        when: "the context is duplicated"
        Note newNote = noteService.duplicateNoteInContext(note, targetContext, user)

        then:"the duplicated note is associated with the target context"
        newNote.context == targetContext

        and:"the duplicated context has the same properties than the original"
        newNote.author == user
        newNote.content == note.content
        newNote.fragmentTag == note.fragmentTag

        and:"the new note has no errors"
        !newNote.hasErrors()
    }

}
