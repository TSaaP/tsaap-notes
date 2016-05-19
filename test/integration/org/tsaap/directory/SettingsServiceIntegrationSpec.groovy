package org.tsaap.directory

import spock.lang.Specification

class SettingsServiceIntegrationSpec extends Specification {

    UserAccountService userAccountService
    SettingsService settingsService

    void "test adding settings for a user"() {
        given: "an user without settings"
        def user = userAccountService.addUser(new User(firstName: "Alexandre", lastName: "Lagane",
                username: "Alex_test",
                email: "alag@nomail.com",
                password: "password",
                language: 'en'),
                RoleEnum.STUDENT_ROLE.role)

        when: "settings are updated for this user"
        def settings = settingsService.updateSettingsForUser(user, ["dailyNotifications": true, "mentionNotifications": true])

        then: "settings is correctly added in the base"
        def dbSettings = Settings.findAllById(settings.id)
        !dbSettings.dailyNotifications
        !dbSettings.mentionNotifications

        and:
        Settings.count() == 1
    }

    void "test modifying settings for a user"() {
        given: "an user with settings"
        def user = userAccountService.addUser(new User(firstName: "Alexandre", lastName: "Lagane",
                username: "Alex_test",
                email: "alag@nomail.com",
                password: "password",
                language: 'en'),
                RoleEnum.STUDENT_ROLE.role)
        settingsService.updateSettingsForUser(user, ["dailyNotifications": false, "mentionNotifications": false])

        when: "settings are updated for this user"
        def settings = settingsService.updateSettingsForUser(user, ["dailyNotifications": true, "mentionNotifications": true])

        then: "settings is correctly updated in the base"
        def dbSettings = Settings.findAllById(settings.id)
        dbSettings.dailyNotifications
        dbSettings.mentionNotifications

        and:
        Settings.count() == 1
    }
}
