package org.tsaap.notes

import org.tsaap.directory.User

class Note {
  String globalId
  Date dateCreated
  Date lastUpdated
  User author
  Context context
  Note parentNote

  String content


  static constraints = {
    context nullable: true
    parentNote nullable: true
    content maxSize: 280
  }
}
