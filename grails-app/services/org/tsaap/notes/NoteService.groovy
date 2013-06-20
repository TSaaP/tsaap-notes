package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.ResourceDescription

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
              ResourceDescription rootResource = null,
              Context context = null,
              Note parentNote = null) {
    // create the note
    Note theNote = new Note(author: author,
                            content: content,
                            rootResource: rootResource,
                            context: context,
                            parentNote: parentNote)
    // save the note
    theNote.save()

    // manage tags
    def tags = noteHelper.tagsFromContent()
    tags.each {
      Tag tag = Tag.findOrSaveWhere(name: it)
      new NoteTag(note: theNote, tag: tag).save()
    }

    // manage mentions
    def mentions = noteHelper.mentionsFromContent()
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
