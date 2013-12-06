package org.tsaap.questions

class UserAnswer {

  UserResult userResult
  AnswerSet answerSet
  Answer answer

  static belongsTo = [userResult:UserResult, answerSet:AnswerSet, answer:AnswerSet]

  static constraints = {}
}
