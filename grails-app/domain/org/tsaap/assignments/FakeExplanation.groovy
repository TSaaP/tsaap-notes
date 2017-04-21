package org.tsaap.assignments

import org.tsaap.directory.User

class FakeExplanation {

    Date dateCreated
    Date lastUpdated
    Integer correspondingItem
    String content

    User author
    Statement statement

    static constraints = {
        correspondingItem nullable: true
        content blank: false
    }
}
