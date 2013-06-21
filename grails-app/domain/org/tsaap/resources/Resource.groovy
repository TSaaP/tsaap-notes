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

  /**
   * The json Document of the resource that encapsulates all the relevant
   * informations on the current resource. Two goals here :
   * <ul>
   *   <li> make easy to expose the current resource informations in a rest api
   *   <li> make easy the retrieve of the resource informations in one shot by a third
   *   party tool
   * </ul>
   * */
  String jsonDocument

  static constraints = {
    url url: true, unique: true
    metadata nullable: true
    descriptionAsNote nullable: true, maxSize: 280
  }
}
