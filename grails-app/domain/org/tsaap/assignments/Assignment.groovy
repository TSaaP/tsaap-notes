package org.tsaap.assignments

import org.tsaap.directory.User

/**
 * Class corresponding to an assignment
 */
class Assignment {

    String title
    User owner

    Date dateCreated
    Date lastUpdated

    String globalId

    static constraints = {
        title blank: false
        globalId nullable: true
    }

    /**
     * Get the schedule associated with this assignment if any
     * @return the schedule
     */
    Schedule getSchedule() {
        Schedule.findByAssignment(this)
    }

    /**
     * Get the sequences
     * @return the sequences
     */
    List<Sequence> getSequences() {
        Sequence.findAllByAssignment(this,[sort:'rank', order:'asc'])
    }

    /**
     * Get the last sequence of the current assignment
     * @return
     */
    Sequence getLastSequence() {
        def res = null
        if (sequences) {
            res = sequences.last()
        }
        res
    }

    static transients = ['schedule', 'sequences', 'lastSequence']
}
