package org.tsaap.notes

import org.tsaap.directory.User

class ContextFollower {

  Date dateCreated
  User follower
  Context context

  static constraints = {}

  static mapping = {
    cache 'read-write'
  }

}
