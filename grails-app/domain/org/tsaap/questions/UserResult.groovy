package org.tsaap.questions

import org.tsaap.directory.User

class UserResult {

  User user
  PackedQuestion question
  Float percentCredit = 0

  static belongsTo = [question: PackedQuestion]

  static constraints = {}
}
