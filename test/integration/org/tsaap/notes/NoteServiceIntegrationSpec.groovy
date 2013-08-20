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

import grails.plugin.spock.IntegrationSpec
import org.tsaap.BootstrapTestService

class NoteServiceIntegrationSpec extends IntegrationSpec {

  BootstrapTestService bootstrapTestService
  NoteService noteService

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
    Note note1 = noteService.addNote(bootstrapTestService.learnerMary, "a note 1 with #tag and @${bootstrapTestService.teacherJeanne.username}")

    when: "a user bookmarks a note"
    noteService.bookmarkNotebyUser(note1, bootstrapTestService.learnerPaul)

    then: 'a bookmark object is persited in database'
    Bookmark.countByNoteAndUser(note1, bootstrapTestService.learnerPaul) == 1

    and: 'a note has no bookmarks when trying to get it by the to-many relation'
    !note1.bookmarks
  }

  def "delete bookmark"() {
    Note note1 = noteService.addNote(bootstrapTestService.learnerMary, "a note 1")
    noteService.bookmarkNotebyUser(note1, bootstrapTestService.learnerPaul)

    when: "a user unbookmarked a note"
    noteService.unbookmarkedNoteByUser(note1, bootstrapTestService.learnerPaul)

    then: "there is no more bookmark record in the database"
    Bookmark.countByNoteAndUser(note1, bootstrapTestService.learnerPaul) == 0

  }


  def "delete a note"() {

    Note note1 = noteService.addNote(bootstrapTestService.learnerMary, "a note 1 with #tag and @${bootstrapTestService.teacherJeanne.username}")
    Note note2 = noteService.addNote(bootstrapTestService.learnerMary, "a note 2", null, note1)
    Note note3 = noteService.addNote(bootstrapTestService.learnerPaul, 'a note 3', null, note1)
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

  def "attempting to delete a note by a user that is not the author"() {

    Note note1 = noteService.addNote(bootstrapTestService.learnerMary, "a note 1 with #tag and @${bootstrapTestService.teacherJeanne.username}")

    when: "a user tries to delete a note he has not written"
    noteService.deleteNoteByAuthor(note1, bootstrapTestService.learnerPaul)

    then: "the deletion fails with an illegal argument exception"
    thrown(IllegalArgumentException)

  }


}
