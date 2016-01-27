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
import org.tsaap.notes.Context
import org.tsaap.notes.ContextService
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService
import spock.lang.Specification

/**
 * Created by franck on 28/10/2014.
 */
@TestFor(ContextService)
@Mock([Context,Note])
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
        given:"a contex"
        context

        and:"a user who is not the owner of the context"
        User aUser = Mock(User)

        when:"the user try to duplicate the scope"
        contextService.duplicateContext(context,aUser)

        then:"an exception is thrown"
        thrown(PreconditionViolation)
    }

    def "duplication of a context must duplicate name, url, owner"() {
        given:"a context"
        context

        when: "the context is duplicated"
        Context newContext = contextService.duplicateContext(context, user,false)

        then:"the duplicated context has the same properties than the original"
        newContext.contextName == "${context.contextName}$ContextService.SUFFIXE_COPY"
        newContext.url == context.url
        newContext.owner == context.owner
        newContext.descriptionAsNote == context.descriptionAsNote

        and:"he has no errors"
        newContext.hasErrors()
    }

    def "default duplication of a context must duplicate the questions of the owner of the scope"() {
        given:"a context"
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
            findAllNotesAsQuestionForContext(context) >> [q1,q2,q3]
        }

        when: "the context is duplicated"
        contextService.duplicateContext(context, user)

        then:"only note as question authored by the scope owner is duplicated"
        1 * contextService.noteService.duplicateNoteInContext(q1,_,user)
        1 * contextService.noteService.duplicateNoteInContext(q2,_,user)
        0 * contextService.noteService.duplicateNoteInContext(q3,_,user)

    }



}
