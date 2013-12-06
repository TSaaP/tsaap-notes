package org.tsaap.questions

import grails.test.mixin.TestFor
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/
@TestFor(Question)
class QuestionSpec extends Specification {

  @Shared
  QuestionHelper questionHelper = new QuestionHelper()

  @Unroll
  void "creation of answer set list from question #inText"() {

    given: "a text of a question in gift format"
    String text = inText

    when: "parsing the text question the first time"
    def res = questionHelper.getAnswerSetListForGiftText(text)

    then: "getting the list of answer set"
    res.size() == nbOfAnswerSet
    res[0] == firstAnswerSet

    where: inText            | nbOfAnswerSet | firstAnswerSet
    '::Q1:: 1+1=2 {T}'       | 1             | 'T'
    '::Q1:: \\{1+1=2\\} {T}' | 1             | 'T'
    '::Q1:: brackets in the answer : { = \\{truc\\}  une autre \\}} or { = \\{machin}' | 2 | ' = \\{truc\\}  une autre \\}'
    '::Q2:: What\'s between orange and green in the spectrum? \n' + '\' + \'{ =yellow # right; good! ~red # wrong, it\'s yellow ~blue # wrong, it\'s yellow }' | 1 | ' =yellow # right; good! ~red # wrong, it\'s yellow ~blue # wrong, it\'s yellow '
    '::Q3:: Two plus {=two =2} equals four and Two plus {=three =3} equals five'| 2 | '=two =2'
  }
}
