/*
 * Copyright 2013 Tsaap Development Group
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

package org.tsaap.questions

import org.gcontracts.PreconditionViolation
import org.tsaap.BootstrapTestService
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService
import spock.lang.Specification

class LiveSessionResponseIntegrationSpec extends Specification {
    BootstrapTestService bootstrapTestService
    NoteService noteService
    LiveSessionService liveSessionService
    Note note

    def setup() {
        bootstrapTestService.initializeTests()
        note = noteService.addNote(bootstrapTestService.learnerPaul,"::a question:: What ? {=this ~that}")
    }

    void "test the creation of a new live session response"() {
        given:"a live session and a user"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(note.author, note)

        when:"the live session is not started"
        liveSession.isNotStarted()

        and:"trying to create a response for a user"
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[]')

        then:"a violation of a precondition occurs"
        thrown(PreconditionViolation)

        when:"the session is started"
        liveSession.start()

        and:"trying to create a response for a user"
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[]')

        then:"the response is created"
        liveSession.getResponseForUser(bootstrapTestService.learnerMary)

        when:"a trying to create a response for the same user and same session"
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[]')

        then:"a violation of a precondition occurs"
        thrown(PreconditionViolation)


    }

    void "test the construction of UserResponse from a live session response"() {

        given:"a started live session for a note"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(bootstrapTestService.learnerPaul,note)
        liveSession.start()

        and: "and a live session response"
        LiveSessionResponse liveSessionResponse = new LiveSessionResponse(
                liveSession: liveSession,answerListAsString: '[["this"]]',
                user: bootstrapTestService.learnerMary)

        when: "getting the user response"
        UserResponse userResponse = liveSessionResponse.userResponse

        then: "the user response is set"
        userResponse != null

        and:"the user response percent credit is reported on the percent credit of the live session response"
        userResponse.evaluatePercentCredit() == liveSessionResponse.percentCredit
        liveSessionResponse.percentCredit == 100

        when: "the live session response content is mal formated"
        liveSessionResponse = new LiveSessionResponse(
                liveSession: liveSession,answerListAsString: '[["this]',
                user: bootstrapTestService.learnerMary)

        and: "trying to get the user response"
        userResponse = liveSessionResponse.userResponse

        then: "the user response is not set"
        userResponse == null

        and:"the percent credit of the live session response is set to 0"
        liveSessionResponse.percentCredit == 0


    }


}
