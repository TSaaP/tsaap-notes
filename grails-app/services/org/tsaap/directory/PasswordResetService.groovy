package org.tsaap.directory

import grails.plugin.mail.MailService
import org.springframework.context.MessageSource

class PasswordResetService {

    MailService mailService
    MessageSource messageSource

    def sendPasswordResetKeyForUser(User user) {
        def prk = PasswordResetKey.findByUser(user)
        if (!prk) {
            prk = createPasswordResetKeyForUser(user)
        }

        try {
            def sub = messageSource.getMessage("email.passwordReset.title", null, new Locale(Settings.findByUser(user).language))
            mailService.sendMail {
                to user.email
                subject sub
                html view: "/email/emailPasswordReset", model: [user            : user,
                                                                passwordResetKey: prk.passwordResetKey]
            }
        } catch (Exception e) {
            log.error("Error with ${user.email}: ${e.message}")
        }
    }

    def createPasswordResetKeyForUser(user) {
        def prk = new PasswordResetKey(passwordResetKey: UUID.randomUUID().toString(), user: user)
        prk.save()
        prk
    }
}
