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

package org.tsaap.notes

import org.tsaap.BootstrapTestService
import org.tsaap.questions.LiveSession
import org.tsaap.questions.LiveSessionService
import spock.lang.Specification

class NoteIntegrationSpec extends Specification {
    BootstrapTestService bootstrapTestService
    NoteService noteService
    LiveSessionService liveSessionService

    def setup() {
        bootstrapTestService.initializeTests()
    }


    void "test the finding of the last live session for a note"() {
        when: "a note is not a question"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerMary, "not a question")

        then: "no live session found"
        !note.liveSession

        when: "a note is a question but that has no live session"
        note = noteService.addQuestion(bootstrapTestService.learnerMary, "::a question:: what ? {=this ~that}")

        then: "no live session found"
        !note.liveSession

        and: "the note has one live session"
        LiveSession liveSession = liveSessionService.createLiveSessionForNote(bootstrapTestService.learnerMary, note)

        then: "a live session is found"
        note.liveSession == liveSession

        when: "the note is a question and has more than one live session"
        liveSession.start()
        liveSession.stop()
        LiveSession lastLiveSession = liveSessionService.createLiveSessionForNote(bootstrapTestService.learnerMary, note)

        then: "the last live session is returned"
        note.liveSession == lastLiveSession

    }

    void "test the evaluation of the grade of a note"() {
        given: "a note without grade"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerMary, "not a question")

        when: "trying to update the grade"
        note.updateMeanGrade()

        then: "the grade is null"
        note.grade == null

        when: "adding one grade"
        def user = bootstrapTestService.learnerPaul
        noteService.gradeNotebyUser(note, user, 2d)

        and: "reevaluate the mean grade"
        note.updateMeanGrade()

        then: "the mean grade is equal to the unique grade"
        note.grade == 2d

        when: "having 2 grades"
        def user2 = bootstrapTestService.learnerMary
        noteService.gradeNotebyUser(note, user2, 3d)

        and: "reevaluate the mean grade"
        note.updateMeanGrade()

        then: "the mean grade is effectively the mean"
        note.grade == 2.5d

    }


}
