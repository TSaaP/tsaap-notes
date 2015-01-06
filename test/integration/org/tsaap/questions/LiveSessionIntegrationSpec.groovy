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

class LiveSessionIntegrationSpec extends Specification {
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
                liveSession: liveSession,answerListAsString: '[["0"]]',
                user: bootstrapTestService.learnerMary)

        when: "getting the user response"
        UserResponse userResponse = liveSessionResponse.userResponse

        then: "the user response is set correctly"
        userResponse != null
        userResponse.userAnswerBlockList[0].answerList[0].identifier == "0"

        and:"the user response percent credit is reported on the percent credit of the live session response"
        userResponse.evaluatePercentCredit() == liveSessionResponse.percentCredit
        liveSessionResponse.percentCredit == 100

        when: "the live session response content is mal formated"
        liveSessionResponse = new LiveSessionResponse(
                liveSession: liveSession,answerListAsString: '[["0]',
                user: bootstrapTestService.learnerMary)

        and: "trying to get the user response"
        userResponse = liveSessionResponse.userResponse

        then: "the user response is  set as empty response"
        userResponse.userAnswerBlockList.size() == 1
        userResponse.userAnswerBlockList[0].answerList[0].textValue == GiftQuestionService.NO_RESPONSE

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
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerPaul,'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[0],'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[1],'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[2],'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[3],'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[4],'[["3"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[5],'[["3"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[6],'[["3"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[7],'[["0"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[8],'[[]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learners[9],'[[]]')


        when:"calculating the matrix "
        def matrix = liveSession.buildResultMatrix()

        then:"the given matrix is correctly set"
        matrix.size() == 1
        def currentMap = matrix[0]
        currentMap["good1"] == 50
        currentMap["bad2"] == 0
        currentMap["bad3"] == (3/12)*100
        currentMap["bad1"] == (1/12)*100
        currentMap[GiftQuestionService.NO_RESPONSE] == (2/12)*100

    }

    void "test the construction of the result matrix in a session phase"() {
        given:"a started live session for a note"
        def note2 = noteService.addNote(bootstrapTestService.teacherJeanne,"::a question:: What ? {~bad1 =good1 ~bad2 ~bad3}")
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(bootstrapTestService.teacherJeanne,note2)
        SessionPhase sessionPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(bootstrapTestService.teacherJeanne,liveSession)

        and:"several live session responses"
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learnerMary,'[["1"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learnerPaul,'[["1"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[0],'[["1"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[1],'[["1"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[2],'[["1"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[3],'[["1"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[4],'[["3"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[5],'[["3"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[6],'[["3"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[7],'[["0"]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[8],'[[]]',null,1)
        liveSessionService.createResponseForSessionPhaseAndUser(sessionPhase,bootstrapTestService.learners[9],'[[]]',null,1)


        when:"calculating the matrix "
        def matrix = sessionPhase.buildResultMatrix()

        then:"the given matrix is correctly set"
        matrix.size() == 1
        def currentMap = matrix[0]
        currentMap["good1"] == 50
        currentMap["bad2"] == 0
        currentMap["bad3"] == (3/12)*100
        currentMap["bad1"] == (1/12)*100
        currentMap[GiftQuestionService.NO_RESPONSE] == (2/12)*100

    }

    void "test the delete of a live session with responses"() {
        given:"a live session with at least one response"
        def liveSession = liveSessionService.createAndStartLiveSessionForNote(bootstrapTestService.learnerPaul,note)
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.teacherJeanne,'[["1"]]')

        when:"deleting the live session"
        liveSessionService.deleteLiveSessionByAuthor(liveSession,bootstrapTestService.learnerPaul)

        then:"the responses are deleted"
        !LiveSessionResponse.findAllByLiveSession(liveSession)

        and:"the live session is deleted"
        !LiveSession.findByNote(note)
    }

    void "test the delete of a live session with phase and responses"() {
        given:"a live session with at least one response"
        def liveSession = liveSessionService.createLiveSessionForNote(bootstrapTestService.learnerPaul,note)
        def phase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(bootstrapTestService.learnerPaul,liveSession)
        def explanationId = liveSessionService.createResponseForSessionPhaseAndUser(phase,bootstrapTestService.learnerMary,'[["1"]]',"an explanation",1).explanationId

        when:"deleting the live session"
        liveSessionService.deleteLiveSessionByAuthor(liveSession,bootstrapTestService.learnerPaul)

        then:"the responses are deleted"
        !LiveSessionResponse.findAllByLiveSession(liveSession)
        !LiveSessionResponse.findAllBySessionPhase(phase)

        and:"the live session and the phase is deleted"
        !SessionPhase.findByLiveSession(liveSession)
        !LiveSession.findByNote(note)
        //!Note.findById(explanationId)
    }

    void "test the delete of a note that is a question with live sessions"() {
        given:"a note with live sessions and live session responses attached"
        def liveSession = liveSessionService.createAndStartLiveSessionForNote(bootstrapTestService.learnerPaul,note)
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.learnerMary,'[["1"]]')
        liveSessionService.createResponseForLiveSessionAndUser(liveSession,bootstrapTestService.teacherJeanne,'[["1"]]')
        liveSession.stop()
        liveSessionService.createAndStartLiveSessionForNote(bootstrapTestService.learnerPaul,note)

        when:"deleting the note"
        noteService.deleteNoteByAuthor(note,note.author)

        then:"the live sessions and live session responses are deleted"
        !LiveSessionResponse.findAllByLiveSession(liveSession)
        !LiveSession.findAllByNote(note)

        and:"the note is deleted"
        !Note.findById(note.id)

    }

}
