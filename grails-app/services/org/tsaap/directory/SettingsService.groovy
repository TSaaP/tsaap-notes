package org.tsaap.directory

import grails.transaction.Transactional

@Transactional
class SettingsService {

    /**
     * Update existing settings for a user
     * @param user
     * @param settingsMap a map with the settings attributes to set
     * @return the processed user
     */
    Settings updateSettingsForUser(User user, Map settingsMap) {
        user.settings.properties = settingsMap
        user.settings.save()
        user.settings
    }

    /**
     * Initialize settings for a new user
     * @param user
     * @return the processed user
     */
    Settings initializeSettingsForUser(User user) {
        Settings settings = new Settings()
        settings.dailyNotifications = true
        settings.mentionNotifications = true
        settings.user = user
        settings.save()
        settings
    }
}
