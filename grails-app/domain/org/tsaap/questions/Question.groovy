package org.tsaap.questions

class Question {

  int questionType = 1
  String title
  String text
  List<AnswerSet> responseSets

  static hasMany = [responseSets:AnswerSet]

  static constraints = {
    questionType inList: QuestionTypeEnum.values()*.code
    title nullable: true
    text blank: false
  }
}


enum QuestionTypeEnum {
  ExclusiveChoice(1),
  MultipleChoice(2),
  TrueFalse(3),
  FillInTheBlank(4)

  int code

  QuestionTypeEnum(int code) {
    this.code = code
  }

}