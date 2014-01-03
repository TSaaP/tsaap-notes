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

import org.tsaap.questions.Answer
import org.tsaap.questions.AnswerBlock
import org.tsaap.questions.Question
import org.tsaap.questions.QuestionType
import org.tsaap.questions.impl.DefaultQuestion
import org.tsaap.questions.impl.gift.GiftQuizContentHandler
import org.tsaap.questions.impl.gift.GiftReader
import org.tsaap.questions.impl.gift.GiftReaderNotEscapedCharacterException
import org.tsaap.questions.impl.gift.GiftReaderQuestionWithInvalidFormatException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/

class GiftReaderSpec extends Specification {


    @Unroll
    def "test the parsing of well formed Exclusive Choice question"() {

        given: "a text containing one well formated gift question"
        def questionText = currentQText

        and: "a default gift reader on this question"
        GiftQuizContentHandler handler = new GiftQuizContentHandler()
        def quizReader = new GiftReader(quizContentHandler: handler)
        def reader = new StringReader(currentQText)

        when: "parsing the text with the GiftReader"
        quizReader.parse(reader)

        then: "the obtained quiz has one question with the title and type correctly set"
        def quiz = handler.quiz
        quiz.questionList.size() == 1
        Question question = quiz.questionList[0]
        question.title == title
        question.questionType == QuestionType.ExclusiveChoice
        question.questionType.code == 1

        and: "the question is composed with one answer fragment and at least one text fragment"
        question.answerBlockList.size() == 1
        question.textBlockList.size() == nbTextFragments
        question.blockList.size() == nbFragments

        and: "the answer fragment has at least two answers"
        AnswerBlock answerFragment = question.answerBlockList[0]
        answerFragment.answerList.size() == nbAnswers
        Answer ans1 = answerFragment.answerList[0]
        Answer ans2 = answerFragment.answerList[1]

        and: "the answers have properties correctly set."
        ans1.identifier == "0"
        ans1.textValue == answerText1
        ans1.feedBack == answerFeedback1
        ans1.percentCredit == answerCredit1
        ans2.identifier == "1"
        ans2.textValue == answerText2
        ans2.feedBack == answerFeedback2
        ans2.percentCredit == answerCredit2

        where: "the given texts are representative of relevant use cases"
        currentQText | title          | nbFragments | nbTextFragments | nbAnswers | answerText1 | answerCredit1 | answerFeedback1 | answerText2 | answerCredit2 | answerFeedback2
        ec_q1_ok     | 'Question 1'   | 2           | 1               | 3         | 'yellow'    | 100f          | null            | 'red'       | 0f            | null
        ec_q2_ok     | 'Question 2'   | 2           | 1               | 3         | 'yellow'    | 100f          | 'congrats !'    | 'red'       | 0f            | 'try again'
        ec_q3_ok     | 'Question : 3' | 2           | 1               | 3         | 'yellow'    | 100f          | 'congrats !'    | 'red'       | 0f            | 'not ='

    }


    @Unroll
    def "test the parsing of malformed Exclusive Choice question"() {

        given: "a text containing one mal formated gift question"
        def questionText = currentQText

        and: "a default gift reader on this question"
        GiftQuizContentHandler handler = new GiftQuizContentHandler()
        def quizReader = new GiftReader(quizContentHandler: handler)
        def reader = new StringReader(currentQText)

        when: "parsing the text with the GiftReader"
        quizReader.parse(reader)

        then: "the parsing fail with a QuizReaderException"
        thrown(anExecptionType)

        where: "the given texts are representative of relevant use cases"
        currentQText | anExecptionType
        ec_q1_ko     | GiftReaderNotEscapedCharacterException.class
        ec_q2_ko     | GiftReaderQuestionWithInvalidFormatException.class
        ec_q3_ko     | GiftReaderNotEscapedCharacterException.class


    }

    @Unroll
    def "test the parsing of well formed Multiple Choice question"() {

        given: "a text containing one well formated gift question"
        def questionText = currentQText

        and: "a default gift reader on this question"
        GiftQuizContentHandler handler = new GiftQuizContentHandler()
        def quizReader = new GiftReader(quizContentHandler: handler)
        def reader = new StringReader(currentQText)

        when: "parsing the text with the GiftReader"
        quizReader.parse(reader)

        then: "the obtained quiz has one question with the title and type correctly set"
        def quiz = handler.quiz
        quiz.questionList.size() == 1
        Question question = quiz.questionList[0]
        question.title == title
        question.questionType == QuestionType.MultipleChoice
        question.questionType.code == 2

        and: "the question is composed with one answer fragment and at least one text fragment"
        question.answerBlockList.size() == 1
        question.textBlockList.size() == nbTextFragments
        question.blockList.size() == nbFragments

        and: "the answer fragment has at least two answers"
        AnswerBlock answerFragment = question.answerBlockList[0]
        answerFragment.answerList.size() == nbAnswers
        Answer ans1 = answerFragment.answerList[0]
        Answer ans2 = answerFragment.answerList[1]

        and: "the answers have properties correctly set."
        ans1.identifier == "0"
        ans1.textValue == answerText1
        ans1.feedBack == answerFeedback1
        ans1.percentCredit == answerCredit1
        ans2.identifier == "1"
        ans2.textValue == answerText2
        ans2.feedBack == answerFeedback2
        ans2.percentCredit == answerCredit2

        where: "the given texts are representative of relevant use cases"
        currentQText | title          | nbFragments | nbTextFragments | nbAnswers | answerText1 | answerCredit1 | answerFeedback1 | answerText2 | answerCredit2 | answerFeedback2
        mc_q1_ok     | 'Question 1'   | 2           | 1               | 4         | 'tomatoes'  | 50f           | null            | 'potatoes'  | -50f          | null
        mc_q2_ok     | 'Question 2'   | 2           | 1               | 4         | 'tomatoes'  | 50f           | 'yep !'         | 'potatoes'  | -50f          | 'bad !'
        mc_q3_ok     | 'Question : 3' | 2           | 1               | 4         | 'tomatoes'  | 50f           | 'yep !'         | 'potatoes'  | -50f          | 'bad !'

    }

    @Shared // basic EC question without feedback
    def ec_q1_ok = '::Question 1:: What\'s between orange and green in the spectrum ? \n { =yellow ~red  ~blue }'

    @Shared // EC question with feedback
    def ec_q2_ok = '::Question 2:: What\'s between orange and green in the spectrum ? \n { =yellow # congrats ! ~red # try again  ~blue #not yet }'

    @Shared // EC Question with escape characters
    def ec_q3_ok = '::Question \\: 3:: What\'s between orange and green in the \\#spectrum ? \n { =yellow # congrats ! ~red #not \\= ~blue # try again }'

    @Shared // basic EC question without no left bracket
    def ec_q1_ko = '::Question 1:: What\'s between orange and green in the spectrum ? \n  =yellow ~red  ~blue }'

    @Shared // EC question with feedback  but without right bracket
    def ec_q2_ko = '::Question 2:: What\'s between orange and green in the spectrum ? \n { =yellow # congrats ! ~red # try again  ~blue #not yet '

    @Shared // EC Question with one  character not escaped  in the title
    def ec_q3_ko = '::Question : 3:: What\'s between orange and green in the \\#spectrum ? \n { =yellow # congrats ! ~red #not \\= ~blue # try again }'

    @Shared // basic MC question without feedback
    def mc_q1_ok = '::Question 1:: What\'are fruits  ? \n { ~%50%tomatoes  ~%-50%potatoes ~%50%apple ~%-50%pepper}'

    @Shared // MC question with feedback
    def mc_q2_ok = '::Question 2:: What\'are fruits  ? \n { ~%50%tomatoes #yep !  ~%-50%potatoes #bad ! ~%50%apple#yop ! ~%-50%pepper#null!}'

    @Shared // MC Question with escape characters
    def mc_q3_ok = '::Question \\: 3:: What\'are fruits  ? \n { ~%50%tomatoes #yep !  ~%-50%potatoes #bad ! ~%50%apple#yop ! ~%-50%pepper#null!}'


}