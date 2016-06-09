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
