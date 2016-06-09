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
import spock.lang.Specification

/**
 * Created by franck on 28/10/2014.
 */
@TestFor(ContextService)
@Mock([Context, Note])
class ContextServiceSpec extends Specification {

    ContextService contextService
    User user
    Context context

    def setup() {
        contextService = new ContextService()
        user = Mock(User)
        context = Mock(Context) {
            getContextName() >> "scope 1"
            getUrl() >> "http://aurl"
            getOwner() >> user
            getDescriptionAsNote() >> "a description with #aTag and #anOtherTag"
        }
    }

    def "duplication of a context is only possible for the owner of the original scope"() {
        given: "a contex"
        context

        and: "a user who is not the owner of the context"
        User aUser = Mock(User)

        when: "the user try to duplicate the scope"
        contextService.duplicateContext(context, aUser)

        then: "an exception is thrown"
        thrown(PreconditionViolation)
    }

    def "duplication of a context must duplicate name, url, owner"() {
        given: "a context"
        context

        when: "the context is duplicated"
        Context newContext = contextService.duplicateContext(context, user, false)

        then: "the duplicated context has the same properties than the original"
        newContext.contextName == "${context.contextName}$ContextService.SUFFIXE_COPY"
        newContext.url == context.url
        newContext.owner == context.owner
        newContext.descriptionAsNote == context.descriptionAsNote

        and: "he has no errors"
        newContext.hasErrors()
    }

    def "default duplication of a context must duplicate the questions of the owner of the scope"() {
        given: "a context"
        context

        and: "2 question notes associated with the context"
        Note q1 = Mock(Note) {
            getAuthor() >> user
        }
        Note q2 = Mock(Note) {
            getAuthor() >> user
        }
        Note q3 = Mock(Note) {
            getAuthor() >> Mock(User)
        }
        contextService.noteService = Mock(NoteService) {
            findAllNotesAsQuestionForContext(context) >> [q1, q2, q3]
        }

        when: "the context is duplicated"
        contextService.duplicateContext(context, user)

        then: "only note as question authored by the scope owner is duplicated"
        1 * contextService.noteService.duplicateNoteInContext(q1, _, user)
        1 * contextService.noteService.duplicateNoteInContext(q2, _, user)
        0 * contextService.noteService.duplicateNoteInContext(q3, _, user)

    }


}
