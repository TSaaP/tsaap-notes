/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tsaap.questions.gift

import org.tsaap.questions.impl.DefaultQuestion
import org.tsaap.questions.impl.gift.GiftQuizContentHandler
import org.tsaap.questions.impl.gift.GiftReader
import spock.lang.Shared
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/

class GiftReaderSpec extends Specification {

  @Shared
  def wellFormatedECQuestions = [q1: '::Question 1:: What\'s between orange and green in the spectrum ? \n { =yellow ~red  ~blue }']

  def "test the parsing of well formed exclusive choice question"() {

    given: "a text containing well formated gift question corresponding to exclusive choice question"
    def questionText = currentQuestionText

    when: "parsing the text with the GiftReader and the default gift content handler"
    GiftQuizContentHandler handler = new GiftQuizContentHandler()
    def quizReader = new GiftReader(quizContentHandler: handler)
    def reader = new StringReader(currentQuestionText)
    quizReader.parse(reader)

    then: "the obtained quiz is OK"
    def quiz = handler.quiz
    quiz.questionList.size() == 1
    DefaultQuestion question = quiz.questionList[0]
    question.title == title
    question.answerFragmentList.size() == numberOfAnswerFragments
    question.textFragmentList.size() == numberOfTextFragments
    question.fragmentList.size() == numberOfFragments

    where: "the given texts are representative of relevant use cases"
    currentQuestionText        | title        | numberOfFragments | numberOfAnswerFragments | numberOfTextFragments | numberOfAnswers
    wellFormatedECQuestions.q1 | 'Question 1' | 2                 | 1                       | 1                     | 3

  }

}
