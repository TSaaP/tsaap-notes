/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.tsaap.questions.gift

import org.tsaap.questions.Question
import org.tsaap.questions.UserResponse
import org.tsaap.questions.impl.gift.GiftQuestionService
import org.tsaap.questions.impl.gift.GiftUserResponseAnswerBlockListSizeIsNotValidInResponse
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/

class UserResponseSpec extends Specification {


    @Unroll
    def "test the evaluation of the valid response given on an Exclusive Choice question"() {

        given: "a question corresponding to an EC question written in gift format"
        Question question = giftQuestionService.getQuestionFromGiftText(ec_q3_ok)

        when: "the user choose the good answer"
        UserResponse userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList("u1", question, [["0"]])
        userResponse.question == question
        userResponse.userIdentifier == "u1"


        then: "the user has 100% of credit"
        userResponse.evaluatePercentCredit() == 100f

        when: "the user choose the bad answer"
        userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList("u1", question, [["2"]])

        then: "the user has 0% credit"
        userResponse.evaluatePercentCredit() == 0f

        when: "the user choose no answers"
        userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList("u1", question, [[]])


        then: "the user has 0% credit"
        userResponse.evaluatePercentCredit() == 0f


    }

    @Unroll
    def "test the validation of an EC question response"() {
        given: "a question corresponding to a question written in gift format"
        Question question = giftQuestionService.getQuestionFromGiftText(ec_q3_ok)

        when: "a response given by a user contains a different count of answer blocks"
        giftQuestionService.createUserResponseForQuestionAndAnswerBlockList("u1", question, [["0"], ["2"]])

        then: "the response is not valid"
        thrown(GiftUserResponseAnswerBlockListSizeIsNotValidInResponse)

        when: "a response given by a user doesn't mach with any choice"
        UserResponse userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList("u1", question, [["3"]])

        then: "the response is not valid and then credit is 0"
        userResponse.evaluatePercentCredit() == 0f

    }


    @Shared
    // EC Question with escape characters
    def ec_q3_ok = '::Question \\: 3:: What\'s between orange and green in the \\#spectrum ? \n { =yellow # congrats ! ~red #not \\= ~blue # try again }'

    @Shared
    GiftQuestionService giftQuestionService = new GiftQuestionService();

}