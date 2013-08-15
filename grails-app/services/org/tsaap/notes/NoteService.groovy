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

import org.tsaap.directory.User

class NoteService {

  /**
   * Add a new note
   * @param author the author
   * @param content the content
   * @param context the context
   * @param parentNote the parent note
   * @return the added note
   */
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




}
