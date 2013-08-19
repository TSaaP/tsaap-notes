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

import org.hibernate.criterion.CriteriaSpecification
import org.springframework.transaction.annotation.Transactional
import org.tsaap.directory.User

class NoteService {

  static transactional = false

  /**
   * Add a new note
   * @param author the author
   * @param content the content
   * @param context the context
   * @param parentNote the parent note
   * @return the added note
   */
  @Transactional
  Note addNote(User author,
               String content,
               Context context = null,
               Note parentNote = null) {

    // create the note
    Note theNote = new Note(author: author,
                            content: content,
                            context: context,
                            parentNote: parentNote)
    // save the note
    theNote.save()

    // manage tags & mentions

    def tags = NoteHelper.tagsFromContent(content)
    def mentions = NoteHelper.mentionsFromContent(content)

    def contentFromContext = context?.descriptionAsNote
    if (contentFromContext) {
      tags += NoteHelper.tagsFromContent(contentFromContext)
      mentions += NoteHelper.mentionsFromContent(contentFromContext)
    }

    tags.each {
      Tag tag = Tag.findOrSaveWhere(name: it)
      new NoteTag(note: theNote, tag: tag).save()
    }

    mentions.each {
      User user = User.findByUsername(it)
      if (user) {
        new NoteMention(note: theNote, mention: user).save()
      }
    }

    // return the note
    theNote
  }

  /**
   * Bookmark a note by a user
   * @param theNote the note to bookmarked
   * @param theUser the bookmarker
   * @return the bookmark
   */
  Bookmark bookmarkNotebyUser(Note theNote, User theUser) {
    Bookmark bookmark = new Bookmark(user: theUser, note: theNote)
    bookmark.save(failOnError: true)
  }

  /**
   * Unbookmark a note by a given user
   * @param note the note
   * @param user the user
   */
  def unbookmarkedNoteByUser(Note note, User user) {
    Bookmark bookmark = Bookmark.findByNoteAndUser(note, user)
    if (bookmark) {
      note.removeFromBookmarks(bookmark)
      bookmark.delete()
    }
  }

  /**
   * Find all notes for the given search criteria
   * @param inUser the user performing the search
   * @param userNotes indicates if the search must find the notes the user is owner
   * @param userFavorites indicates if the search must find the user favorite notes
   * @param all indicates if the search must find all notes for a given context
   * @param inContext the given context
   * @param paginationAndSorting specification of pagination and sorting
   * @return the notes corresponding to the search
   */
  List<Note> findAllNotes(User inUser,
                          Boolean userNotes = true,
                          Boolean userFavorites = false,
                          Boolean all = false,
                          Context inContext = null,
                          Map paginationAndSorting = [sort: 'dateCreated', order: 'desc']) {
    if (!(userNotes || userFavorites || all)) {
      return []
    }
    if (!inContext) { // all is not relevant when there is no context
      all = false
    }
    def criteria = Note.createCriteria()
    def results = criteria.list(paginationAndSorting) {
      createAlias('bookmarks', 'bmks', CriteriaSpecification.LEFT_JOIN)
      if (inContext) { // if there is a context
        eq 'context', inContext
        if (!all) { // we know that one of the two filters is active
          or {
            if (userNotes) {
              eq 'author', inUser
            }
            if (userFavorites) {
              eq 'bmks.user', inUser
            }
          }
        }
      } else { // if there is no context
        or { // we know that one of the two filters is active
          if (userNotes) {
            eq 'author', inUser
          }
          if (userFavorites) {
            eq 'bmks.user', inUser
          }
        }
      }
      order paginationAndSorting.sort, paginationAndSorting.order
    }
  }



}



