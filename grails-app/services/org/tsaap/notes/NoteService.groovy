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

import grails.orm.PagedResultList
import org.gcontracts.annotations.Requires
import org.hibernate.criterion.CriteriaSpecification
import org.springframework.transaction.annotation.Transactional
import org.tsaap.attachement.AttachementService
import org.tsaap.directory.User
import org.tsaap.questions.LiveSession
import org.tsaap.questions.LiveSessionService
import org.tsaap.questions.impl.gift.GiftQuestionService

class NoteService {

    static transactional = false
    LiveSessionService liveSessionService
    AttachementService attachementService
    GiftQuestionService giftQuestionService

    /**
     * Add a new note
     * @param author the author
     * @param content the content
     * @param context the context
     * @param parentNote the parent note
     * @return the added note
     */
    @Transactional
    @Requires({ author && content && (!context || context.isOpen()) && parentNote?.noteKind != NoteKind.QUESTION})
    Note addNote(User author,
                 String content,
                 Context context = null,
                 Tag fragmentTag = null,
                 Note parentNote = null,
                 NoteKind noteKind = NoteKind.STANDARD) {

        // create the note
        Note theNote = new Note(author: author,
                content: content,
                context: context,
                fragmentTag: fragmentTag,
                parentNote: parentNote,
                kind: noteKind.ordinal()
        )

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
     * add a question note
     * @param author the author
     * @param content the content
     * @param context the context
     * @param fragmentTag the fragment tag
     * @param parentNote the parent note
     * @return the added question
     */
    @Transactional
    @Requires({ author && content })
    Note addQuestion(User author,
                     String content,
                     Context context = null,
                     Tag fragmentTag = null,
                     Note parentNote = null) {
        Note theNote
        try {
            if (giftQuestionService.getQuestionFromGiftText(content)) {
                theNote = addNote(author, content, context, fragmentTag, parentNote, NoteKind.QUESTION)
            }
        }
        catch (Exception e) {
            throw new IsNotQuestionException("notes.edit.question.error")
        }
        theNote
    }

    /**
     * add a standard note
     * @param author the author
     * @param content the content
     * @param context the context
     * @param fragmentTag the fragment tag
     * @param parentNote the parent note
     * @return the added standard note
     */
    @Transactional
    @Requires({ author && content })
    Note addStandardNote(User author,
                         String content,
                         Context context = null,
                         Tag fragmentTag = null,
                         Note parentNote = null) {
        Note theNote
        if (content?.startsWith("::")) {
            throw new IsNotStandardNoteException("notes.edit.note.error")
        } else {
            theNote = addNote(author, content, context, fragmentTag, parentNote, NoteKind.STANDARD)
        }
        theNote
    }

    /**
     * Bookmark a note by a user
     * @param theNote the note to bookmarked
     * @param theUser the bookmarker
     * @return the bookmark
     */
    @Requires({ theNote && theUser })
    Bookmark bookmarkNotebyUser(Note theNote, User theUser) {
        Bookmark bookmark = new Bookmark(user: theUser, note: theNote)
        // for performance issues the bookmarks bag coming from the to many relationship
        // is not set
        bookmark.save(failOnError: true)
    }

    /**
     * Unbookmark a note by a given user
     * @param note the note
     * @param user the user
     */
    @Requires({ note && user })
    def unbookmarkedNoteByUser(Note note, User user) {
        Bookmark bookmark = Bookmark.findByNoteAndUser(note, user)
        if (bookmark) {
            note.removeFromBookmarks(bookmark)
            bookmark.delete(flush: true)
        }
    }

    /**
     * mark as liked note a note by a user
     * @param theNote the note to bookmarked
     * @param theUser the bookmarker
     * @return the bookmark
     */
    @Requires({ theNote && theUser })
    @Transactional
    def scoreNotebyUser(Note theNote, User theUser) {
        Score score = new Score(user: theUser, note: theNote)
        // for performance issues the bookmarks bag coming from the to many relationship
        // is not set
        theNote.incrementScore()
        score.save(failOnError: true)
    }

    /**
     * mark as liked note a note by a user
     * @param theNote the note to bookmarked
     * @param theUser the bookmarker
     * @return the bookmark
     */
    @Requires({ theNote && theUser })
    @Transactional
    def gradeNotebyUser(Note theNote, User theUser, Double theGrade) {

        NoteGrade grade = NoteGrade.findOrCreateByNoteAndUser(theNote, theUser)
        grade.grade = theGrade
        grade.save(failOnError: true)
    }

    /**
     * Delete a note
     * @param note the note to delete
     * @param user the author of the note
     */
    @Transactional
    @Requires({ note && (note.author == user || user == note?.context?.owner) })
    def deleteNoteByAuthor(Note note, User user) {
        // detach attachment if needed
        if (note.attachment) {
            attachementService.detachAttachement(note.attachment)
        }

        // set parentNote fields
        def query = Note.where {
            parentNote == note
        }
        query.updateAll(parentNote: null)
        // delete tags if any
        query = NoteTag.where {
            note == note
        }
        query.deleteAll()
        // delete mentions if any
        query = NoteMention.where {
            note == note
        }
        query.deleteAll()
        // delete bookmarks if any
        query = Bookmark.where {
            note == note
        }
        query.deleteAll()

        //delete score if any
        query = Score.where {
            note == note
        }
        query.deleteAll()

        // delete live sessions if any
        query = LiveSession.where {
            note == note
        }

        query.findAll { liveSessionService.deleteLiveSessionByAuthor(it, user) }
        query.deleteAll()
        // finally delete notes
        note.delete()
    }

    /**
     * Count notes for the given search criteria
     * @param inUser the user performing the search
     * @param userNotes indicates if the search must find the notes the user is owner
     * @param userFavorites indicates if the search must find the user favorite notes
     * @param all indicates if the search must find all notes for a given context
     * @param inContext the given context
     * @return the number of notes corresponding to the search
     */
    def countNotes(User inUser,
                   Boolean userNotes = true,
                   Boolean userFavorites = false,
                   Boolean all = false,
                   Context inContext = null,
                   Tag inFragmentTag = null,
                   String kindParam) {
        List kindList = null
        if (kindParam != 'question') {
            kindList = [NoteKind.STANDARD.ordinal()]
        } else {
            kindList = [NoteKind.QUESTION.ordinal()]
        }
        if (!(userNotes || userFavorites || all)) {
            return 0
        }
        if (!inContext) { // all is not relevant when there is no context
            all = false
        }
        if (all && !inFragmentTag) { // we have a context and user want all notes on the context
            return Note.countByContextAndKindInList(
                    inContext, kindList)
        }
        if (all && inFragmentTag) {
            return Note.countByContextAndFragmentTagAndKindInList(
                    inContext,
                    inFragmentTag,
                    kindList)
        }
        // if not all, we use a criteria
        def criteria = Note.createCriteria()
        def countList = criteria.list() {
            projections {
                count()
            }
            createAlias('bookmarks', 'bmks', CriteriaSpecification.LEFT_JOIN)
            if (inContext) { // if there is a context
                eq 'context', inContext
                if (inFragmentTag) { // if there is a context and a fragmentTag
                    eq 'fragmentTag', inFragmentTag
                }
            }
            // we know that one of the two filters is active
            or {
                if (userNotes) {
                    eq 'author', inUser
                }
                if (userFavorites) {
                    eq 'bmks.user', inUser
                }
            }
            inList 'kind', kindList
        }
        countList[0];
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
    def findAllNotes(User inUser,
                     Boolean userNotes = true,
                     Boolean userFavorites = false,
                     Boolean all = false,
                     Context inContext = null,
                     Tag inFragmentTag = null,
                     Map paginationAndSorting = [sort: 'dateCreated', order: 'desc'],
                     String kindParam,
                     String inlineParam) {
        List kindList = null
        List fragList = null
        def noPagination = [sort: 'dateCreated', order: 'desc']
        if (kindParam != 'question') {
            kindList = [NoteKind.STANDARD.ordinal()]
        } else {
            kindList = [NoteKind.QUESTION.ordinal()]
        }
        if (!(userNotes || userFavorites || all)) {
            return new DefaultPagedResultList(list: [], totalCount: 0)
        }
        if (!inContext) { // all is not relevant when there is no context
            all = false
        }
        if (all && !inFragmentTag) { // we have a context and user want all notes on the context
            return new DefaultPagedResultList(list: Note.findAllByContextAndKindInList(
                    inContext,
                    kindList,
                    paginationAndSorting),
                    totalCount: Note.countByContextAndKindInList(
                            inContext, kindList,
                            paginationAndSorting)
            )
        }
        if (all && inFragmentTag) {
            if (inlineParam == 'on' && kindParam != 'question') {
                def authorList = Note.findAllByContextAndFragmentTagAndAuthorAndKindInList(
                        inContext,
                        inFragmentTag,
                        inUser,
                        kindList,
                        noPagination
                )
                def othersList = Note.findAllByContextAndFragmentTagAndAuthorNotEqualAndKindInList(
                        inContext,
                        inFragmentTag,
                        inUser,
                        kindList,
                        noPagination
                )
                fragList = authorList + othersList
            } else {
                fragList = Note.findAllByContextAndFragmentTagAndKindInList(
                        inContext,
                        inFragmentTag,
                        kindList,
                        paginationAndSorting
                )
            }
            return new DefaultPagedResultList(list: fragList,
                    totalCount: Note.countByContextAndFragmentTagAndKindInList(
                            inContext,
                            inFragmentTag,
                            kindList,
                            paginationAndSorting))
        }
        // if not all, we use a criteria
        def criteria = Note.createCriteria()
        PagedResultList results = criteria.list(paginationAndSorting) {
            createAlias('bookmarks', 'bmks', CriteriaSpecification.LEFT_JOIN)
            if (inContext) { // if there is a context
                eq 'context', inContext
                if (inFragmentTag) { // if there is a context and a fragmentTag
                    eq 'fragmentTag', inFragmentTag
                }
            }
            // we know that one of the two filters is active
            or {
                if (userNotes) {
                    eq 'author', inUser
                }
                if (userFavorites) {
                    eq 'bmks.user', inUser
                }
            }
            inList 'kind', kindList
            order paginationAndSorting.sort, paginationAndSorting.order
        }
        new DefaultPagedResultList(list: results.list, totalCount: results.totalCount)
    }

    /**
     * Duplicate a note in a context
     * @param note the note to duplicate
     * @param targetContext the target context
     * @param user the user triggering the duplication
     * @return the new note
     */
    @Requires({ note && note.author == user })
    Note duplicateNoteInContext(Note note, Context targetContext, User user) {
        addNote(user, note.content, targetContext, note.fragmentTag, null, note.noteKind)
    }

    /**
     * Find all questions for a context
     * @param context the context
     * @return the list of questions
     */
    List<Note> findAllNotesAsQuestionForContext(Context context) {
        Note.findAllByContext(context).findAll {
            it.isAQuestion()
        }
    }
}

/**
 * Custom paged result list*/
class DefaultPagedResultList {

    List list
    Long totalCount

}


