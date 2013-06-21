package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.Resource

class Note {

  Date dateCreated
  Date lastUpdated
  User author
  Resource rootResource
  Note parentNote

  String content

  static constraints = {
    rootResource nullable: true
    parentNote nullable: true
    content maxSize: 280
  }
}
