/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    GiftQuestionService giftQuestionService
    Question question
    LiveSession liveSession

    static hasMany = [bookmarks: Bookmark]

    static constraints = {
        context nullable: true
        fragmentTag nullable: true
        parentNote nullable: true
        bookmarks nullable: true
        grade nullable: true
    }

    static mapping = {
        version false
    }

    static transients = ['noteUrl', 'question', 'giftQuestionService', 'liveSession', 'activeLiveSession','attachment','noteKind']

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
        save(failOnError: true)
    }

    boolean hasBeenAlreadyEvaluatedByUser(User user) {
        NoteGrade.findByNoteAndUser(this, user)
    }

    int evaluationCount() {
        NoteGrade.countByNote(this)
    }
}

enum NoteKind {
    STANDARD,
    QUESTION,
    EXPLANATION,
    INVALID_QUESTION
}