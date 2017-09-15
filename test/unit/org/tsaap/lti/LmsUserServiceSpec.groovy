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

package org.tsaap.lti

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import groovy.sql.Sql
import org.tsaap.directory.UserProvisionAccountService
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LmsUserService)
class LmsUserServiceSpec extends Specification {

    LmsUserService lmsUserService
    SpringSecurityService springSecurityService
    LmsUserHelper lmsUserHelper
    UserProvisionAccountService userProvisionAccountService
    Sql sql
    LmsUser lmsUser
    LmsUser lmsUserTeacher

    def setup() {
        lmsUserService = new LmsUserService()
        lmsUser = new LmsUser(ltiUserId: '10',ltiConsumerKey: 'key', firstname: 'john',lastname: 'doe',
                email: 'doe@nomail.com',isLearner: true)
        lmsUserTeacher = new LmsUser(ltiUserId: '11',ltiConsumerKey: 'key', firstname: 'jean',lastname: 'test',
                email: 'test@nomail.com',isLearner: false)
    }

    void "test to find or create a tsaap note account for given lti users without a tsaap account"() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsUserHelper = Mock(LmsUserHelper)
        springSecurityService = Mock(SpringSecurityService)
        userProvisionAccountService = Mock(UserProvisionAccountService)
        lmsUserService.lmsUserHelper = lmsUserHelper
        lmsUserService.userProvisionAccountService = userProvisionAccountService
        lmsUserService.springSecurityService = springSecurityService

        when: "I try to found or create a tsaap account for a lti user as learner"
        def res = lmsUserService.findOrCreateUser(sql, lmsUser)

        then: "The user is log with his learner created account"
        1 * lmsUserHelper.findUserIdForLtiUserId(sql, '10','key') >> null
        1 * userProvisionAccountService.generateUsername(sql, "john", "doe") >> "jdoe"
        1 * userProvisionAccountService.generateEncodedPassword() >> "pass"
        1 * lmsUserHelper.insertUserInDatabase(sql, lmsUser)
        1 * lmsUserHelper.insertUserRoleInDatabase(sql, 2, lmsUser.userId)
        1 * lmsUserHelper.insertLmsUserInDatabase(sql, lmsUser.userId, 'key', '10')
        res.username == 'jdoe'
        !res.isEnabled

        when: "I try to found or create a tsaap account for a lti user as a teacher"
        res = lmsUserService.findOrCreateUser(sql, lmsUserTeacher)

        then: "the user is log with his teacher created account"
        1 * lmsUserHelper.findUserIdForLtiUserId(sql, '11','key') >> null
        1 * userProvisionAccountService.generateUsername(sql, "jean", "test") >> "jtes"
        1 * userProvisionAccountService.generateEncodedPassword() >> "password"
        1 * lmsUserHelper.insertUserInDatabase(sql, lmsUserTeacher)
        1 * lmsUserHelper.insertUserRoleInDatabase(sql, 3, lmsUser.userId)
        1 * lmsUserHelper.insertLmsUserInDatabase(sql, lmsUser.userId, 'key', '11')
        res.username == 'jtes'
        !res.isEnabled
    }

    void "test to find or create a tsaap note account for a given lti user who have a tsaap account"() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsUserHelper = Mock(LmsUserHelper)
        springSecurityService = Mock(SpringSecurityService)
        userProvisionAccountService = Mock(UserProvisionAccountService)
        lmsUserService.lmsUserHelper = lmsUserHelper
        lmsUserService.userProvisionAccountService = userProvisionAccountService
        lmsUserService.springSecurityService = springSecurityService

        when: "I try to found or create a tsaap account for this lti user"
        def res = lmsUserService.findOrCreateUser(sql, lmsUser)

        then: "The user is log with his account"
        1 * lmsUserHelper.findUserIdForLtiUserId(sql, '10','key') >> 88
        1 * lmsUserHelper.selectUsernameAndPassword(sql, 'key','10') >> [username: "jdoe", password: "pass"]
        1 * lmsUserHelper.selectUserIsEnable(sql, "jdoe") >> true
        1 * springSecurityService.reauthenticate("jdoe", "pass")
        res.username == 'jdoe'
        res.isEnabled
    }
}
