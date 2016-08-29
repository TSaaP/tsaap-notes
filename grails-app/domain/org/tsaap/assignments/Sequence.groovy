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

    static transients = ['interactions','content', 'title']

    /**
     * Find all interactions
     * @return the interactions
     */
    List<Interaction> getInteractions() {
        Interaction.findAllBySequence(this, [sort:'rank', order:'asc'])
    }

    /**
     * Get the title of the statement
     * @return the title
     */
    String getTitle() {
        statement?.title
    }

    /**
     * Get the content of the statement
     * @return the content
     */
    String getContent() {
        statement?.content
    }


}
