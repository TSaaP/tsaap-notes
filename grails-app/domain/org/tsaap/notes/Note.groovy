/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.notes

import org.tsaap.attachement.Attachement
import org.tsaap.directory.User
import org.tsaap.questions.LiveSession
import org.tsaap.questions.Question
import org.tsaap.questions.TextBlock
import org.tsaap.questions.impl.DefaultQuestion
import org.tsaap.questions.impl.gift.GiftQuestionService

class Note {

    Date dateCreated
    User author
    Context context
    Tag fragmentTag
    Note parentNote

    String content
    Integer score = 0
    Double grade

    Integer kind = NoteKind.STANDARD.ordinal()

    Integer rank

    GiftQuestionService giftQuestionService
    Question question
    LiveSession liveSession

    Boolean isFirstQuestionInContext
    Boolean isLastQuestionInContext

    static hasMany = [bookmarks: Bookmark]

    static constraints = {
        context nullable: true
        fragmentTag nullable: true
        parentNote nullable: true
        bookmarks nullable: true
        grade nullable: true
        rank nullable: true
    }

    static mapping = {
        version false
    }

    static transients = ['noteUrl', 'question', 'giftQuestionService', 'liveSession', 'activeLiveSession', 'attachment', 'noteKind', 'isFirstQuestionInContext', 'isLastQuestionInContext']

    private static final String QUESTION_DEFAULT_TITLE = "Question"
    private static final String QUESTION_INVALID_DEFAULT_TITLE = "question.format.error"

    /**
     * Get the note kind object
     * @return the note kind
     */
    NoteKind getNoteKind() {
        NoteKind.values()[kind]
    }

    /**
     * Get the attachment of the note if any
     * @return the attachment or null
     */
    Attachement getAttachment() {
        Attachement.findByNote(this)
    }

    /**
     * Indicate if the current note is bookmarked by the given user
     * @param user the user
     * @return true if the note is bookmarked, false if not
     */
    boolean isBookmarkedByUser(User user) {
        Bookmark.findByNoteAndUser(this, user)
    }

    /**
     * Indicate if the current note is scored by the given user
     * @param user the user
     * @return true if the note is scored, false if not
     */
    boolean isScoredByUser(User user) {
        Score.findByNoteAndUser(this, user)
    }

    /**
     * Indicate if a note has a parent note
     * @return true if the note has a parent, false if not
     */
    boolean hasParent() {
        parentNote
    }

    /**
     * Indicate if a note is an interactive question
     * @return true if the note is an interactive question
     */
    boolean isAQuestion() {
        kind == NoteKind.QUESTION.ordinal() || kind == NoteKind.INVALID_QUESTION.ordinal()
    }

    /**
     * Indicat if a note is an invalid interactive question
     * @return true if the note is an invalid question
     */
    boolean isAnInvalidQuestion() {
        kind == NoteKind.INVALID_QUESTION.ordinal()
    }

    /**
     * Get the question corresponding to the note or null
     * @return the corresponding question or null
     */
    Question getQuestion() {
        if (isAnInvalidQuestion() && !question) {
            question = invalidQuestion()
        } else if (!question) {
            try {
                question = giftQuestionService.getQuestionFromGiftText(content)
                if (question.title == null) {
                    question.title = QUESTION_DEFAULT_TITLE
                }
            } catch (Exception e) {
                log.error("""${e.message}
                ${content}
                """)
                question = invalidQuestion()
                kind = NoteKind.INVALID_QUESTION.ordinal()
                save(flush: true)
            }
        }
        question
    }

    /**
     * Create and return a question with invalid content
     * @return An object question corresponding to an invalid question
     */
    public DefaultQuestion invalidQuestion() {
        question = new DefaultQuestion(title: QUESTION_INVALID_DEFAULT_TITLE)
        question.addTextBlock(new TextBlock() {
            @Override
            String getText() {
                return content
            }
        })
        return question
    }

    /**
     * Get the last live session for the current note if it is a question
     * @return the last live session if it exists
     */
    LiveSession getLiveSession() {
        if (liveSession && !liveSession.stopped) {
            return liveSession
        }
        if (isAQuestion()) {
            liveSession = LiveSession.findByNote(this, [sort: "dateCreated", order: "desc"])
        }
        liveSession
    }

    /**
     * Get the current active live session if it exists
     * @return the current active live session or null
     */
    LiveSession getActiveLiveSession() {
        if (liveSession && !liveSession.stopped) {
            liveSession
        }
        null
    }

    /**
     *
     * @return the url the note is linked to
     */
    String getNoteUrl() {
        def rootUrl = context?.url
        if (!rootUrl) {
            return null
        }
        def hash = ""
        if (fragmentTag) {
            hash = "#$fragmentTag.name"
        }
        "${rootUrl}${hash}"
    }

    /**
     * Increment the score of the current note
     * @return the new score
     */
    Integer incrementScore() {
        score = score + 1
        score
    }

    /**
     * Evaluate the mean grade of a note
     * @return the mean grade of a note
     */
    Double evaluateTheMeanGrade() {
        def query = NoteGrade.where {
            note == this
        }.projections {
            avg('grade')
        }
        query.find() as Double
    }

    /**
     * Update the grade of the note
     * @return
     */
    def updateMeanGrade() {
        grade = evaluateTheMeanGrade()
        save(failOnError: true, flush: true)
    }

    Double findOrUpdateMeanGrade() {
        if (grade == null) {
            updateMeanGrade()
        }
        grade
    }

    boolean hasBeenAlreadyEvaluatedByUser(User user) {
        NoteGrade.findByNoteAndUser(this, user)
    }

    int evaluationCount() {
        NoteGrade.countByNote(this)
    }

    /**
     * Indicate if the note can be edited by user
     * The note can be a standard note, a question or an explanation
     * @param user the user asking for edition
     * @return true if it can be edited, false otherwise
     */
    boolean canBeEditedBy(User user) {
        (!context.isClosed()
                && user == author
                && kind != NoteKind.EXPLANATION.ordinal() // explanations can't be edited
                && kind != NoteKind.STANDARD.ordinal() || context.noteTakingEnabled // note -> noteTaking enabled
                && !isAQuestion() || !liveSession) // question -> not started
    }
}

enum NoteKind {
    STANDARD,
    QUESTION,
    EXPLANATION,
    INVALID_QUESTION
}