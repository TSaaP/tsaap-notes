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

import org.tsaap.BootstrapTestService
import org.tsaap.questions.LiveSession
import org.tsaap.questions.LiveSessionService
import spock.lang.Specification

class NoteIntegrationSpec extends Specification {
    BootstrapTestService bootstrapTestService
    NoteService noteService
    LiveSessionService liveSessionService
    ContextService contextService

    def setup() {
        bootstrapTestService.initializeTests()
    }


    void "test the finding of the last live session for a note"() {
        when: "a note is not a question"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerMary, "not a question")

        then: "no live session found"
        !note.liveSession

        when: "a note is a question but that has no live session"
        Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerMary, contextName: "context"))
        note = noteService.addQuestion(bootstrapTestService.learnerMary, "::a question:: what ? {=this ~that}", context)

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
