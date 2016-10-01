package org.tsaap.assignments

import org.tsaap.directory.User

class PeerGrading {

    Date dateCreated
    Date lastUpdated

    Float grade
    String annotation

    User grader
    ChoiceInteractionResponse response


    static constraints = {
        annotation nullable: true
        grade nullable: true
    }
}
