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

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.directory.User

class NotesController {

  SpringSecurityService springSecurityService
  NoteService noteService
  ContextService contextService

  /**
   *
   * @return
   */
  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def index() {
    User user = springSecurityService.currentUser
    Context context = null
    if (params.contextId) {
      context = Context.get(params.contextId)
    } else if (params.contextName) {
      context = Context.findByContextName(params.contextName, [cache: true])
      if (context == null && params.createContext == 'true') {
        User contextOwner = params.contextOwner ? User.findByUsername(params.contextOwner) : user
        context = contextService.addContext(new Context(contextName: params.contextName,
                                                        owner: contextOwner,
                                                        url: params.url,
                                                        descriptionAsNote: params.desc))
      }
    }
    def displaysMyNotes = true
    def displaysMyFavorites = false
    def displaysAll = false
    if (!params.displaysMyNotes) {
      displaysMyNotes = false
    }
    if (params.displaysMyFavorites) {
      displaysMyFavorites = true
    }
    if (params.displaysAll) {
      displaysAll = true
    }
    params.max = Math.min(params.max as Long ?: 7, 20)
    def paginationAndSorting = [sort: 'dateCreated', order: 'desc', max: params.max]
    if (params.offset) {
      paginationAndSorting.offset = params.offset
    }
    def notes = noteService.findAllNotes(user,
                                         displaysMyNotes,
                                         displaysMyFavorites,
                                         displaysAll,
                                         context,
                                         paginationAndSorting)
    render(view: '/notes/index', model: [user: user,
            notes: notes,
            displaysMyNotes: displaysMyNotes,
            displaysMyFavorites: displaysMyFavorites,
            displaysAll: displaysAll,
            context: context])
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def addNote() {
    def user = springSecurityService.currentUser
    def noteContent = params.noteContent
    Context context = null
    if (params.contextId) {
      context = Context.get(params.contextId)
    }
    Note parentNote = null
    if (params.parentNoteId) {
      parentNote = Note.get(params.parentNoteId)
      if (!context) {
        context = parentNote.context
      }
    }
    Note note = noteService.addNote(user, noteContent, context, parentNote)
    if (note.hasErrors()) {
      def notes = noteService.findAllNotes(user, true, false, false, context)
      render(view: '/notes/index', model: [user: user, editedNote: note, notes: notes])
    } else {
      params.remove('noteContent')
      redirect(action: index(), params: params)
    }
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def bookmarkNote() {
    def user = springSecurityService.currentUser
    Note note = Note.get(params.noteId)
    noteService.bookmarkNotebyUser(note, user)
    params.remove('noteId')
    redirect(action: index(), params: params)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def unbookmarkNote() {
    def user = springSecurityService.currentUser
    Note note = Note.get(params.noteId)
    noteService.unbookmarkedNoteByUser(note, user)
    params.remove('noteId')
    redirect(action: index(), params: params)
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def deleteNote() {
    def user = springSecurityService.currentUser
    Note note = Note.get(params.noteId)
    noteService.deleteNoteByAuthor(note, user)
    params.remove('noteId')
    redirect(action: index(), params: params)
  }

}
