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
@Mock([User, PasswordResetService, PasswordResetKey])
class PasswordResetKeyControllerSpec extends Specification {

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

        then:"must rend send mail confirm view"
        view == '/passwordResetKey/sendMailConfirm'

    }

    void "test doPasswordReset action"() {
        when:"call doPasswordReset action"
        controller.doPasswordReset()

        then:"must rend passwordReset view"
        view == '/passwordResetKey/passwordReset'
    }

    void "test goIndex action"() {
        when:"call goIndew action"
        controller.goIndex()

        then:"redirect to the index view"
        response.redirectedUrl == '/'
    }

    void "test resetPassword action"() {

        when:"generate password rest key for user for reset current password"
        PasswordResetKey prk = new PasswordResetKey(passwordResetKey: UUID.randomUUID().toString(), user: user, subscriptionSource: 'elaastic').save()
        params.passwordResetKey = PasswordResetKey.findByUser(user).passwordResetKey

        and:"try the reset password when the passwords are not the same"
        params.password = "password1"
        params.passwordConfirm = "password2"
        controller.resetPassword()

        then:"return passwordrest view with the passwordResetKey params"
        view == '/passwordResetKey/passwordReset'
        model.passwordResetKey == params.passwordResetKey

        when:"try the reset password when the passwords are the same"
        params.password = "1234"
        params.passwordConfirm = "1234"
        controller.resetPassword()

        then:"the password has a new value"
        user.password == "1234"

    }
}
