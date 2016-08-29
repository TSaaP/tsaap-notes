package org.tsaap.assignments

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
}
