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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gcontracts.PreconditionViolation
import org.tsaap.notes.Note
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(LiveSession)
@Mock([Note, LiveSessionResponse])
class LiveSessionSpec extends Specification {

    void "test the start and stop of a live session"() {
        given: " a newly created live session "
        LiveSession liveSession = new LiveSession(note: mockDomain(Note))
        liveSession.isNotStarted()

        when: " starting the live session"
        liveSession.start()

        then: " the status and the start date are updated"
        !liveSession.isNotStarted()
        liveSession.isStarted()
        liveSession.startDate != null

        when: "trying to restart the same live session"
        liveSession.start()

        then: "an precondition violation occurs"
        thrown(PreconditionViolation)

        when: "trying to stop the live session"
        liveSession.stop(false)

        then: "the status is updated and the end date is updated"
        !liveSession.isNotStarted()
        !liveSession.isStarted()
        liveSession.isStopped()
        liveSession.endDate != null

    }
}
