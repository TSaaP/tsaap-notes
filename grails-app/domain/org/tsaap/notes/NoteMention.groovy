package org.tsaap.notes

import org.tsaap.directory.User

class NoteMention {

  Note note
  User mention

  static constraints = {}

  static mapping = {
    cache 'read-write'
  }
}
