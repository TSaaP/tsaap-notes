package org.tsaap.directory

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(PasswordResetKeyController)
class PasswordResetKeySpec extends Specification {

    SpringSecurityService springSecurityService = Mock(SpringSecurityService)
    PasswordResetService passwordResetService = Mock(PasswordResetService)

    User user
    def setup() {
        user= new User(firstName: "moghite", lastName: "kacimi", username: "akac", email: "akac@mail.com", password: "password")
        user.springSecurityService = springSecurityService
        springSecurityService.encodePassword(user.password) >> user.password
        user.save()

    }

    def cleanup() {
        user.delete()
    }

    void "test doForget action"() {
        when:"Call doforget action"
        controller.doForget()

        then:"must rend forget pasword view"
        view == '/passwordResetKey/forgetPassword'
    }

    void "test doReset action"() {
        when:"Try to reset password with an invalid email adress"
        params.email = "akacakac@mail.com"

        and:"call doReset action"
        controller.doReset()

        then:"must rend forget password view"
        view == '/passwordResetKey/forgetPassword'

        when: "Trying to reset password with an valid email address"
        params.email = "akac@mail.com"

        and:"call doReset action"
        controller.doReset()

        then:""
        response.redirectedUrl == '/login/auth'
    }

    void "test doPasswordReset action"() {
        when:"call doPasswordReset action"
        controller.doPasswordReset()

        then:"must rend passwordReset view"
        view == '/passwordResetKey/passwordReset'
    }

}
