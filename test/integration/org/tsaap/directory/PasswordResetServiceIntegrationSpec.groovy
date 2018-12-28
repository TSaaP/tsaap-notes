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

class PasswordResetServiceIntegrationSpec extends Specification {

    def passwordResetService
    def bootstrapTestService

    int lifetime
    def grailsApplication

    def setup() {
        lifetime = grailsApplication.config.tsaap.auth.password_reset_key.lifetime_in_hours ?: 1
        bootstrapTestService.initializeTests()
    }

    def "generate and update a new key"() {
        when: "a key is generated for a new user"
        def count = PasswordResetKey.count()
        def key1val = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul).passwordResetKey

        then: "the key is added in database"
        PasswordResetKey.count == count + 1

        when: "the user asks again for a key while its old key is still active"
        def key2 = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul)
        def key2val = key2.passwordResetKey

        then: "the key is the same"
        key1val == key2val

        when: "they user asks again for a key when its old key is obsolete"
        key2.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        key2.save()
        def key3val = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul).passwordResetKey

        then: "the key is different"
        key2val != key3val
        PasswordResetKey.count == count + 1
    }

    def "find password reset key mails to send"() {
        when: "a key is added for a user"
        def key = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul)

        then: "the key is found"
        key == passwordResetService.findAllPasswordResetKey(UserAccountService.DEFAULT_SUBSCRIPTION_SOURCE)[0]

        when: "the key is marked as sent"
        key.passwordResetEmailSent = true
        key.save()

        then: "the key is not found"
        passwordResetService.findAllPasswordResetKey(UserAccountService.DEFAULT_SUBSCRIPTION_SOURCE).size() == 0

        when: "a new key is added and is obsolete"
        def obsKey = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerMary)
        obsKey.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        obsKey.save()

        then: "the key is not found"
        passwordResetService.findAllPasswordResetKey(UserAccountService.DEFAULT_SUBSCRIPTION_SOURCE).size() == 0
    }

    def "remove old keys"() {
        given: "a key is still valid"
        def validKey = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul)

        and: "a key is too old"
        def obsoleteKey = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerMary)
        obsoleteKey.dateCreated = DateUtils.addHours(new Date(), -lifetime - 1)
        obsoleteKey.save()

        when: "the old keys are removed"
        passwordResetService.removeOldPasswordResetKeys()

        then: "the valid key is in the database"
        def keys = PasswordResetKey.findAll()
        keys.contains(validKey)

        and: "the obsolete key is not in the database"
        !keys.contains(obsoleteKey)
    }

    /*
    def "sent password reset key are sent"() {
        given: "a key in the database"
        def key = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul)

        when: "we send paswword reset keys mails"
        passwordResetService.sendPasswordResetKeyMessages()

        then: "the key is marked as sent"
        key.passwordResetEmailSent
    }
    */
}
