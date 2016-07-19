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

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.attachement.AttachementService
import org.tsaap.directory.User
import org.tsaap.questions.impl.gift.GiftQuestionService

class NotesController {

    SpringSecurityService springSecurityService
    NoteService noteService
    GiftQuestionService giftQuestionService
    AttachementService attachementService
    ContextService contextService


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index() {
        User user = springSecurityService.currentUser
        renderMainPage(params, user)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def add() {
        def user = springSecurityService.currentUser
        def noteContent = params.noteContent
        Context context = null
        if (params.contextId) {
            context = Context.get(params.contextId)
        }
        Tag fragmentTag = null
        if (params.fragmentTagId) {
            fragmentTag = Tag.get(params.fragmentTagId)
        } else if (params.fragmentTagName) {
            fragmentTag = Tag.findOrSaveWhere(name: params.fragmentTagName.toLowerCase())
        }
        Note parentNote = null
        if (params.parentNoteId) {
            parentNote = Note.get(params.parentNoteId)
            if (!context) {
                context = parentNote.context
            }
            if (!fragmentTag) {
                fragmentTag = parentNote.fragmentTag
            }
        }
        Note myNote
        try {
            if (controllerName == 'questions') {
                myNote = noteService.addQuestion(user, noteContent, context, fragmentTag, parentNote)
                flash.message = message(code: 'notes.edit.add.question.success')
            } else {
                myNote = noteService.addStandardNote(user, noteContent, context, fragmentTag, parentNote)
            }
            def file = request.getFile('myFile')
            if (file && !file.isEmpty()) {
                attachementService.addFileToNote(file, myNote)
            }
        } catch (IsNotQuestionException e) {
            params.put("error", "question")
        } catch (IsNotStandardNoteException e) {
            params.put("error", "note")
        }

        params.remove('noteContent')
        redirect(action: index(), params: params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def bookmark() {
        def user = springSecurityService.currentUser
        Note note = Note.get(params.noteId)
        noteService.bookmarkNotebyUser(note, user)
        params.remove('noteId')
        redirect(action: index(), params: params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unbookmark() {
        def user = springSecurityService.currentUser
        Note note = Note.get(params.noteId)
        noteService.unbookmarkedNoteByUser(note, user)
        params.remove('noteId')
        redirect(action: index(), params: params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def markAsLiked() {
        def user = springSecurityService.currentUser
        Note note = Note.get(params.noteId)
        noteService.scoreNotebyUser(note, user)
        params.remove('noteId')
        redirect(action: index(), params: params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def delete() {
        def user = springSecurityService.currentUser
        Note note = Note.load(params.noteId)
        noteService.deleteNoteByAuthor(note, user)
        params.remove('noteId')
        redirect(action: index(), params: params)
    }

    /**
     * Update the specified note or question and redirect to note or question list
     * Show errors if any
     * @return
     */
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update() {
        Note note = Note.findById(params.noteId as Long)
        try {
            if (controllerName == 'questions') {
                noteService.updateQuestionById(note, params.noteContent, note.author)
            } else {
                noteService.updateNoteById(note, params.noteContent, note.author)
            }
            def file = request.getFile('myFile')
            if (file && !file.isEmpty()) {
                attachementService.addFileToNote(file, note)
            }
        } catch (IsNotQuestionException e) {
            params.put("error", "question")
        } catch (IsNotStandardNoteException e) {
            params.put("error", "note")
        }
        redirect(action: index(), params: params)
    }

    /**
     * Remove attachment and put the browse file input instead
     * @return
     */
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def removeAttachement() {
        Note note = Note.findById(params.noteId as Long)
        if (note.attachment && note.author == springSecurityService.currentUser) {
            attachementService.detachAttachement(note.attachment)
        }

        render """<input type="file" name="myFile" title="Image: gif, jpeg and png only"
       style="margin-top: 5px"/>"""
    }


    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def showDiscussion() {
        def user = springSecurityService.currentUser
        Long noteId = params.noteId as Long
        Map showDiscussion = [:]
        showDiscussion[noteId] = true
        params.remove('noteId')
        renderMainPage(params, user, showDiscussion)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def hideDiscussion() {
        def user = springSecurityService.currentUser
        Map showDiscussion = [:]
        renderMainPage(params, user, showDiscussion)
    }

    /**
     * Render the main page given the params and the user
     * @param params the params
     * @param user the user
     * @return the result of the render operation
     */
    private def renderMainPage(def params, User user, Map showDiscussion = [:]) {
        Context context = null
        if (params.contextId && params.contextId != 'null') {
            context = Context.get(params.contextId as Long)
            if (!contextService.contextExists(context)) {
                response.sendError(404)
                return
            }
        }
        Tag fragmentTag = null
        if (params.fragmentTagId && params.fragmentTagId != 'null') {
            fragmentTag = Tag.get(params.fragmentTagId)
        } else if (params.fragmentTagName && params.fragmentTagName != 'null') {
            fragmentTag = Tag.findOrSaveWhere(name: params.fragmentTagName.toLowerCase())
            if (fragmentTag != null) {
                params.fragmentTagId = fragmentTag.id
            }
        }
        def displaysMyNotes = true
        def displaysMyFavorites = false
        def displaysAll = false
        if (params.displaysMyNotes != 'on') {
            displaysMyNotes = false
        }
        if (params.displaysMyFavorites == 'on') {
            displaysMyFavorites = true
        }
        if (params.displaysAll == 'on') {
            displaysAll = true
        }

        def paginationAndSorting
        if (controllerName == 'questions') {
            paginationAndSorting = [sort: 'rank', order: 'asc']
        } else {
            params.max = Math.min(params.max as Long ?: 5, 20)
            paginationAndSorting = [sort: 'dateCreated', order: 'desc', max: params.max]
            if (params.offset) {
                paginationAndSorting.offset = params.offset
            }
        }

        def kind = controllerName == 'questions' ? 'question' : 'standard'

        def inlineParams = params.inline
        def notes = noteService.findAllNotes(user,
                displaysMyNotes,
                displaysMyFavorites,
                displaysAll,
                context,
                fragmentTag,
                paginationAndSorting,
                kind,
                inlineParams)

        def otherKind
        if (controllerName == 'questions') {
            otherKind = 'standard'
        } else {
            otherKind = 'question'
        }

        def countTotal
        countTotal = noteService.countNotes(user,
                displaysMyNotes,
                displaysMyFavorites,
                displaysAll,
                context,
                fragmentTag,
                otherKind
        )

        /* Set isFirstQuestionInContext or isLastQuestionInContext flag on questions to know if we can't move them up or down */
        if (controllerName == 'questions') {
            if (notes.totalCount > 0) {
                /*
                If we use pagination for questions again we can use this code
                if (!paginationAndSorting.offset || paginationAndSorting.offset.toLong() == 0) {
                    notes.list.first().isFirstQuestionInContext = true
                }
                if (paginationAndSorting?.offset?.toLong() >= notes.totalCount - paginationAndSorting.max) {
                    notes.list.last().isLastQuestionInContext = true
                }
                */
                notes.list.first().isFirstQuestionInContext = true
                notes.list.last().isLastQuestionInContext = true
            }
        }

        if (controllerName == 'questions') {
            render view: '/questions/index', model: [user          : user,
                                                     notes         : notes,
                                                     countTotal    : countTotal,
                                                     context       : context,
                                                     fragmentTag   : fragmentTag,
                                                     showDiscussion: showDiscussion]
        } else {
            render view: '/notes/index', model: [user          : user,
                                                 notes         : notes,
                                                 countTotal    : countTotal,
                                                 context       : context,
                                                 fragmentTag   : fragmentTag,
                                                 showDiscussion: showDiscussion]
        }

    }
}
