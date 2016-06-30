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
import org.tsaap.questions.Question
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
    def addNote() {
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
            if (params.kind && params.kind == 'question') {
                myNote = noteService.addQuestion(user, noteContent, context, fragmentTag, parentNote)
            } else {
                myNote = noteService.addStandardNote(user, noteContent, context, fragmentTag, parentNote)
            }
        } catch (IsNotQuestionException e) {
            params.put("error", "question")
            renderMainPage(params, user)
            return
        } catch (IsNotStandardNoteException e) {
            params.put("error", "note")
            renderMainPage(params, user)
            return
        }

        def file = request.getFile('myFile')
        if (file && !file.isEmpty()) {
            attachementService.addFileToNote(file, myNote)
        }


        params.remove('noteContent')
        redirect(action: index(), params: params)
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
    def markAsLikedNote() {
        def user = springSecurityService.currentUser
        Note note = Note.get(params.noteId)
        noteService.scoreNotebyUser(note, user)
        params.remove('noteId')
        redirect(action: index(), params: params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def deleteNote() {
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
    def updateNote() {
        def user = springSecurityService.currentUser
        Note note = Note.findById(params.noteId as Long)
        try {
            if (params?.kind == 'question') {
                noteService.updateQuestionById(params.noteId, params.noteContent)
            } else {
                noteService.updateNoteById(params.noteId, params.noteContent)
                def file = request.getFile('myFile')
                if (file && !file.isEmpty()) {
                    attachementService.addFileToNote(file, note)
                }
            }
        } catch (IsNotQuestionException e) {
            params.put("error", "question")
            renderMainPage(params, user)
            return
        } catch (IsNotStandardNoteException e) {
            params.put("error", "note")
            renderMainPage(params, user)
            return
        }
        redirect(action: index(), params: params)
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def removeAttachement() {
        def user = springSecurityService.currentUser
        Note note = Note.findById(params.noteId as Long)
        if (note.attachment) {
            attachementService.detachAttachement(note.attachment)
        }
        redirect(action: index(), params: params)
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

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def evaluateContentAsNote() {
        String noteInput = params.content
        if (noteInput?.startsWith('::')) {
            try {
                Question question = giftQuestionService.getQuestionFromGiftText(noteInput)
                render(template: '/questions/preview/detail', model: [question: question])
            } catch (Exception e) {
                render("${e.message}")
            }
        } else {
            render(noteInput ?: '')
        }
    }

    /**
     * Give the different question type sample and create link for popup window dedicate to questions samples
     * @return the popup window content
     */
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def getQuestionsSamples() {

        def content = "${message(code: "notes.edit.sampleQuestion.text")}"
        def single = "${message(code: "notes.edit.sampleQuestion.singleChoice")}"

        Question singleChoice = giftQuestionService.getQuestionFromGiftText("${message(code: "notes.edit.sampleQuestion.singleChoiceExemple")}")

        def singlelink = """<a class="sampleLink" id="singleQuestionSample" onClick="sampleLink(0)">${
            message(code: "notes.edit.sampleQuestion.link")
        }</a><br><br>"""
        def multiple = "${message(code: "notes.edit.sampleQuestion.multipleChoice")}"

        Question multipleChoice = giftQuestionService.getQuestionFromGiftText("${message(code: "notes.edit.sampleQuestion.multipleChoiceExemple")}")

        def multiplelink = """<a class="sampleLink" id="singleQuestionSample" onClick="sampleLink(1)">${
            message(code: "notes.edit.sampleQuestion.link")
        }</a>"""

        render(content)
        render(single)
        render(template: '/questions/preview/detail', model: [question: singleChoice])
        render(singlelink)
        render(multiple)
        render(template: '/questions/preview/detail', model: [question: multipleChoice])
        render(multiplelink)

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
        params.max = Math.min(params.max as Long ?: 5, 20)
        def paginationAndSorting = [sort: 'dateCreated', order: 'desc', max: params.max]
        if (params.offset) {
            paginationAndSorting.offset = params.offset
        }
        def kindParams = params.kind
        def inlineParams = params.inline
        def notes = noteService.findAllNotes(user,
                displaysMyNotes,
                displaysMyFavorites,
                displaysAll,
                context,
                fragmentTag,
                paginationAndSorting,
                kindParams,
                inlineParams)

        def kind
        if (kindParams == 'standard') {
            kind = NoteKind.QUESTION
        } else {
            kind = NoteKind.STANDARD
        }

        def countTotal
        countTotal = noteService.countNotes(user,
                displaysMyNotes,
                displaysMyFavorites,
                displaysAll,
                context,
                fragmentTag,
                kind.toString().toLowerCase()
        )

        render view: '/notes/index', model: [user          : user,
                                             notes         : notes,
                                             countTotal    : countTotal,
                                             context       : context,
                                             fragmentTag   : fragmentTag,
                                             showDiscussion: showDiscussion]
    }
}
