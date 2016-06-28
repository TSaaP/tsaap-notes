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

import org.gcontracts.PreconditionViolation
import org.tsaap.BootstrapTestService
import org.tsaap.attachement.Attachement
import org.tsaap.attachement.AttachementDto
import org.tsaap.attachement.AttachementService
import org.tsaap.directory.User
import spock.lang.Specification

class NoteServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    NoteService noteService
    ContextService contextService
    AttachementService attachementService

    def setup() {
        bootstrapTestService.initializeTests()
    }

    def "add notes"() {

        when: "adding a note"
        Note note = noteService.addNote(bootstrapTestService.learnerPaul, content)

        then: "the note exists and mentions and tags are created when needed"
        note != null
        note.content == content
        // tags
        def noteTags = NoteTag.findAllByNote(note)
        noteTags.size() == tags.size()
        if (noteTags.size() > 0) {
            def tagsFromNote = noteTags*.tag*.name
            tagsFromNote == tags
        }
        // mentions
        def noteMentions = NoteMention.findAllByNote(note)
        noteMentions.size() == mentions.size()
        if (noteMentions.size() > 0) {
            def mentionsFromNote = noteMentions*.mention*.username
            mentionsFromNote == mentions
        }


        where: "all these data are tested"
        content                                                                     | tags                                     | mentions
        "@teacher_jeanne : a simple #content, with no #spaces in the content"       | ["content", "spaces"]                    | ["teacher_jeanne"]
        "a simple #content with no #spaces in the content but with #content a copy" | ["content", "spaces"]                    | []
        "a simple #content\n #another\r #tag3\n with #spaces\t in the content"      | ["content", "another", "tag3", "spaces"] | []
        "a simple with no tags @teacher_jeanne"                                     | []                                       | ["teacher_jeanne"]
        "a simple with no @tags @teacher_jeanne"                                    | []                                       | ["teacher_jeanne"]
        "Is it #LOWERCASE ?"                                                        | ["lowercase"]                            | []

    }

    def "add bookmark"() {
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a note 1 with #tag and @${bootstrapTestService.teacherJeanne.username}")

        when: "a user bookmarks a note"
        noteService.bookmarkNotebyUser(note1, bootstrapTestService.learnerPaul)

        then: 'a bookmark object is persited in database'
        Bookmark.countByNoteAndUser(note1, bootstrapTestService.learnerPaul) == 1

        and: 'a note has no bookmarks when trying to get it by the to-many relation'
        !note1.bookmarks
    }

    def "delete bookmark"() {
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a note 1")
        noteService.bookmarkNotebyUser(note1, bootstrapTestService.learnerPaul)

        when: "a user unbookmarked a note"
        noteService.unbookmarkedNoteByUser(note1, bootstrapTestService.learnerPaul)

        then: "there is no more bookmark record in the database"
        Bookmark.countByNoteAndUser(note1, bootstrapTestService.learnerPaul) == 0

    }


    def "delete a note"() {

        given: "A note with tags, mentions, fragment tag and responses and that is bookmarked"
        Tag tag = Tag.findOrSaveWhere(name: "afragmenttag")
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a note 1 with #tag and @${bootstrapTestService.teacherJeanne.username}", null, tag, null)
        noteService.addStandardNote(bootstrapTestService.learnerMary, "a note 2", null, null, note1)
        noteService.addStandardNote(bootstrapTestService.learnerPaul, 'a note 3', null, null, note1)
        noteService.bookmarkNotebyUser(note1, bootstrapTestService.learnerPaul)

        when: "a note is removed"
        noteService.deleteNoteByAuthor(note1, bootstrapTestService.learnerMary)

        then: "the note is deleted from database"
        Note.get(note1.id) == null

        and: "Child notes have their field parentNote set to null"
        Note.countByParentNote(note1) == 0
        // use a fetch because batch query doesn't invalidate first level cache

        and: "note mentions, note tags and bookmarks are deleted too"
        NoteMention.countByNote(note1) == 0
        NoteTag.countByNote(note1) == 0
        Bookmark.countByNote(note1) == 0

    }

    def "update a note"() {

        given:"create new note"
        Note note = noteService.addNote(bootstrapTestService.learnerMary, "contennt")

        when:"try to update content of note"
        noteService.updateNoteById((note.id).toString(), "new content")

        then:"the note content is updated"
        note.content == "new content"
    }

    def "delete a note with attachement"() {

        given: "A note with an attachement"
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a note with attachement", bootstrapTestService.context1)
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)
        myAttachement = attachementService.addNoteToAttachement(note1, myAttachement)

        when: "I want to delete this note"
        noteService.deleteNoteByAuthor(note1, bootstrapTestService.learnerMary)

        then: "The note is delete"
        Note.get(note1.id) == null
        myAttachement.note == null

    }

    def "attempting to delete a note by a user that is not the author"() {

        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a note 1 with #tag and @${bootstrapTestService.teacherJeanne.username}")

        when: "a user tries to delete a note he has not written"
        noteService.deleteNoteByAuthor(note1, bootstrapTestService.learnerPaul)

        then: "the deletion fails with an illegal argument exception"
        thrown(PreconditionViolation)

    }

    def "find all question notes for a context"() {
        given: "a context"
        Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))

        and: "a std note"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerPaul, "a standard note", context)

        and: "a question note"
        Note question = noteService.addQuestion(bootstrapTestService.learnerPaul, "::a question:: what ? {=true~false}", context)

        when: "finding all questions for context"
        List<Note> res = noteService.findAllNotes(bootstrapTestService.learnerPaul, false, false, true, context, null, null, 'question', '').list
        List<Note> res2 = noteService.findAllNotesAsQuestionForContext(context)
        def count = noteService.countNotes(bootstrapTestService.learnerPaul, false, false, true, context, null, 'question')

        then: "all questions are found"
        res.size() == 1
        res.contains(question)
        res == res2
        count == 1
    }


    def "count  number of questions"() {
        given: "a context"
        Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))

        and: "a std note"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerPaul, "a standard note", context)


        when: "finding all questions for context"

        def count = noteService.countNotes(bootstrapTestService.learnerPaul, false, false, true, context, null, 'standard')

        then: "all notes are found"

        count == 1
    }

    def "find all notes in embedded mode"() {
        given: "a context"
        Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))

        and: "two std notes"
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a standard note2", context)
        Note note = noteService.addStandardNote(bootstrapTestService.learnerPaul, "a standard note", context)

        and: "a question note"
        Note question = noteService.addQuestion(bootstrapTestService.learnerPaul, "::a question:: what ? {=true~false}", context)

        when: "finding all notes"
        List<Note> res = noteService.findAllNotes(bootstrapTestService.learnerPaul, false, false, true, context, null, null, 'standard', 'on').list
        def count = noteService.countNotes(bootstrapTestService.learnerPaul, false, false, true, context, null, 'standard')

        then: "all standard notes are sort with user note in first"
        res.size() == 2
        res.contains(note)
        res.contains(note1)
        count == 2
    }

    def "find all notes and good sort, for notes with a fragment tag in embedded mode"() {
        given: "a context"
        Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))

        and: "two std notes with a fragment tag"
        Tag tag = Tag.findOrSaveWhere(name: "afragmenttag")
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary, "a standard note2", context, tag)
        Note note = noteService.addStandardNote(bootstrapTestService.learnerPaul, "a standard note", context, tag)

        and: "a question note"
        Note question = noteService.addQuestion(bootstrapTestService.learnerPaul, "::a question:: what ? {=true~false}", context, tag)

        when: "finding all notes with the fragment tag"
        List<Note> res = noteService.findAllNotes(bootstrapTestService.learnerPaul, false, false, true, context, tag, null, 'standard', 'on').list

        then: "all standard notes with the tag are found and are sort with user note in first"
        res.size() == 2
        res.contains(note)
        res.contains(note1)
        res.get(0) == note
    }

    def "test the grade of a note by a user"() {
        given: "a note and a user"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerPaul, "a standard note")
        def user = bootstrapTestService.learnerMary

        when: "user grade for the first time"
        NoteGrade grade = noteService.gradeNotebyUser(note, user, 1d)

        then: "a grade is created"
        NoteGrade fetchGrade = NoteGrade.findByNoteAndUser(note, user)
        fetchGrade.grade == 1d

        when: "the same user grade one more time"
        noteService.gradeNotebyUser(note, user, 2d)

        then: "no more grade is created"
        NoteGrade.countByNoteAndUser(note, user) == 1
        fetchGrade.grade == 2d

    }

    def "test the score of a note by a user"() {

        given: "a note and a user"
        Note note = noteService.addStandardNote(bootstrapTestService.learnerPaul, "a note", bootstrapTestService.context1)
        User user = bootstrapTestService.learnerMary

        when: "user like the note"
        noteService.scoreNotebyUser(note, user)

        then: "the note get score"
        note.score != null
        def score = Score.findByUser(user)
        score.note == note
    }

    def "test add question failure"() {

        when: "I want to a question with bad syntax"
        noteService.addQuestion(bootstrapTestService.learnerPaul, "bad syntax", bootstrapTestService.context1)

        then: "I get an IsNotQuestionException"
        thrown(IsNotQuestionException)
    }

    def "test add standard note failure"() {

        when: "I want to a standard note with bad syntax"
        noteService.addStandardNote(bootstrapTestService.learnerPaul, "::a question:: what ? {=true~false}", bootstrapTestService.context1)

        then: "I get IsNotStandardNoteException"
        thrown(IsNotStandardNoteException)
    }

    def "test find all user notes for a context"() {

        given: "two notes add by paul and one add by mary for a context"
        Tag tag = Tag.findOrSaveByName("myTag")
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerPaul, "note1", bootstrapTestService.context1, tag)
        Note note2 = noteService.addStandardNote(bootstrapTestService.learnerPaul, "note2", bootstrapTestService.context1, tag)

        when: "I want to find only paul's notes"
        List<Note> res = noteService.findAllNotes(bootstrapTestService.learnerPaul, true, true, false, bootstrapTestService.context1, tag, [sort: 'dateCreated', order: 'desc'], 'standard', 'off').list

        then: "I get only the two paul's notes"
        res.size() == 2
        res.contains(note1)
        res.contains(note2)
    }
}
