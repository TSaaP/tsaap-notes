package org.tsaap.questions

class UserAnswer {

  UserResult userResult
  Answer answer

  static belongsTo = [userResult:UserResult]

  static constraints = {}
}
