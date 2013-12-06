package org.tsaap.questions

class Answer {

  String text
  String feedback
  Float percentCredit = 0

  Question question

  static belongsTo = [question:Question]

  static constraints = {
    text blank: false
    feedback nullable: true
  }
}
