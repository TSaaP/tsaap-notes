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

import org.apache.commons.lang.time.DateUtils
import spock.lang.Specification

/**
 *
 */
class ActivationKeyServiceIntegrationSpec extends Specification {

    ActivationKey activationKey
    ActivationKeyService activationKeyService
    UserAccountService userAccountService

    User user1
    User user2
    User user3
    def lifetime
    def grailsApplication

    def setup() {
        user1 = userAccountService.addUser(new User(
                firstName: "moghite", lastName: "kacimi", username: "akac",
                password: "password", email: "akac@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role)
        user2 = userAccountService.addUser(new User(
                firstName: "moghite2", lastName: "kacimi", username: "akac2",
                password: "password", email: "akac2@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role)
        user3 = userAccountService.addUser(new User(
                firstName: "moghite3", lastName: "kacimi", username: "akac3",
                password: "password", email: "akac3@nomail.com", language: 'fr'), RoleEnum.STUDENT_ROLE.role, true)
        ActivationKey.deleteAll()
        lifetime = grailsApplication.config.tsaap.auth.activation_key.lifetime_in_hours ?: 3
    }

    def cleanup() {
        if (user1)
            user1.delete()
        if (user2)
            user2.delete()
        if (user3)
            user3.delete()
    }

    void "test removeOldActivationKeys"() {

        given: "Create activation key for user1"
        activationKey = new ActivationKey(activationKey: "Key", activationEmailSent: false, dateCreated: new Date(), user: user1)
        activationKey.save()

        expect: "Users and activation keys are presents"
        ActivationKey.count() == 1
        User.count() == 13 // because of fake users and admin

        when: "Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then: "Count number of ActivationKey and number of user"
        ActivationKey.count() == 1
        User.count() == 13

        when: "Create activation key for user2"
        ActivationKey activationKey2 = new ActivationKey(activationKey: "Key", activationEmailSent: false, user: user2)
        activationKey2.save()
        activationKey2.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        activationKey2.save()

        and: "Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then: "Count number of ActivationKey and number of user"
        ActivationKey.count() == 1
        User.count() == 12

        when: "Create activation key for user3"
        ActivationKey activationKey3 = new ActivationKey(activationKey: "Key", activationEmailSent: true, user: user3)
        activationKey3.save()
        activationKey3.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        activationKey3.save()

        and: "Start Activation Key garbage collector job "
        activationKeyService.removeOldActivationKeys()

        then: "Count number of ActivationKey and number of user"
        ActivationKey.count() == 1
        User.count() == 12
    }
}
