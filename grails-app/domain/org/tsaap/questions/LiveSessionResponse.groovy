package org.tsaap.questions

import org.tsaap.directory.User

class LiveSessionResponse {

    LiveSession liveSession
    User user
    String answersAsString
    Float percentCredit = 0

    static constraints = {
        answersAsString nullable: true
    }
}
