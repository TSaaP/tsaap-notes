package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.ResourceDescription

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
    // parse tags


    theNote

  }
}
