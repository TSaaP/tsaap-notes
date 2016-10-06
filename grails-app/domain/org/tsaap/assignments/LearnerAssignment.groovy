package org.tsaap.assignments

import org.tsaap.directory.User

class LearnerAssignment {

    User learner
    Assignment assignment
    Date dateCreated
    Date lastUpdated

    static constraints = {
    }

    static mapping = {
        assignment fetch: 'join'
    }
}
