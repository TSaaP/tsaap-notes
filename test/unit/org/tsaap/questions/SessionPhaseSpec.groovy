package org.tsaap.questions

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.gcontracts.PreconditionViolation
import org.tsaap.notes.Note
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
