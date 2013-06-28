package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.Resource

/**
 * The context describes the context which the learner take notes in.
 * Typically the context references a resource and a description that allows
 * the capture of tags and mentions for all notes taken in the context
 * */
class Context {

  Date dateCreated
  Resource resource
  /**
   * The owner is most probably the teacher in a learning context
   **/
  User owner

  /**
   * The description note allows the description of the context with tags and
   * mentions following the way they are used in a note (# or @ prefix).
   * All mentions and tags on the context will be automatically bind to each
   * note taken by a user on this resource
   **/
  String descriptionAsNote

  static constraints = {
    descriptionAsNote nullable: true, maxSize: 280
  }
}
