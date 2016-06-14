package org.tsaap.directory



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

class PasswordResetKeyController {

    /**
     * render the forget password view
     */
    def doForget() {
        render view:'forgetPassword'
        //render "text"
    }

    /**
     * Reset the password if the email address is valid, otherwise return message error
     */
    def doReset() {

    }
}
