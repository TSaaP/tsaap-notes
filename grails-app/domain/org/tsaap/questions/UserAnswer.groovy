package org.tsaap.questions

class UserAnswer {

  UserResult userResult

  static belongsTo = [userResult:UserResult]

  static constraints = {}
}
