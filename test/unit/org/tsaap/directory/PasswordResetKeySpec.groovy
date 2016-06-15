package org.tsaap.directory

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(PasswordResetKeyController)
@Mock([User])
class PasswordResetKeySpec extends Specification {

    def setup() {
        User user = new User(firstName: "moghite", lastName: "kacimi", username: "akac", email: "akac@mail.com", password: "password")
    }

    def cleanup() {
    }

    void "test doForget action"() {
        when:"Call doforget action"
        controller.doForget()

        then:"must rend forget pasword view"
        view == '/passwordResetKey/forgetPassword'
    }

    void "test doReset action"() {
        when:"Try to searsh with an invalid email"
        params.email = "akacakac@mail.com"

        and:"call doReset action"
        controller.doReset()

        then:"must rend forget password view"
        view == '/passwordResetKey/forgetPassword'
    }
}
