package org.tsaap.directory

class UnsubscribeKey {

  String unsubscribeKey
  User user

  static belongsTo = [user:User]

  static constraints = {
    unsubscribeKey blank: false
  }
}
