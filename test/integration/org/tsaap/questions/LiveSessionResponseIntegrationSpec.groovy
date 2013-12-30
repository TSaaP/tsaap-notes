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
import org.tsaap.questions.impl.gift.GiftQuestionService
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
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[[]]')

        then:"a violation of a precondition occurs"
        thrown(PreconditionViolation)

        when:"the session is started"
        liveSession.start()

        and:"trying to create a response for a user"
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[[]]')

        then:"the response is created"
        liveSession.getResponseForUser(bootstrapTestService.learnerMary)

        when:"a trying to create a response for the same user and same session"
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[[]]')

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

        then: "the user response is set correctly"
        userResponse != null
        userResponse.userAnswerBlockList[0].answerList[0].textValue == "this"

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

        when:"the live session response content correspond to a empty response"
        liveSessionResponse = new LiveSessionResponse(
                liveSession: liveSession,answerListAsString: '[[]]',
                user: bootstrapTestService.learnerMary)

        and: "trying to get the user response"
        userResponse = liveSessionResponse.userResponse

        then: "the user response is set with the no response answer"
        userResponse.userAnswerBlockList[0].answerList[0] == note.giftQuestionService.noResponseAnswer

        and:"the percent credit of the live session response is set to 0"
        liveSessionResponse.percentCredit == 0


    }

    void "test the construction of the result matrix"() {
        given:"a started live session for a note"
        def note2 = noteService.addNote(bootstrapTestService.teacherJeanne,"::a question:: What ? {~bad1 =good1 ~bad2 ~bad3}")
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(bootstrapTestService.teacherJeanne,note2)
        liveSession.start()

        and:"several live session responses"
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[["good1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerPaul,'[["good1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[0],'[["good1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[1],'[["good1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[2],'[["good1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[3],'[["good1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[4],'[["bad3"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[5],'[["bad3"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[6],'[["bad3"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[7],'[["bad1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[8],'[[]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[9],'[[]]')


        when:"calculating the matrix "
        def matrix = liveSession.resultMatrix()

        then:"the given matrix is correctly set"
        matrix.size() == 1
        def currentMap = matrix[0]
        currentMap["good1"] == 50
        currentMap["bad2"] == 0
        currentMap["bad3"] == (3/12)*100
        currentMap["bad1"] == (1/12)*100
        currentMap[GiftQuestionService.NO_RESPONSE] == (2/12)*100

    }


}
