package org.tsaap.assignments

import org.tsaap.attachement.Attachement
import org.tsaap.directory.User

class Statement {

    String title
    String content

    Date dateCreated
    Date lastUpdated

    User owner

    static constraints = {
        title blank: false
        content blank: false
    }

    /**
     * Get the attachment
     * @return the attachment
     */
    Attachement getAttachment() {
        if (id == null) {
            return null
        }
        Attachement.findByStatement(this)
    }

}
