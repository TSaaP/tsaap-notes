package org.tsaap.directory

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SettingsController {

    SpringSecurityService springSecurityService
    SettingsService settingsService

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doSettings() {
        render(view: '/settings/settings',model: [user: springSecurityService.currentUser])
        //redirect(uri: '/settings/settings')
    }
    @Transactional
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doUpdate() {

        User user = springSecurityService.currentUser

        def settingsMap = [:]
        if (params.dailyNotification) {
            settingsMap.dailyNotifications = true
        }
        else {
            settingsMap.dailyNotifications = false
        }
        if (params.mentionNotification) {
            settingsMap.mentionNotifications = true
        }
        else {
            settingsMap.mentionNotifications = false
        }

        def settings = settingsService.updateSettingsForUser(user, settingsMap)

         if (settings.hasErrors()) {
            render(view: '/settings/settings')
        } else {
            flash.message = message(code: 'useraccount.update.success')
            redirect(uri: '/settings/doSettings')
        }
    }

}
