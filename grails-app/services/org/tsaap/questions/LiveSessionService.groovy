package org.tsaap.questions

import grails.transaction.Transactional
import org.gcontracts.annotations.Requires
import org.tsaap.directory.User
import org.tsaap.notes.Note

@Transactional
class LiveSessionService {

    /**
     * Create a live session for a corresponding note
     * @param user the user of the session
     * @param note the note the live session is associated with
     * @return the live session
     */
    @Requires({user == note.author && note.isAQuestion() && !note.activeLiveSession})
    LiveSession createLiveSessionForNote(User user, Note note) {
        LiveSession liveSession = new LiveSession(note:note)
        liveSession.save()
        liveSession
    }
}
