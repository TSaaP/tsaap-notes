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
