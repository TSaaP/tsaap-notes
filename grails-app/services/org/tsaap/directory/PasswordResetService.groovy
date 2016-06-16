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

import grails.plugin.mail.MailService
import org.apache.commons.lang.time.DateUtils
import org.springframework.context.MessageSource

class PasswordResetService {

    def grailsApplication
    MailService mailService
    MessageSource messageSource

    def sendPasswordResetKeyMessages() {
        def prkList = findAllPasswordResetKey()
        prkList.each { prk ->
            try {
                def sub = messageSource.getMessage("email.passwordReset.title", null, new Locale(prk.user.settings.language))
                mailService.sendMail {
                    to prk.user.email
                    subject sub
                    html view: "/email/emailPasswordReset", model: [user            : prk.user,
                                                                    passwordResetKey: prk.passwordResetKey]
                }
                prk.passwordResetEmailSent = true
                prk.save()
            } catch (Exception e) {
                log.error("Error with ${prk.user.email}: ${e.message}")
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

    def findAllPasswordResetKey() {
        def lifetime = grailsApplication.config.tsaap.auth.password_reset_key.lifetime_in_hours ?: 1
        PasswordResetKey.withCriteria {
            eq 'passwordResetEmailSent', false
            gt 'dateCreated', DateUtils.addHours(new Date(), -lifetime)
            join 'user'
            join 'user.settings'
        }
    }

    def removeOldPasswordResetKeys() {
        def lifetime = grailsApplication.config.tsaap.auth.password_reset_key.lifetime_in_hours ?: 1
        def oldKeys = PasswordResetKey.findAllByDateCreatedLessThan(DateUtils.addHours(new Date(), -lifetime))
        PasswordResetKey.deleteAll(oldKeys)
    }
}
