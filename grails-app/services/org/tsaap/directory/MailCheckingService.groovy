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
import groovy.sql.Sql
import org.springframework.context.MessageSource

import javax.sql.DataSource

class MailCheckingService {

    static transactional = false

    MailService mailService
    DataSource dataSource
    MessageSource messageSource

    /**
     * Send email to check user emails and then activate the corresponding user
     * accounts
     */
    def sendCheckingEmailMessages(String subscriptionSource=UserAccountService.DEFAULT_SUBSCRIPTION_SOURCE) {
        Map notifications = findAllNotifications(subscriptionSource)
        List<String> actKeysWithEmailSent = []
        notifications.each { actKey, map ->
            actKeysWithEmailSent << actKey
            try {
                def sub = messageSource.getMessage("email.checking.title", null, new Locale(map.language))
                mailService.sendMail {
                    to map.email
                    subject sub
                    html view: "/email/emailChecking", model: [user  : map,
                                                               actKey: actKey]
                }
            } catch (Exception e) {
                log.error("Error with ${map.email} : ${e.message}")
            }
        }
        log.debug("Nb email sending try : ${actKeysWithEmailSent.size()}")
        if (actKeysWithEmailSent) {
            updateEmailSentStatusForAllNotifications(actKeysWithEmailSent)
        }
    }

    /**
     * The notifications is a map :<br>
     * <ul>
     *   <li> key : the activation key
     *   <li> value : a map representing the corresponding user [user_id:..., first_name:...,email:...]
     * </ul>
     * @return the notifications as a map
     */
    protected Map findAllNotifications(String subscriptionSource) {
        def sql = new Sql(dataSource)
        def req = """
              SELECT tuser.id as user_id, tuser.first_name, tuser.email, tsettings.language, tact_key.activation_key
              FROM user as tuser
              INNER JOIN  activation_key as tact_key ON tact_key.user_id = tuser.id
              INNER JOIN settings as tsettings ON tsettings.user_id = tuser.id
              where tact_key.activation_email_sent = false and tact_key.subscription_source = $subscriptionSource """
        def rows = sql.rows(req)
        log.debug("Select request : $req")
        log.debug("Nb rows selected : ${rows.size()}")
        def notifications = [:]
        rows.each {
            def key = it.activation_key
            notifications[key] = [user_id: it.user_id, first_name: it.first_name, email: it.email, language: it.language]
        }
        sql.close()
        log.debug("Notifications to process : $notifications")
        notifications
    }

    /**
     *
     * @param actKeys list of activation keys which email was sent for
     */
    private updateEmailSentStatusForAllNotifications(List<String> actKeys) {

        def placeholderKeys = actKeys.collect { '?' }.join(',')
        def sql = new Sql(dataSource)
        def req = "update activation_key as tact_key set tact_key.activation_email_sent = true where tact_key.activation_key in ($placeholderKeys)"
        def nbUpdates = sql.executeUpdate(req, actKeys)
        log.debug("the update request : $req")
        log.debug("Nb of rows updated : $nbUpdates")
        sql.close()

    }
}
