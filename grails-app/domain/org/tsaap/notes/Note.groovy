package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.ResourceDescription

class Note {

  User author
  ResourceDescription rootResource
  Note parentNote
  Context context

  String content

  static constraints = {
    rootResource nullable: true
    parentNote nullable: true
    context nullable: true
  }
}
