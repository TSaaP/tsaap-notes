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
    Settings initializeSettingsForUser(User user, String language) {
        Settings settings = new Settings()
        settings.dailyNotifications = true
        settings.mentionNotifications = true
        settings.user = user
        settings.language = language
        settings.save()
        settings
    }
}
