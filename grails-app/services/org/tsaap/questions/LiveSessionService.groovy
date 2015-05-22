package org.tsaap.questions

import grails.transaction.NotTransactional
import grails.transaction.Transactional
import org.gcontracts.annotations.Requires
import org.tsaap.directory.User
import org.tsaap.notes.Bookmark
import org.tsaap.notes.Note
import org.tsaap.notes.NoteGrade
import org.tsaap.notes.NoteKind
import org.tsaap.notes.NoteMention
import org.tsaap.notes.NoteService
import org.tsaap.notes.NoteTag

@Transactional
class LiveSessionService {

    NoteService noteService

    /**
     * Create a live session for a corresponding note
     * @param user the user of the session
     * @param note the note the live session is associated with
     * @return the live session
     */
    @Requires({user == note.author && note.isAQuestion() && !note.activeLiveSession})
    LiveSession createLiveSessionForNote(User user, Note note) {
        LiveSession liveSession = new LiveSession(note:note)
        liveSession.save(flush: true)
        note.liveSession = liveSession
        liveSession
    }

    /**
     * Create and start a live session for a corresponding note
     * @param user the user of the session
     * @param note the note the live session is associated with
     * @return the started live session
     */
    LiveSession createAndStartLiveSessionForNote(User user, Note note) {
        createLiveSessionForNote(user,note).start()
    }

    /**
     * Create a live session response for a given live session and  a given user
     * @param liveSession the given live session
     * @param user the given user
     * @param value the text value of the response
     * @return the live session response
     */
    @Requires({ liveSession.isStarted() && !liveSession.isStopped() && !liveSession.getResponseForUser(user) })
    LiveSessionResponse createResponseForLiveSessionAndUser(LiveSession liveSession,User user, String value) {
        LiveSessionResponse liveSessionResponse = new LiveSessionResponse(
                liveSession:liveSession,
                user: user,
                answerListAsString: value)
        liveSessionResponse.save()
        liveSessionResponse
    }

    /**
     * Delete a live session
     * @param liveSession the liveSession to delete
     * @param user the author of the live session
     */
    @org.springframework.transaction.annotation.Transactional
    @Requires({ liveSession && liveSession.note.author == user })
    def deleteLiveSessionByAuthor(LiveSession liveSession, User user) {

        //delete note tag and note grade for this live session explanation notes
        def liveResponse = LiveSessionResponse.where {
            liveSession == liveSession
        }
        List<Note> noteList = new ArrayList<Note>()
        def notes = liveResponse.findAll()
        notes.each {
            if(it.explanationId != null){
                def theNote = Note.findById(it.explanationId)
                // delete note tag part
                if(NoteTag.findAllByNote(theNote)){
                    def noteTagList = NoteTag.findAllByNote(theNote)
                    noteTagList.each {
                        it.delete()
                    }
                }
                // delete note grade part
                if(NoteGrade.findAllByNote(theNote)){
                    def noteGradeList = NoteGrade.findAllByNote(theNote)
                    noteGradeList.each {
                        it.delete()
                    }
                }
                noteList.add(theNote)
            }
        }

        // delete live sessions responses if any
        def query = LiveSessionResponse.where {
            liveSession == liveSession
        }
        query.deleteAll()
        // delete session phases if any
        def query2 = SessionPhase.where {
            liveSession == liveSession
        }
        query2.deleteAll()
        // delete the live session
        liveSession.delete()

        // finally delete all the explanation notes
        noteList.each {
            it.delete()
        }
    }

    /**
     * Create a first phase for a live session
     * @param user the user of the session
     * @param liveSession the live session
     * @return the first phase
     */
    @Requires({user == liveSession.note.author && !liveSession.hasStartedSessionPhase()})
    SessionPhase createAndStartFirstSessionPhaseForLiveSession(User user, LiveSession liveSession) {
        if (liveSession.isNotStarted()) {
            liveSession.start()
        }
        createSessionPhaseForLiveSessionWithRank(liveSession,1).start()
    }

    /**
     * Create a third phase for a live session
     * @param user the user of the session
     * @param liveSession the live session
     * @return the third phase
     */
    @Requires({user == liveSession.note.author && !liveSession.hasStartedSessionPhase()})
    SessionPhase createAndStartSessionPhaseForLiveSessionWithRank(User user, LiveSession liveSession,Integer rank) {
        createSessionPhaseForLiveSessionWithRank(liveSession,rank).start()
    }

    private SessionPhase createSessionPhaseForLiveSessionWithRank(LiveSession liveSession, Integer rank) {
        SessionPhase sessionPhase = new SessionPhase(liveSession: liveSession, rank: rank)
        sessionPhase.save(flush: true)
        sessionPhase
    }

    /**
     * Create a live session response for a given session phase and  a given user
     * @param session phase the given live session
     * @param user the given user
     * @param value the text value of the response
     * @param explanation the given explanation for the given response
     * @param confidenceDegree the confidence degree given by the answerer
     * @return the live session response
     */
    @Requires({ sessionPhase.isStarted() && !sessionPhase.isStopped() && !sessionPhase.getResponseForUser(user) })
    LiveSessionResponse createResponseForSessionPhaseAndUser(SessionPhase sessionPhase,User user, String value,
                                                             String explanation, Integer confidenceDegree) {
        LiveSession liveSession = sessionPhase.liveSession
        LiveSessionResponse liveSessionResponse = new LiveSessionResponse(
                liveSession:liveSession,
                sessionPhase: sessionPhase,
                user: user,
                answerListAsString: value,
                confidenceDegree: confidenceDegree
        )
        Note note = liveSession.note
        if (explanation) {
            liveSessionResponse.explanation = noteService.addNote(user,explanation,note.context,note.fragmentTag,note,NoteKind.EXPLANATION)
        }
        liveSessionResponse.save()
        liveSessionResponse
    }

    /**
     * Close an N phases submit live session
     * @param liveSession the live session to close
     */
    @NotTransactional
    def closeNPhaseSubmitLiveSession(LiveSession liveSession) {
        // stop all phases
        def phases = SessionPhase.findAllByLiveSession(liveSession)
        phases.each { SessionPhase phase ->
            if (!phase.isStopped()) {
                phase.stop(false)
            }
        }
        // stop liveSession
        if (!liveSession.isStopped()) {
            liveSession.stop(false)
        }
        // evaluate grades for all explanations
        SessionPhase secondPhase = liveSession.findSecondPhase()
        List<LiveSessionResponse> responseList = LiveSessionResponse.findAllBySessionPhase(secondPhase,[fetch: [explanation: 'join']])
        responseList.each { LiveSessionResponse response ->
            Note expl = response.explanation
            if (expl) {
                expl.updateMeanGrade()
            }
        }
    }

}
