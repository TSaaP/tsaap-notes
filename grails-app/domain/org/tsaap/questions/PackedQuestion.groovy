package org.tsaap.questions

class PackedQuestion {

  int questionType = 1
  String inText

  String title


  static constraints = {
    questionType inList: QuestionType.values()*.code
    title nullable: true
    inText blank: false
  }

}



