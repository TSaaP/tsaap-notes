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
import org.apache.commons.lang.time.DateUtils

@Transactional
class ActivationKeyService {

    def grailsApplication

    /**
     * Remove all activationKeys older than 3 (or custom value) hours and corresponding user who didn't activate their
     * accounts
     * The maximum lifetime is expressed in the parameter tsaap.auth.activation_key.lifetime_in_hours
     * @return
     */
    def removeOldActivationKeys() {
        def lifetime = grailsApplication.config.tsaap.auth.activation_key.lifetime_in_hours ?: 3
        def keys = ActivationKey.findAllByDateCreatedLessThan(DateUtils.addHours(new Date(), -lifetime))
        ActivationKey.deleteAll(keys)
        def users = User.findAllByIdInListAndEnabled(keys.user.id, false)
        def settings = Settings.findAllByUserInList(users)
        def roles = UserRole.findAllByUserInList(users)
        def unsubKeys = UnsubscribeKey.findAllByUserInList(users)

        UnsubscribeKey.deleteAll(unsubKeys)
        UserRole.deleteAll(roles)
        Settings.deleteAll(settings)
        User.deleteAll(users)
    }
}
