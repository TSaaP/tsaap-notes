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

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.gcontracts.PreconditionViolation
import org.tsaap.lti.LmsUserHelper
import org.tsaap.lti.LtiUserException

import javax.sql.DataSource

class UserAccountController {

    UserAccountService userAccountService
    SpringSecurityService springSecurityService
    LmsUserHelper lmsUserHelper
    DataSource dataSource
    Sql sql

    /**
     * Action allowing a user to subscribe
     * @param firsName the first name given by the user
     * @param lastName the last name given by the user
     * @param username the username given by the user
     * @param email the email given by the user
     * @param password the password given by the user
     * @param role the role given by the user
     */
    def doSubscribe() {
        Role mainRole = RoleEnum.valueOf(RoleEnum, params.role).role
        if (!userAccountService.languageIsSupported(params.language)) {
            params.language = 'en'
        }
        User user = new User(params)
        def checkEmail = grailsApplication.config.tsaap.auth.check_user_email ?: true
        if (params.password == params.password2) {
            user = userAccountService.addUser(user, mainRole, !checkEmail, params.language, checkEmail)
        } else {
            user.errors.rejectValue('password', 'user.password.confirm.fail', 'The two passwords must be the same.')
        }

        if (user.hasErrors()) {
            render(view: '/index', model: [user: user])
        } else {
            flash.message = message(code: checkEmail ? 'subscription.success.email_to_be_checked' : 'subscription.success')
            redirect(uri: '/login/auth')
        }
    }

    /**
     * Action allowing a update a user
     * @param firsName the first name given by the user
     * @param lastName the last name given by the user
     * @param username the username given by the user
     * @param email the email given by the user
     * @param password the password given by the user
     * @param role the role given by the user
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doUpdate() {
        User user = springSecurityService.currentUser
        if ((params.password || params.password2) && (params.password != params.password2)) {
            user.errors.rejectValue('password', 'user.password.confirm.fail', 'The two passwords must be the same.')
        } else {
            if (!params.password) {
                params.remove('password')
            }

            Role mainRole = RoleEnum.STUDENT_ROLE.role
            try {
                mainRole = RoleEnum.valueOf(RoleEnum, params.role).role
            } catch (Exception e) {
            }

            //params.language = userAccountService.LANGUAGES_SUPPORTED.get(params.language)
            user.properties = params
            user = userAccountService.updateUser(user, mainRole)
        }
        if (user.hasErrors()) {
            render(view: '/userAccount/edit', model: [user: user])
        } else {
            flash.message = message(code: 'useraccount.update.success')
            redirect(uri: '/userAccount/doEdit')
        }
    }

    /**
     *
     * Go to the edit page
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doEdit() {
        render(view: '/userAccount/edit', model: [user: springSecurityService.currentUser])
    }

    /**
     * Unsubscribe the current user
     * @return
     */
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doUnsubscribe() {
        User user = springSecurityService.currentUser
        userAccountService.disableUser(user)
        redirect(uri: '/logout')
    }

    /**
     * Enable a user
     * @param actKey the activation key
     * @param id the user id
     */
    def doEnableUser() {
        def strKey = params.actKey
        def id = params.id
        User user = User.get(params.id)
        ActivationKey actKey = ActivationKey.findByUserAndActivationKey(user, strKey)
        try {
            userAccountService.enableUserWithActivationKey(user, actKey)
        } catch (PreconditionViolation e) {
            flash.message = message(code: 'useraccount.activation.failure')
            throw e
        }
        flash.message = message(code: 'useraccount.update.success')
        redirect(uri: '/login/auth')
    }

    def ltiConnection() {
        if (params.agree == "true") {
            springSecurityService.reauthenticate(params.username)
            sql = new Sql(dataSource)
            lmsUserHelper = new LmsUserHelper()
            lmsUserHelper.enableUser(sql, params.username)
            redirect(controller: "questions", params: [contextId: params.contextId, contextName: params.contextName, displaysAll: params.displaysAll])
        } else {
            throw new LtiUserException("error.lti.user.agreement")
        }
    }
}
