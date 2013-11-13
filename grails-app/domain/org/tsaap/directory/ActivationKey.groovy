package org.tsaap.directory

class ActivationKey {

  String activationKey
  Date dateCreated
  User user

  static belongsTo = [user:User]

  static constraints = {
    activationKey blank: false
  }
}
