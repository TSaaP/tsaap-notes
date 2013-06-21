package org.tsaap.resources

import org.tsaap.directory.User

class ResourceFollower {

  Date dateCreated
  User follower
  Resource resource

  static constraints = {}

  static mapping = {
    cache 'read-write'
  }

}
