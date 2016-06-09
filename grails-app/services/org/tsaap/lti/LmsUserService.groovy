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
import groovy.sql.Sql
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.UserProvisionAccountService


class LmsUserService {

    SpringSecurityService springSecurityService
    LmsUserHelper lmsUserHelper
    UserProvisionAccountService userProvisionAccountService

    /**
     * Find or create a tsaap account for a given lti user
     * @param ltiUserId lti user id
     * @param firstName lti user first name
     * @param lastName lti user last name
     * @param email lti user email
     * @param key lti consumer key
     * @param role true if the lti user is a learner else false
     */
    def findOrCreateUser(Sql sql, String ltiUserId, String firstName, String lastName, String email, String key, Boolean isLearner) {
        String username
        def password
        Boolean enable
        // Verify if the user have already an account
        def result = lmsUserHelper.selectLmsUser(sql, ltiUserId)
        if (result == null) {
            // If not, create an account for it
            username = userProvisionAccountService.generateUsername(sql, firstName, lastName)
            password = userProvisionAccountService.generatePassword()

            // Add user in database
            lmsUserHelper.insertUserInDatabase(sql, email, firstName, lastName, username, password, false)
            Long tsaapUserId = lmsUserHelper.selectUserId(sql, username)
            if (isLearner) {
                lmsUserHelper.insertUserRoleInDatabase(sql, RoleEnum.STUDENT_ROLE.id, tsaapUserId)
            } else {
                lmsUserHelper.insertUserRoleInDatabase(sql, RoleEnum.TEACHER_ROLE.id, tsaapUserId)
            }
            lmsUserHelper.insertLmsUserInDatabase(sql, tsaapUserId, key, ltiUserId)
            enable = false

        } else {
            // Get the user username and password to connect it
            def user = lmsUserHelper.selectUsernameAndPassword(sql, ltiUserId)
            username = user.username
            password = user.password
            enable = lmsUserHelper.selectUserIsEnable(sql, username)
        }
        if (enable) {
            springSecurityService.reauthenticate(username, password)
        }
        [username, enable]
    }
}
