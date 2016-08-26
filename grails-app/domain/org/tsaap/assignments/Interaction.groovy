package org.tsaap.assignments

import org.tsaap.directory.User

class Interaction {

    Integer rank
    String specification

    Date dateCreated
    Date lastUpdated

    User owner
    Sequence sequence

    static constraints = {
    }

    static transients = ['schedule']

    /**
     * Get the schedule
     * @return the schedule
     */
    Schedule getSchedule() {
        Schedule.findByInteraction(this)
    }
}
