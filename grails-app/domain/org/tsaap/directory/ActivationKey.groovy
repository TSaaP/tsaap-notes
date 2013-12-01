package org.tsaap.directory

class ActivationKey {

  String activationKey
  Date dateCreated
  User user
  Boolean activationEmailSent = false

  static belongsTo = [user:User]

  static constraints = {
    activationKey blank: false
  }
}
