package org.tsaap.assignments

import org.tsaap.directory.User

class PeerGrading {

    Date dateCreated
    Date lastUpdated

    Float grade
    String annotation

    User grader
    ChoiceInteractionResponse response
    OpenInteractionResponse openResponse


    static constraints = {
        annotation nullable: true
        grade nullable: true
        response nullable: true, validator: { val, obj ->
            if (obj.openResponse == null && val == null) {
                return ['responseCannotBeNull']
            }
        }
        openResponse nullable: true, validator: {val, obj ->
            if (obj.response == null && val == null) {
                return ['responseCannotBeNull']
            }
        }
    }
}
