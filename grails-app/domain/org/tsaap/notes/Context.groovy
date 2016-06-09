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

import org.gcontracts.annotations.Requires
import org.tsaap.directory.User

/**
 * The context describes the context which the learner take notes in.
 * Typically the context references a resource and a description that allows
 * the capture of tags and mentions for all notes taken in the context
 * */
class Context {

    static transients = ['hasNotes']

    Date dateCreated
    Date lastUpdated

    String contextName
    String url
    String source

    /**
     * The owner is most probably the teacher in a learning context
     **/
    User owner
    Boolean ownerIsTeacher = true

    /**
     * The description note allows the description of the context with tags and
     * mentions following the way they are used in a note (# or @ prefix).
     * All mentions and tags on the context will be automatically bind to each
     * note taken by a user on this resource
     **/
    String descriptionAsNote

    Boolean noteTakingEnabled = false
    Boolean closed = false
    Boolean removed = false

    static constraints = {
        contextName blank: false, maxSize: 1024
        url url: true, nullable: true
        descriptionAsNote nullable: true, maxSize: 280
        source nullable: true, editable: false
        closed nullable: true
        removed nullable: true
    }

    /**
     * Check if the current context has notes
     * @return true if the current context has notes
     */
    Boolean hasNotes() {
        Note.countByContext(this)
    }
    /**
     * Check if the current context has notes
     * Of kind Standard
     * @return true if the current context has standard notes
     */

    Boolean hasStandardNotes() {
        Note.countByContextAndKind(this, NoteKind.STANDARD.ordinal())
    }
    /**
     * Check if the current context has new notes since yesterday
     * @return true if the current context has notes
     */
    Integer newNotesCountSinceYesterday() {
        def today = new Date()
        Note.countByContextAndDateCreatedBetween(this, today - 1, today)
    }

    /**
     * Check if a context is followed by a user
     * @param user
     * @return
     */
    @Requires({ user })
    Boolean isFollowedByUser(User user) {
        if (owner == user) {
            return true
        }
        ContextFollower contextFollower = ContextFollower.findByFollowerAndContext(user, this)
        contextFollower && !contextFollower.isNoMoreSubscribed
    }

    /**
     * Check if the current context is open
     * @return true if the context is open false otherwise
     */
    Boolean isOpen() {
        closed == false
    }

    /**
     * Check if the current context is closed
     * @return true if the context is closed false otherwise
     */
    Boolean isClosed() {
        closed == true
    }
    /**
     * check if the current context is removed
     * @return true if the context is removed false otherwise
     */
    Boolean isRemoved() {
        removed == true
    }


    static mapping = {
        version(false)
    }
}
