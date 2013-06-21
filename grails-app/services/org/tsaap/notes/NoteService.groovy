package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.Resource

class NoteService {

  NoteHelper noteHelper

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
    def tags = noteHelper.tagsFromContent(content)
    if (contentFromResource) {
      tags += noteHelper.tagsFromContent(contentFromResource)
    }
    tags.each {
      Tag tag = Tag.findOrSaveWhere(name: it)
      new NoteTag(note: theNote, tag: tag).save()
    }

    // manage mentions
    def mentions = noteHelper.mentionsFromContent(content)
    if (contentFromResource) {
      mentions += noteHelper.mentionsFromContent(contentFromResource)
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
