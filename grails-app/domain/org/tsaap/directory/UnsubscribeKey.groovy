package org.tsaap.directory

class UnsubscribeKey {

  String unsubscribeKey
  Date dateCreated
  User user

  static belongsTo = [user:User]

  static constraints = {
    unsubscribeKey blank: false
  }
}
