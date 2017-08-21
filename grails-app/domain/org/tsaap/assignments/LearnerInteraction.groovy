package org.tsaap.assignments

import org.tsaap.directory.User

class LearnerInteraction {

    User learner
    Interaction interaction
    Boolean isActive = false
    String state = StateType.show.name()
    Date dateCreated
    Date lastUpdated

    static constraints = {
        state inList: StateType.values()*.name()
    }

    static mapping = {
        interaction fetch: 'join'
    }

    static transients = ['sequence']

    /**
     * Activate the current learner interaction
     */
    def activate() {
        def query = LearnerInteraction.where {
            learner == this.learner
        }
        query.updateAll(isActive: false)
        isActive = true
        save()
    }

    /**
     * Get the sequence
     * @return the sequence
     */
    Sequence getSequence() {
        interaction.sequence
    }
}
