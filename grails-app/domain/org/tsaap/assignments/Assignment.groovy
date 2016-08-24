package org.tsaap.assignments

import org.tsaap.directory.User

/**
 * Class corresponding to an assignment
 */
class Assignment {

    String title
    User owner

    Date dateCreated

    static constraints = {
        title blank: false
    }
}
