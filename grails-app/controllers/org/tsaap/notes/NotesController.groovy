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
    if (params.contextName) {
      context = Context.findByContextName(params.contextName, [cache:true])
      if (context == null && params.createContext == 'true') {
        User contextOwner = params.contextOwner ? User.findByUsername(params.contextOwner) : user
        context = contextService.addContext(new Context(contextName: params.contextName,
                                                        owner: contextOwner,
                                                        url: params.url,
                                                        descriptionAsNote: params.desc))
      }
    }
    def notes = noteService.findAllNotes(user,true,false, false,context)
    render(view: '/notes/index', model: [user: user, notes: notes, context:context])
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def addNote() {
    def user = springSecurityService.currentUser
    def noteContent = params.noteContent
    Context context = null
    if (params.contextId)  {
      context = Context.get(params.contextId)
    }
    Note note = noteService.addNote(user, noteContent,context)
    if (note.hasErrors()) {
      def notes = noteService.findAllNotes(user,true,false, false,context)
      render(view: '/notes/index', model: [user: user, note:note, notes: notes])
    } else {
      params.remove('noteContent')
      params.remove('contextId')
      if (context) {
        params.contextName = context.contextName
      }
      redirect(action: index(), params: params)
    }
  }
}
