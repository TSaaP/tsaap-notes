package org.tsaap.questions

class PackedQuestion {

  int questionType = 1
  String inText

  String title
  String resolvedText

  List<AnswerSet> answerSetList

  static hasMany = [answerSetList:AnswerSet]

  static constraints = {
    questionType inList: QuestionTypeEnum.values()*.code
    title nullable: true
    inText blank: false
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

