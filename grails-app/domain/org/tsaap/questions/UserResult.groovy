package org.tsaap.questions

import org.tsaap.directory.User

class UserResult {

  User user
  Question question
  Float percentCredit = 0

  static belongsTo = [question: Question]

  static constraints = {}
}
