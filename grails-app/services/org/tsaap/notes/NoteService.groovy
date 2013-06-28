package org.tsaap.notes

import groovy.json.JsonBuilder
import org.tsaap.directory.User
import org.tsaap.resources.Resource

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

    def contentFromResource = context?.resource?.descriptionAsNote
    if (contentFromResource) {
      tags += NoteHelper.tagsFromContent(contentFromResource)
      mentions += NoteHelper.mentionsFromContent(contentFromResource)
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
