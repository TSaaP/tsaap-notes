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

import groovy.sql.Sql
import org.tsaap.lti.LmsUserHelper
import spock.lang.Specification

import javax.sql.DataSource

/**
 * Created by dorian on 24/06/15.
 */
class UserProvisionAccountServiceIntegrationSpec extends Specification {

    UserProvisionAccountService userProvisionAccountService
    DataSource dataSource
    LmsUserHelper lmsUserHelper
    Sql sql

    def setup() {
        sql = new Sql(dataSource)
        lmsUserHelper = new LmsUserHelper()
        userProvisionAccountService.lmsUserHelper = lmsUserHelper
    }

    def "test generate username"() {

        when: "I want to generate a new username for two user with same quadrigram"
        def username = userProvisionAccountService.generateUsername(sql, "john", "doe")
        lmsUserHelper.insertUserInDatabase(sql, 'jdoe@mail.com', 'john', "doel'hh", 'jdoe', 'pass', true)
        def username2 = userProvisionAccountService.generateUsername(sql, 'jane', 'doe')
        lmsUserHelper.insertUserInDatabase(sql, 'jane@mail.com', 'jane', 'doe', 'jdoe2', 'passw', true)
        def username3 = userProvisionAccountService.generateUsername(sql, 'jean', 'doel')
        lmsUserHelper.insertUserInDatabase(sql, 'janine@mail.com', 'janine', 'doerel', 'jdoer', 'passw', true)
        def username4 = userProvisionAccountService.generateUsername(sql, 'julien', 'doelel')


        then: "I got a different username for the two users"
        username == "jdoe"
        username2 == "jdoe2"
        username3 == "jdoe3"
        username4 == "jdoe3"


    }

    def "test generate username with very short name"() {
        when: "I generate a username based on a very short name"
        def username = userProvisionAccountService.generateUsername(sql, "john", "do")

        then: "I got a username with the adhoc trigram"
        username == "jdo"

        when: "a user with quadrigramm already used exists"
        lmsUserHelper.insertUserInDatabase(sql, 'jdoe@mail.com', 'john', "do", 'jdo21', 'pass', true)
        def username2 = userProvisionAccountService.generateUsername(sql, "john", "do")
        lmsUserHelper.insertUserInDatabase(sql, 'jdoe@mail.com', 'john', "do", 'jdoka', 'pass', true)
        def username3 = userProvisionAccountService.generateUsername(sql, "john", "do")

        then: "I got a username with the adhoc trigram"
        username2 == "jdo22"
        username3 == "jdo22"

    }
}
