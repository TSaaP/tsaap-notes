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

package org.tsaap.questions

import org.gcontracts.PreconditionViolation
import org.tsaap.BootstrapTestService
import org.tsaap.directory.User
import org.tsaap.notes.Context
import org.tsaap.notes.ContextService
import org.tsaap.notes.Note
import org.tsaap.notes.NoteKind
import org.tsaap.notes.NoteService
import spock.lang.Specification

class LiveSessionServiceIntegrationSpec extends Specification {
    BootstrapTestService bootstrapTestService
    NoteService noteService
    LiveSessionService liveSessionService
    Note note
    User user
    ContextService contextService

    def setup() {
        bootstrapTestService.initializeTests()
        user = bootstrapTestService.learnerPaul
        Context context = contextService.saveContext(new Context(owner: user, contextName: "context"))
        note = noteService.addQuestion(user, "::a question:: What ? {=this ~that}", context)
    }

    void "test the creation of a first phase session"() {
        given: "a live session and a user"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)

        when: "trying to create a first phase"
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        then: "the first phase is created without errors"
        !firstPhase.hasErrors()
        firstPhase.rank == 1
        firstPhase.liveSession == liveSession

        and: "both live session and session phase are started"
        liveSession.isStarted()
        firstPhase.isStarted()

        when: "trying to create a new phase"
        SessionPhase otherPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

    }

    void "test the creation of a second phase session"() {
        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        when: "trying to create the second phase"
        liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 2)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

        when: "the first phase is stopped"
        firstPhase.stop()

        and: "trying again"
        SessionPhase secondPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 2)

        then: "the second phase is created without errors"
        !secondPhase.hasErrors()
        secondPhase.rank == 2
        secondPhase.liveSession == liveSession


    }

    void "test the creation of a third phase session"() {
        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        when: "trying to create the second phase"
        SessionPhase thirdPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 3)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

        when: "the first phase is stopped"
        firstPhase.stop()

        and: "trying again"
        thirdPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 3)

        then: "the second phase is created without errors"
        !thirdPhase.hasErrors()
        thirdPhase.rank == 3
        thirdPhase.liveSession == liveSession

    }

    void "test the creation of a response for a given phase"() {

        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase, learner, '[["1"]]', "an explanation", 5)

        then: "the response is created without errors"
        !response.hasErrors()
        response.liveSession == liveSession
        response.sessionPhase == firstPhase
        response.explanation.content == "an explanation"
        response.explanation.kind == NoteKind.EXPLANATION.ordinal()
        response.confidenceDegree == 5

        when: "trying again"
        liveSessionService.createResponseForSessionPhaseAndUser(firstPhase, learner, '[["1"]]', "an explanation", 5)

        then: "a precondition is violated"
        thrown(PreconditionViolation)

    }

    void "test the creation of a response for a given phase with empty explanation"() {

        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response with null explanation for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase, learner, '[["1"]]', null, 5)

        then: "the response is created without errors"
        !response.hasErrors()
        response.explanation == null

        when: "creating the second phase"
        firstPhase.stop()
        SessionPhase secondPhase = liveSessionService.createAndStartSessionPhaseForLiveSessionWithRank(user, liveSession, 2)

        and: "creating In the second phase the explanation is empty"
        response = liveSessionService.createResponseForSessionPhaseAndUser(secondPhase, learner, '[["1"]]', "", 5)

        then: "the response is created without errors"
        !response.hasErrors()
        response.explanation == null

    }

    void "test the creation of a response for a given phase with no degree of confidence"() {

        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response with null explanation for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase, learner, '[["1"]]', "an exxpl", null)

        then: "the response is created without errors"
        !response.hasErrors()
        response.confidenceDegree == null

    }

    void "test the creation of a response with XSS explanation"() {
        given: "a live session and a user and the first phase started"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(user, note)
        SessionPhase firstPhase = liveSessionService.createAndStartFirstSessionPhaseForLiveSession(user, liveSession)

        and: "a learner"
        def learner = bootstrapTestService.learnerMary

        when: "trying to create a response for that learner for this session"
        LiveSessionResponse response = liveSessionService.createResponseForSessionPhaseAndUser(firstPhase, learner, '[["1"]]', "<script>alert('XSS');</script>", 5)

        then: "the response has no or empty explanation"
        !response.explanation?.content
    }
}
