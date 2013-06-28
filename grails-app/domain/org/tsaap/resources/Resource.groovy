package org.tsaap.resources

class Resource {

  Date dateCreated
  String url

  /**
   * The description note allows the description of the resource with tags and
   * mentions following the way they are used in a note (# or @ prefix).
   * All mentions and tags on the resource will be automatically bind to each
   * note taken by a user on this resource
   **/
  String descriptionAsNote
  String metadata


  static constraints = {
    url url: true, unique: true
    metadata nullable: true
    descriptionAsNote nullable: true, maxSize: 280
  }
}
