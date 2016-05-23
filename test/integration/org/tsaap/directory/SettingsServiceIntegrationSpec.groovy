package org.tsaap.directory

import spock.lang.Specification

class SettingsServiceIntegrationSpec extends Specification {

    UserAccountService userAccountService
    SettingsService settingsService

    void "test adding settings for a user"() {
        given: "an user with settings"
        def settingsCount = Settings.count()
        def user = userAccountService.addUser(new User(firstName: "Alexandre", lastName: "Lagane",
                username: "Alex_test",
                email: "alag@nomail.com",
                password: "password",
                language: 'en'),
                RoleEnum.STUDENT_ROLE.role)

        expect: "one settings entry is added"
        Settings.count() == settingsCount + 1

        when: "settings are updated for this user"
        def settings = settingsService.updateSettingsForUser(user, ["dailyNotifications": true, "mentionNotifications": false])
        def dbSettings = Settings.findById(settings.id)

        then: "dailyNotifications has the correct value in the base"
        dbSettings.dailyNotifications

        and: "mentionNotifications has the correct value in the base"
        !dbSettings.mentionNotifications

        and: "no more settings entry is added"
        Settings.count() == settingsCount + 1
    }
}
