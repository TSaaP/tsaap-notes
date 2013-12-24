package org.tsaap.questions

import org.tsaap.notes.Note

class LiveSession {

    Date dateCreated
    String status = LiveSessionStatus.NotStarted.name()
    Date startDate
    Date endDate

    Note note

    static constraints = {
        status inList: LiveSessionStatus.values()*.name()
        startDate nullable: true
        endDate nullable: true
    }
}

enum LiveSessionStatus {
    NotStarted,
    Started,
    Ended
}