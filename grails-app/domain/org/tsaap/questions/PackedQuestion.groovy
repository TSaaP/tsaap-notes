package org.tsaap.questions

class PackedQuestion {

  int questionType = 1
  String inText



  static constraints = {
    questionType inList: QuestionType.values()*.code
    inText blank: false
  }

}



