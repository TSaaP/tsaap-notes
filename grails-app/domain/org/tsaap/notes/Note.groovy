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

import org.tsaap.directory.User
import org.tsaap.questions.LiveSession
import org.tsaap.questions.Question
import org.tsaap.questions.impl.gift.GiftQuestionService

class Note {
    Date dateCreated
    User author
    Context context
    Tag fragmentTag
    Note parentNote

    String content

    static hasMany = [bookmarks: Bookmark]

    static constraints = {
        context nullable: true
        fragmentTag nullable: true
        parentNote nullable: true
        bookmarks nullable: true
        content maxSize: 280
    }

    static mapping = {
        version false
    }

    static transients = ['noteUrl', 'question', 'giftQuestionService','liveSession','activeLiveSession']

    /**
     * Indicate if the current note is bookmarked by the given user
     * @param user the user
     * @return true if the note is bookmarked, false if not
     */
    boolean isBookmarkedByUser(User user) {
        Bookmark.findByNoteAndUser(this, user)
    }

    /**
     * Indicate if a note has a parent note
     * @return true if the note has a parent, false if not
     */
    boolean hasParent() {
        parentNote
    }

    GiftQuestionService giftQuestionService

    /**
     * Indicate if a note is an interactive question
     * @return true if the note is an interactive question
     */
    boolean isAQuestion() {
        this.getQuestion() != null
    }

    Question question

    /**
     * Get the question corresponding to the note or null
     * @return the corresponding question or null
     */
    Question getQuestion() {
        if (content?.startsWith("::") && !question) {
            try {
                question = giftQuestionService.getQuestionFromGiftText(content)
            } catch (Exception e) {
                log.error(e.message)
            }
        }
        question
    }

    LiveSession liveSession

    /**
     * Get the last live session for the current note if it is a question
     * @return the last live session if it exists
     */
    LiveSession getLiveSession() {
        if (liveSession && !liveSession.stopped) {
            return liveSession
        }
        if (isAQuestion()) {
           liveSession = LiveSession.findByNote(this,[sort: "dateCreated", order: "desc"])
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
}
