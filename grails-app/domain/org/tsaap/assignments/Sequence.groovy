package org.tsaap.assignments

import org.tsaap.directory.User

class Sequence {

    Integer rank

    Date dateCreated
    Date lastUpdated

    User owner
    Assignment assignment
    Statement statement

    static constraints = {

    }

    static transients = ['interactions']

    /**
     * Find all interactions
     * @return the interactions
     */
    List<Interaction> getInteractions() {
        Interaction.findAllBySequence(this, sort:'rank', order:'asc')
    }
}
