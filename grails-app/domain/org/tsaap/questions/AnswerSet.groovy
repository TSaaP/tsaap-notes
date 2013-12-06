package org.tsaap.questions

class AnswerSet {

  String text
  Question question
  List<Answer> answers

  static hasMany = [answers:Answer]

  static belongsTo = [question: Question]

  static constraints = {
    text blank: false
  }
}
