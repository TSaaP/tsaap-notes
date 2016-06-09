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

import org.tsaap.questions.impl.gift.utils.GeneralFeedbackAlreadySetException
import org.tsaap.questions.impl.gift.utils.NoCloseBracketException
import org.tsaap.questions.impl.gift.utils.QuestionHelper
import spock.lang.Shared
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/

class QuestionHelperSpec extends Specification {

    def "test successful insertion of general feedback"() {

        given: "a question helper and a general feedback to insert"
        def qh = new QuestionHelper()
        def gfk = "a general feedback \n with : special \\ characters { } # = ~"

        when: "trying to insert feedback in gift question"
        def res = qh.insertGeneralFeedbackInGiftQuestion(gfk, mc_q3_ok)

        then: "the new gift question contains the general feedback"
        res == mc_q3_gf_ok
    }

    def "test insertion of general feedback in a question which has already one"() {

        given: "a question helper and a general feedback to insert"
        def qh = new QuestionHelper()
        def gfk = "a general feedback with : special \\ characters { } # = ~"

        when: "trying to insert feedback in gift question which has already a general feedback set"
        qh.insertGeneralFeedbackInGiftQuestion(gfk, mc_q3_gf_ok)

        then: "An exception is thrown indicating a feedback is already set"
        thrown(GeneralFeedbackAlreadySetException)
    }

    def "test insertion of general feedback in a question with no right bracket"() {

        given: "a question helper and a general feedback to insert"
        def qh = new QuestionHelper()
        def gfk = "a general feedback with : special \\ characters { } # = ~"

        when: "trying to insert feedback in gift question with no right bracket"
        qh.insertGeneralFeedbackInGiftQuestion(gfk, ec_q2_ko)

        then: "the an exception is thrown indicating the question has no right bracket"
        thrown(NoCloseBracketException)
    }

    @Shared
    // EC question with feedback  but without right bracket
    def ec_q2_ko = '::Question 2:: What\'s between orange and green in the spectrum ? \n { =yellow # congrats ! ~red # try again  ~blue #not yet '

    @Shared
    // MC Question with escape characters
    def mc_q3_ok = '::Question \\: 3:: What\'are fruits  ? \n { ~%50%tomatoes #yep !  ~%-50%potatoes #bad ! ~%50%apple#yop ! ~%-50%pepper#null!}'

    @Shared
    // MC Question with escape characters
    def mc_q3_gf_ok = '::Question \\: 3:: What\'are fruits  ? \n { ~%50%tomatoes #yep !  ~%-50%potatoes #bad ! ~%50%apple#yop ! ~%-50%pepper#null!####a general feedback \n with \\: special \\\\ characters \\{ \\} \\# \\= \\~}'


}