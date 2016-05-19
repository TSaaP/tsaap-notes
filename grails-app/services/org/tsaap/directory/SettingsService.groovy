package org.tsaap.directory

import grails.transaction.Transactional

@Transactional
class SettingsService {

    def updateSettingsForUser(User user, Map settingsMap) {
        if (user.settings) {
            user.settings.properties = settingsMap
            user.settings.save()
        } else {
            Settings settings = new Settings()
            settings.properties = settingsMap
            settings.save()
            user.settings = settings
            user.save()
        }
        user.settings
    }
}
