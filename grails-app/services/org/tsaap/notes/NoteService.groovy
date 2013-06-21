package org.tsaap.notes

import groovy.json.JsonBuilder
import org.tsaap.directory.User
import org.tsaap.resources.Resource

class NoteService {

  /**
   * Add a new note
   * @param author the author
   * @param content the content
   * @param rootResource the root resource
   * @param context the context
   * @param parentNote the parent note
   */
  def addNote(User author,
              String content,
              Resource rootResource = null,
              Note parentNote = null) {
    // create the note
    Note theNote = new Note(author: author,
                            content: content,
                            rootResource: rootResource,
                            parentNote: parentNote)
    // save the note
    theNote.save()

    // manage tags
    def contentFromResource = rootResource?.descriptionAsNote
    def tags = NoteHelper.tagsFromContent(content)
    if (contentFromResource) {
      tags += NoteHelper.tagsFromContent(contentFromResource)
    }
    tags.each {
      Tag tag = Tag.findOrSaveWhere(name: it)
      new NoteTag(note: theNote, tag: tag).save()
    }

    // manage mentions
    def mentions = NoteHelper.mentionsFromContent(content)
    if (contentFromResource) {
      mentions += NoteHelper.mentionsFromContent(contentFromResource)
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
