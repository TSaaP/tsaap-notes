/*
 * Copyright 2015 Tsaap Development Group
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
import org.tsaap.directory.User
import org.tsaap.notes.Note
import org.tsaap.notes.NoteKind
import org.tsaap.notes.NoteService
import org.tsaap.questions.impl.gift.GiftQuestionService
import spock.lang.Specification

class LiveSessionServiceIntegrationSpec extends Specification {
    BootstrapTestService bootstrapTestService
    NoteService noteService
    LiveSessionService liveSessionService
    Note note
    User user

    def setup() {
        bootstrapTestService.initializeTests()
        user = bootstrapTestService.learnerPaul
        note = noteService.addQuestion(user,"::a question:: What ? {=this ~that}")
    }

    void "test the creation of a first phase session"() {
        given:"a live session and a user"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)

        when:"trying to create a first phase"
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user,liveSession)

        then: "the first phase is created without errors"
        !firstPhase.hasErrors()
        firstPhase.rank == 1
        firstPhase.liveSession == liveSession

        and:"both live session and session phase are started"
        liveSession.isStarted()
        firstPhase.isStarted()

        when: "trying to create a new phase"
        SessionPhase otherPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

    }

    void "test the creation of a second phase session"() {
        given:"a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user,liveSession)

        when: "trying to create the second phase"
        liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession,2)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

        when:"the first phase is stopped"
        firstPhase.stop()

        and:"trying again"
        SessionPhase secondPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession,2)

        then: "the second phase is created without errors"
        !secondPhase.hasErrors()
        secondPhase.rank == 2
        secondPhase.liveSession == liveSession


    }

    void "test the creation of a third phase session"() {
        given:"a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user,liveSession)

        when: "trying to create the second phase"
        SessionPhase thirdPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession,3)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

        when:"the first phase is stopped"
        firstPhase.stop()

        and:"trying again"
        thirdPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession,3)

        then: "the second phase is created without errors"
        !thirdPhase.hasErrors()
        thirdPhase.rank == 3
        thirdPhase.liveSession == liveSession

    }

    void "test the creation of a response for a given phase"() {

        given:"a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user,liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase,learner,'[["1"]]',"an explanation",5)

        then: "the response is created without errors"
        !response.hasErrors()
        response.liveSession == liveSession
        response.sessionPhase == firstPhase
        response.explanation.content == "an explanation"
        response.explanation.kind == NoteKind.EXPLANATION.ordinal()
        response.confidenceDegree == 5

        when:"trying again"
        liveSessionService.createResponseForSessionPhaseAndUser(firstPhase,learner,'[["1"]]',"an explanation",5)

        then:"a precondition is violated"
        thrown(PreconditionViolation)

    }

    void "test the creation of a response for a given phase with empty explanation"() {

        given:"a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user,liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response with null explanation for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase,learner,'[["1"]]',null,5)

        then: "the response is created without errors"
        !response.hasErrors()
        response.explanation == null

        when: "creating the second phase"
        firstPhase.stop()
        SessionPhase secondPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user,liveSession,2)

        and:"creating In the second phase the explanation is empty"
        response = liveSessionService.createResponseForSessionPhaseAndUser(secondPhase,learner,'[["1"]]',"",5)

        then: "the response is created without errors"
        !response.hasErrors()
        response.explanation == null

    }

    void "test the creation of a response for a given phase with no degree of confidence"() {

        given:"a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user,liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response with null explanation for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase,learner,'[["1"]]',"an exxpl",null)

        then: "the response is created without errors"
        !response.hasErrors()
        response.confidenceDegree == null

    }


}
