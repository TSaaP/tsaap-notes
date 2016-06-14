package org.tsaap.directory

import grails.plugin.mail.MailService
import org.apache.commons.lang.time.DateUtils
import org.springframework.context.MessageSource

class PasswordResetService {

    def grailsApplication
    MailService mailService
    MessageSource messageSource

    def lifetime = grailsApplication.config.tsaap.auth.password_reset_key.lifetime_in_hours ?: 1

    def sendPasswordResetKeyMessages(User user) {
        def prkList = PasswordResetKey.findAllByResetPasswordEmailSentAndDateCreatedLessThan(false, DateUtils.addHours(new Date(), -lifetime))
        prkList.each {
            try {
                def sub = messageSource.getMessage("email.passwordReset.title", null, new Locale(Settings.findByUser(user).language))
                mailService.sendMail {
                    to user.email
                    subject sub
                    html view: "/email/emailPasswordReset", model: [user            : user,
                                                                    passwordResetKey: it.passwordResetKey]
                }
            } catch (Exception e) {
                log.error("Error with ${user.email}: ${e.message}")
            }
        }
    }

    def getPasswordResetKeyForUser(User user) {
        def prk = PasswordResetKey.findByUser(user)
        if (prk) {
            if (prk.dateCreated < DateUtils.addHours(new Date(), -lifetime)) {
                prk.passwordResetKey = UUID.randomUUID().toString()
                prk.dateCreated = new Date()
            }
            prk.resetPasswordEmailSent = false
        } else {
            prk = new PasswordResetKey(passwordResetKey: UUID.randomUUID().toString(), user: user)
        }
        prk.save()
        prk
    }
}
