package org.tsaap.notes

class NoteTag {

  Note note
  Tag tag

  static constraints = {}

  static mapping = {
    cache 'read-write'
  }
}
