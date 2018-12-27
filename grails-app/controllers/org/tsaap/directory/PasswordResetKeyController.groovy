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

class PasswordResetKeyController {

    PasswordResetService passwordResetService
    /**
     * render the forget password view
     */
    def doForget() {
        render view:'forgetPassword'
    }

    /**
     * Reset the password if the email address is valid, otherwise return message error
     */
    def doReset() {

        def user = User.findByEmail(params.email)
        if (user) {
            def subscriptionSource = grailsApplication.config.elaastic.subscription.source ?: UserAccountService.DEFAULT_SUBSCRIPTION_SOURCE
            passwordResetService.generatePasswordResetKeyForUser(user, subscriptionSource)
            flash.message = message(code: 'passwordReset.email.success')
            render view: 'sendMailConfirm'
        }
        else {
            flash.message = message(code: 'passwordReset.email.fail')
            render view:'forgetPassword'
        }

    }

    /**
     * render passwordReset view to confirm the new password
     */
    def doPasswordReset() {
        render view:'passwordReset'
    }

    def resetPassword() {
        def key = params.passwordResetKey
        User user = PasswordResetKey.findByPasswordResetKey(key).user
        if (params.password != params.passwordConfirm) {
            user.errors.rejectValue('password', 'user.password.confirm.fail', 'The two passwords must be the same.')
        } else {
            user.password = params.password
            user.save(flush: true)
        }
        if (user.hasErrors()) {
            flash.message = message(code: 'passwordReset.fail')
            render(view: 'passwordReset', model: [passwordResetKey: params.passwordResetKey])
        } else {
            flash.message = message(code: 'useraccount.update.success')
            redirect(uri: '/login/auth')
        }

    }

    def goIndex() {
        redirect(uri:'/')
    }


}
