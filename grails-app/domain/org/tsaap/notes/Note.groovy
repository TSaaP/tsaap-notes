package org.tsaap.notes

import org.tsaap.directory.User
import org.tsaap.resources.Resource

class Note {
  String globalId
  Date dateCreated
  Date lastUpdated
  User author
  Resource rootResource
  Note parentNote

  String content

  /**
   * The json Document of the note that encapsulate all the relevant informations
   * on the current note. Two goals here :
   * <ul>
   *   <li> make easy to expose the current note in a rest api
   *   <li> make easy the retrieve of the note in one shot by a third
   *   party tool
   * </ul>
   * */
  String jsonDocument


  static constraints = {
    rootResource nullable: true
    parentNote nullable: true
    content maxSize: 280
  }
}
