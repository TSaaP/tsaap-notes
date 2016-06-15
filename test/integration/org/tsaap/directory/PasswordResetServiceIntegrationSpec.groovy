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
import org.gcontracts.PreconditionViolation
import org.tsaap.BootstrapTestService
import org.tsaap.attachement.Attachement
import org.tsaap.attachement.AttachementDto
import org.tsaap.attachement.AttachementService
import org.tsaap.notes.*
import spock.lang.Specification

class PasswordResetServiceIntegrationSpec extends Specification {

    def passwordResetService
    def bootstrapTestService

    def lifetime
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
        key2.save(flush: true)
        def key3val = passwordResetService.generatePasswordResetKeyForUser(bootstrapTestService.learnerPaul).passwordResetKey

        then: "the key is different"
        key2val != key3val
        PasswordResetKey.count == count + 1
    }
}
