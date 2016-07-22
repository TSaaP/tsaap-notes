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

package org.tsaap.questions

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.tsaap.directory.User
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(LiveSessionResponse)
@Mock([LiveSession, User])
class LiveSessionResponseSpec extends Specification {

    @Unroll
    void "test the validation of the property answerListAsString with value #value "() {
        given: "a live session response"
        LiveSessionResponse liveSessionResponse = new LiveSessionResponse(user: Mock(User), liveSession: Mock(LiveSession))

        when: "the answer list as string is set corresponding to a specific value"
        liveSessionResponse.answerListAsString = value

        then: "the live session response is valid or not"
        liveSessionResponse.validate() == result

        where: "the values of the answer list as string are"
        value                                                    || result
        null                                                     || true
        '[[]]'                                                   || true
        '[["une réponse"]]'                                      || true
        '[]'                                                     || true
        '[["une réponse","deux réponses"]]'                      || true
        '[["une réponse","deux réponses"],["une autre"],[]]'     || true
        '[,,["une réponse","deux réponses"],["une autre"],[],,]' || true
        ''                                                       || false
        'a string not in bracket'                                || false
        '[["a mal formed list"]'                                 || false
        '[[lost quotes]]'                                        || false

    }

    void "test prettyAnswers"() {
        given: "a live session response"
        LiveSessionResponse liveSessionResponse = new LiveSessionResponse(user: Mock(User), liveSession: Mock(LiveSession))

        when: "the answer list as string is set corresponding to a specific value"
        liveSessionResponse.answerListAsString = value

        then: "the prettyAnswers corresponds"
        liveSessionResponse.prettyAnswers() == pretty

        where: "the values of the answer list as string are"
        value              || pretty
        '[[]]'             || ''
        '[["0"]]'          || '1'
        '[["1","3","14"]]' || '2,4,15'
    }
}