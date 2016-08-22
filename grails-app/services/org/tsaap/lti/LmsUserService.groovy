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
     * @param sql the sql object
     * @param lmsUser the lms user to find or create
     * @return the lms user
     */
    LmsUser findOrCreateUser(Sql sql, LmsUser lmsUser) {
        lmsUser.userId = lmsUserHelper.findUserIdForLtiUserId(sql, lmsUser.ltiUserId, lmsUser.ltiConsumerKey)
        if (lmsUser.userId == null) {
            createUser(sql, lmsUser)
        } else {
            updateUsernamePasswordAndIsEnabled(sql, lmsUser)
        }
        if (lmsUser.isEnabled) {
            springSecurityService.reauthenticate(lmsUser.username, lmsUser.password)
        }
        lmsUser
    }

    private void updateUsernamePasswordAndIsEnabled(Sql sql, LmsUser lmsUser) {
        def usernameAndPassword = lmsUserHelper.selectUsernameAndPassword(sql, lmsUser.ltiUserId)
        lmsUser.username = usernameAndPassword.username
        lmsUser.password = usernameAndPassword.password
        lmsUser.isEnabled = lmsUserHelper.selectUserIsEnable(sql, lmsUser.username)
    }

    private void createUser(Sql sql, LmsUser lmsUser) {
        lmsUser.username = userProvisionAccountService.generateUsername(sql, lmsUser.firstname, lmsUser.lastname)
        lmsUser.password = userProvisionAccountService.generatePassword()
        lmsUserHelper.insertUserInDatabase(sql, lmsUser)
        def roleId = lmsUser.isLearner ? RoleEnum.STUDENT_ROLE.id : RoleEnum.TEACHER_ROLE.id
        lmsUserHelper.insertUserRoleInDatabase(sql, roleId,  lmsUser.userId)
        lmsUserHelper.insertLmsUserInDatabase(sql, lmsUser.userId, lmsUser.ltiConsumerKey, lmsUser.ltiUserId)
    }
}
