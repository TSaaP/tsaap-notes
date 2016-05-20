package org.tsaap.directory

import grails.transaction.Transactional

@Transactional
class SettingsService {

    Settings updateSettingsForUser(User user, Map settingsMap) {
        user.settings.properties = settingsMap
        user.settings.save()
        user.settings
    }

    Settings initializeSettingsForUser(User user) {
        Settings settings = new Settings()
        settings.dailyNotifications = true
        settings.mentionNotifications = true
        settings.user = user
        settings.save()
        settings
    }
}
