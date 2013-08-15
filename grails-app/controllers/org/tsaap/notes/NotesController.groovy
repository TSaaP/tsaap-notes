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

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def index() {
    User user = springSecurityService.currentUser
    render(view: '/notes/index', model: [user: user, notes: Note.findAllByAuthor(user)])
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def addNote() {
    def user = springSecurityService.currentUser
    def noteContent = params.noteContent
    Note note = noteService.addNote(user, noteContent)
    if (note.hasErrors()) {
      render(view: '/notes/index', model: [user: user, note:note, notes: Note.findAllByAuthor(user)])
    } else {
      params.noteContent = null
      redirect(action: index(), params: params)
    }
  }
}
