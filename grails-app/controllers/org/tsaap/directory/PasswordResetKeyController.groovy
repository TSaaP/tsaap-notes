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

        def user = passwordResetService.findUserByEmailAddress(params.email)
        if (user) {
            passwordResetService.generatePasswordResetKeyForUser(user)
            flash.message = message(code: 'useraccount.reset.success')
            redirect(uri: '/login/auth')
        }
        else {
            flash.message = message(code: 'useraccount.reset.fail')
            render view:'forgetPassword'
        }

    }
}
