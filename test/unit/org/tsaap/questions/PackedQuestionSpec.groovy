package org.tsaap.questions

import grails.test.mixin.TestFor
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/
@TestFor(PackedQuestion)
class PackedQuestionSpec extends Specification {

  @Shared
  QuestionHelper questionHelper = new QuestionHelper()

  @Shared
  def questionTexts = [
          q1: '::Q1 a simple title :: 1+1=2 {T}',
          q2: '::Q2 a title with \\{ or \\} :: \\{1+1=2\\} {T}',
          q3: '::Q3 a title wiht \\: :: brackets in the answer : { = \\{truc\\}  une autre \\}} or { = \\{machin}',
          q4: '::Q4:: Two plus {=two =2} equals four and Two plus {=three =3} equals five',
          q5: '::Q5:: What\'s between orange and green in the spectrum? \n { =yellow # right; good! ~red # wrong, it\'s yellow ~blue # wrong, it\'s yellow }']

  @Unroll
  void "extraction of answer set list from question #inText"() {

    given: "a text of a question in gift format"
    String text = inText

    when: "parsing the text question"
    def res = questionHelper.getAnswerSetListForGiftText(text)

    then: "getting the list of answer set"
    res.size() == nbOfAnswerSet
    res[0] == firstAnswerSet

    where: "questions are from different type"
    inText           | nbOfAnswerSet | firstAnswerSet
    questionTexts.q1 | 1             | 'T'
    questionTexts.q2 | 1             | 'T'
    questionTexts.q3 | 2             | ' = \\{truc\\}  une autre \\}'
    questionTexts.q4 | 2             | '=two =2'
    questionTexts.q5 | 1             | ' =yellow # right; good! ~red # wrong, it\'s yellow ~blue # wrong, it\'s yellow '

  }

  @Unroll
  void "extraction of the title if it exists from question #intext"() {

  }
}
