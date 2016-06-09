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

import grails.test.mixin.TestFor
import org.gcontracts.PreconditionViolation
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(SessionPhase)
class SessionPhaseSpec extends Specification {


    void "test the start and stop of a live session"() {
        given: " a newly created live session "
        SessionPhase sessionPhase = new SessionPhase(liveSession: Mock(LiveSession))
        sessionPhase.isNotStarted()

        when: " starting the live session"
        sessionPhase.start()

        then: " the status and the start date are updated"
        !sessionPhase.isNotStarted()
        sessionPhase.isStarted()
        sessionPhase.startDate != null
        sessionPhase.endDate == null

        when: "trying to restart the same live session"
        sessionPhase.start()

        then: "an precondition violation occurs"
        thrown(PreconditionViolation)

        when: "trying to stop the live session"
        sessionPhase.stop(false)

        then: "the status is updated and the end date is updated"
        !sessionPhase.isNotStarted()
        !sessionPhase.isStarted()
        sessionPhase.isStopped()
        sessionPhase.endDate != null

    }
}
