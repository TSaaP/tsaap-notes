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

        when: "I want to generate a username when there is not already the same username in the database"
        def username = userProvisionAccountService.generateUsername(sql, "John", "Dorel")

        then: "I obtain a username without index as suffix"
        username == 'johdore'

        when:"when the username exists"
        lmsUserHelper.insertUserInDatabase(sql, 'jdoe@mail.com', 'john', "doel'hh", 'johdore', 'pass', true)

        and:"I generate a username"
        def username2 = userProvisionAccountService.generateUsername(sql, 'johan', 'dorelia')

        then: "the generation takes into account the existing username and adds an index at the end of the generated username"
        username2 == 'johdore2'

        when:"the username exists with numerical suffix"
        lmsUserHelper.insertUserInDatabase(sql, 'jane@mail.com', 'jane', 'doe', 'johdore15', 'passw', true)

        and:"I generate a username"
        def username3 = userProvisionAccountService.generateUsername(sql, 'johaquim', 'dorelabel')

        then: "the generation takes into account the existing username and adds an index at the end of the generated username"
        username3 == 'johdore16'

        when:"the username exists with a litteral suffix"
        lmsUserHelper.insertUserInDatabase(sql, 'janine@mail.com', 'janine', 'doerel', 'johdoreabcd', 'passw', true)

        and:"I generate a username"
        def username4 = userProvisionAccountService.generateUsername(sql, 'joham', 'doredeli')

        then: "the generation doesn't take into account the existing username with literal suffix"
        username4 == 'johdore16'


    }

    def "test generate username with very short name"() {
        when: "I generate a username based on a very short name"
        def username = userProvisionAccountService.generateUsername(sql, "jo", "do")

        then: "I got a username with the adhoc result"
        username == "jodo"

        when: "a user with quadrigramm already used exists"
        lmsUserHelper.insertUserInDatabase(sql, 'jdoe@mail.com', 'john', "do", 'jodo21', 'pass', true)
        def username2 = userProvisionAccountService.generateUsername(sql, "jo", "do")
        lmsUserHelper.insertUserInDatabase(sql, 'jdoe@mail.com', 'john', "do", 'jodoka', 'pass', true)
        def username3 = userProvisionAccountService.generateUsername(sql, "jo", "do")

        then: "I got a username with the adhoc result"
        username2 == "jodo22"
        username3 == "jodo22"
    }

    def "test generate username with accents"() {
        when: "I generate a username with firsname and lastname with  accents"
        def username = userProvisionAccountService.generateUsername(sql, "Jérémie", "DÖrel")

        then: "I got a username with the adhoc result"
        username == "jerdore"
    }

    def "test generate username with spaces in firstname or lastname"() {
        when: "I generate a username with firsname and lastname  with an whitespaces "
        def username = userProvisionAccountService.generateUsername(sql, "El Medie", "Ma Patrick")

        then: "I got a username with the adhoc result"
        username == "elmmapa"
    }
}
