package org.tsaap.assignments

import org.tsaap.directory.User

class LearnerSequence {

    User learner
    Sequence sequence
    Interaction activeInteraction
    // String state = StateType.show.name() *** JT: seems unused. To remove ?
    Date dateCreated
    Date lastUpdated

    static constraints = {
        activeInteraction nullable: true
    }

}
