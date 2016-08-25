package org.tsaap.assignments

import org.tsaap.directory.User

/**
 * Class corresponding to an assignment
 */
class Assignment {

    String title
    User owner

    Date dateCreated

    static constraints = {
        title blank: false
    }

    /**
     * Get the schedule associated with this assignment if any
     * @return the schedule
     */
    Schedule getSchedule() {
        Schedule.findByAssignment(this)
    }

    static transients = ['schedule']
}
