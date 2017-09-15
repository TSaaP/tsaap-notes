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
import org.tsaap.BootstrapService
import org.tsaap.lti.LmsUser
import org.tsaap.lti.LmsUserHelper
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.SQLException

/**
 * Created by dorian on 24/06/15.
 */
class UserProvisionAccountServiceIntegrationSpec extends Specification {

    UserProvisionAccountService userProvisionAccountService
    BootstrapService bootstrapService
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
        LmsUser lmsUser = new LmsUser('email':'jdoe@mail.com', 'firstname':'john','lastname':"doel'hh",'username':'johdore','password':'pass','isEnabled':true)
        lmsUserHelper.insertUserInDatabase(sql, lmsUser)

        and:"I generate a username"
        def username2 = userProvisionAccountService.generateUsername(sql, 'johan', 'dorelia')

        then: "the generation takes into account the existing username and adds an index at the end of the generated username"
        username2 == 'johdore2'

        when:"the username exists with numerical suffix"
        LmsUser lmsUser2 = new LmsUser('email':'jane@mail.com', 'firstname':'jane','lastname':'doe','username':'johdore15','password':'passw','isEnabled':true)
        lmsUserHelper.insertUserInDatabase(sql,lmsUser2)

        and:"I generate a username"
        def username3 = userProvisionAccountService.generateUsername(sql, 'johaquim', 'dorelabel')

        then: "the generation takes into account the existing username and adds an index at the end of the generated username"
        username3 == 'johdore16'

        when:"the username exists with a litteral suffix"
        LmsUser lmsUser3 = new LmsUser('email':'janine@mail.com', 'firstname':'janine','lastname':'doerel','username':'johdoreabcd','password':'passw','isEnabled':true)
        lmsUserHelper.insertUserInDatabase(sql,lmsUser3)

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
        LmsUser lmsUser = new LmsUser('email':'jdoe@mail.com', 'firstname':'john','lastname':"do",'username':'jodo21','password':'pass','isEnabled':true)
        lmsUserHelper.insertUserInDatabase(sql, lmsUser)
        def username2 = userProvisionAccountService.generateUsername(sql, "jo", "do")
        LmsUser lmsUser2 = new LmsUser('email':'jdoe@mail.com', 'firstname':'john','lastname':"do",'username':'jodoka','password':'pass','isEnabled':true)
        lmsUserHelper.insertUserInDatabase(sql, lmsUser2)
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

    def "test select an username in database"() {

        given: "some users"
        bootstrapService.initializeRoles()
        bootstrapService.inializeDevUsers()


        when: "I want to know if an username in database begin with the username passed in parameter"
        def res = null
        def res2 = null
        try {
            sql.withTransaction { ->
                res = userProvisionAccountService.findMostRecentUsernameStartingWithUsername(sql, "fsil")
                res2 = userProvisionAccountService.findMostRecentUsernameStartingWithUsername(sql, "drol")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "an username is found for jdoe but not for drol"
        res == "fsil"
        res2 == null
    }
}
