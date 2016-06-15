package org.tsaap.directory

import grails.plugin.mail.MailService
import org.apache.commons.lang.time.DateUtils
import org.springframework.context.MessageSource

class PasswordResetService {

    def grailsApplication
    MailService mailService
    MessageSource messageSource

    def sendPasswordResetKeyMessages() {
        def prkList = findAllPasswordResetKey()
        prkList.each {
            try {
                def map = it
                def sub = messageSource.getMessage("email.passwordReset.title", null, new Locale(map.user.settings.language))
                mailService.sendMail {
                    to map.user.email
                    subject sub
                    html view: "/email/emailPasswordReset", model: [user            : map.user,
                                                                    passwordResetKey: map.passwordResetKey]
                }
                it.passwordResetEmailSent = true
                it.save()
            } catch (Exception e) {
                log.error("Error with ${map.user.email}: ${e.message}")
            }
        }
    }

    def generatePasswordResetKeyForUser(User user) {
        def lifetime = grailsApplication.config.tsaap.auth.password_reset_key.lifetime_in_hours ?: 1
        def prk = PasswordResetKey.findByUser(user)
        if (prk) {
            if (prk.dateCreated < DateUtils.addHours(new Date(), -lifetime)) {
                prk.passwordResetKey = UUID.randomUUID().toString()
                prk.dateCreated = new Date()
            }
            prk.passwordResetEmailSent = false
        } else {
            prk = new PasswordResetKey(passwordResetKey: UUID.randomUUID().toString(), user: user)
        }
        prk.save()
        prk
    }

    /**
     *
     * @param email
     * @return true if the address exist in database, false otherwise
     */
    def findUserByEmailAddress(String email) {
      User.findByEmail(email)
    }

    def findAllPasswordResetKey() {
        def lifetime = grailsApplication.config.tsaap.auth.password_reset_key.lifetime_in_hours ?: 1
        PasswordResetKey.withCriteria {
            eq 'passwordResetEmailSent', false
            gt 'dateCreated', DateUtils.addHours(new Date(), -lifetime)
            join 'user'
            join 'user.settings'
        }
    }
}
