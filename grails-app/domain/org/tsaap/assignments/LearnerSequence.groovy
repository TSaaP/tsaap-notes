package org.tsaap.assignments

import org.tsaap.directory.User

class LearnerSequence {

    User learner
    Sequence sequence
    Interaction activeInteraction
    String state = StateType.show.name()
    Date dateCreated
    Date lastUpdated

    static constraints = {
        activeInteraction nullable: true
    }

}
