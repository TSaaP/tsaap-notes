package org.tsaap.assignments

import grails.test.mixin.TestFor
import org.tsaap.contracts.ConditionViolationException
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Schedule)
class ScheduleSpec extends Specification {

    def setup() {
    }

    void "test start and end of manual schedule "() {
        given: "a schedule"
        Schedule schedule = new Schedule().save()

        expect: "schedule is manual"
        schedule.isManual

        when: "starting the schedule"
        schedule.start()

        then : "the schedule is started, open, not ended, not closed"
        schedule.isStarted
        schedule.isOpen()
        !schedule.isEnded
        !schedule.isClosed()

        when: "ending the schedule"
        schedule.end()

        then: "the schedule is started, ended, closed and not open"
        schedule.isStarted
        schedule.isEnded
        schedule.isClosed()
        !schedule.isOpen()
    }

    void "test start of an already started schedule"() {

        given: "a schedule already started"
        Schedule schedule = new Schedule().save()
        schedule.start()

        expect: "the schedule is open"
        schedule.isOpen()

        when: "starting again"
        schedule.start()

        then: "an exception is thrown"
        def exception = thrown(ConditionViolationException)
        exception.message == Schedule.IS__OPEN

        when: "ending the schedule"
        schedule.end()

        and: "trying to start it again"
        schedule.start()

        then: "the schedule is restarted"
        schedule.isOpen()

    }

    void "test end of a not started schedule"() {
        given: "a schedule"
        Schedule schedule = new Schedule().save()

        when: "ending the schedule"
        schedule.end()

        then: "an exception is thrown"
        def exception = thrown(ConditionViolationException)
        exception.message == Schedule.IS__CLOSED

    }

    void "test end of a al ready ended schedule"() {
        given: "a schedule"
        Schedule schedule = new Schedule().save()
        schedule.start()
        schedule.end()

        when: "ending the schedule"
        schedule.end()

        then: "an exception is thrown"
        def exception = thrown(ConditionViolationException)
        exception.message == Schedule.IS__CLOSED

    }


}
