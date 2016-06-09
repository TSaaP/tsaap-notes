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

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.gcontracts.annotations.Requires

@Transactional(readOnly = true)
class SettingsController {

    SpringSecurityService springSecurityService
    SettingsService settingsService

    @Secured(['IS_AUTHENTICATED_FULLY'])
    @Requires({
        !params.key || springSecurityService.currentUser == UnsubscribeKey.executeQuery(
                "SELECT user from UnsubscribeKey where unsubscribe_key = :var",
                [var: params.key])[0]
    })
    def doSettings() {
        render(view: '/settings/settings', model: [user: springSecurityService.currentUser])
        //redirect(uri: '/settings/settings')
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doUpdate() {

        User user = springSecurityService.currentUser

        def settingsMap = [:]
        if (params.dailyNotification) {
            settingsMap.dailyNotifications = true
        } else {
            settingsMap.dailyNotifications = false
        }
        if (params.mentionNotification) {
            settingsMap.mentionNotifications = true
        } else {
            settingsMap.mentionNotifications = false
        }

        settingsMap.language = params.language
        def settings = settingsService.updateSettingsForUser(user, settingsMap)

        if (settings.hasErrors()) {
            render(view: '/settings/settings')
        } else {
            flash.message = message(code: 'useraccount.update.success')
            redirect(uri: '/settings/doSettings')
        }
    }

}
