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
}