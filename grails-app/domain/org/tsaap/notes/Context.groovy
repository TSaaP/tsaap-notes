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

  static constraints = {
    contextName blank: false, maxSize: 1024
    url url: true, nullable: true
    descriptionAsNote nullable: true, maxSize: 280
    source nullable: true, editable: false
    closed nullable: true
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


  static mapping = {
    version(false)
  }
}
