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

class NoteServiceIntegrationSpec extends IntegrationSpec {

  BootstrapTestService bootstrapTestService
  NoteService noteService

  def "add notes"() {

    bootstrapTestService.initializeTests()

    when:
      Note note = noteService.addNote(bootstrapTestService.learnerPaul, content)

    then:
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


    where:
      content                                                                     | tags                                     | mentions
      "@teacher_jeanne : a simple #content, with no #spaces in the content"       | ["content", "spaces"]                    | ["teacher_jeanne"]
      "a simple #content with no #spaces in the content but with #content a copy" | ["content", "spaces"]                    | []
      "a simple #content\n #another\r #tag3\n with #spaces\t in the content"      | ["content", "another", "tag3", "spaces"] | []
      "a simple with no tags @teacher_jeanne"                                     | []                                       | ["teacher_jeanne"]
      "a simple with no @tags @teacher_jeanne"                                    | []                                       | ["teacher_jeanne"]
      "Is it #LOWERCASE ?"                                                        | ["lowercase"]                            | []

  }


}
