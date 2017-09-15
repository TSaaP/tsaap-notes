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

import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.gcontracts.annotations.Requires
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.contracts.Contract

class UserAccountService {

    SettingsService settingsService
    UserProvisionAccountService userProvisionAccountService
    SpringSecurityService springSecurityService
    def dataSource

    private static final HashMap<String, String> LANGUAGES_SUPPORTED = [
            'English' : 'en',
            'Français': 'fr'
    ]

    /**
     * Check if the language passed in parameter is in LANGUAGES_SUPPORTED list
     * @param language the language to check
     * @return if LANGUAGES_SUPPORTED contains language
     */
    boolean languageIsSupported(String language) {
        LANGUAGES_SUPPORTED.containsValue(language)
    }

    /**
     * Add a new user, the user is not enabled
     * @param enabled flag that indicate if the account is enabled
     * @param mainRole the main role for the user
     * @param user the user to be added
     * @return the created user
     */
    User addUser(User user, Role mainRole, Boolean enabled = false, String language = 'fr', boolean checkEmailAccount = false) {
        user.enabled = enabled
        user.save()
        if (!user.hasErrors()) {
            UserRole.create(user, mainRole)
            if (checkEmailAccount) {
                ActivationKey actKey = new ActivationKey(activationKey: UUID.randomUUID().toString(), user: user)
                actKey.save()
            }
            UnsubscribeKey unsubKey = new UnsubscribeKey(unsubscribeKey: UUID.randomUUID().toString(), user: user)
            unsubKey.save()
            user.settings = settingsService.initializeSettingsForUser(user, language)
        }
        user
    }

    /**
     * Add a list of users given by an "owner" of these users. The users are considered as students and immediatly enabled.
     * @param users the user list
     * @param owner the owner
     * @param language the favorite language for these users
     * @return the list of users
     */
    List<User> addUserListByOwner(List<User> userList, User owner, String language = 'fr') throws ConditionViolationException {
        Contract.requires(owner.canBeUserOwner, USER_MUST_BE_AUTHORIZED_TO_BE_OWNER)
        Sql sql = new Sql(dataSource)
        userList.each { User user ->
            user.username = userProvisionAccountService.generateUsername(sql, user.firstName, user.lastName)
            user.clearPassword = userProvisionAccountService.generatePassword()
            user.password = user.clearPassword
            user.owner = owner
            addUser(user, RoleEnum.STUDENT_ROLE.role, true, language)
        }
        sql.close()
        userList
    }

    /**
     * Update the user
     * @param user the user
     * @param mainRole the main role
     * @return the updated user
     */
    User updateUser(User user, Role mainRole) {
        user.save()
        if (!user.hasErrors() && !user.isAdmin() && !UserRole.get(user.id, mainRole.id)) {
            UserRole.removeAll(user)
            UserRole.create(user, mainRole)
        }
        user
    }

    /**
     * Enable a user
     * @param user the user to enabled
     * @return the processed user
     */
    User enableUser(User user) {
        user.enabled = true
        user.save()
        user
    }

    /**
     * Enable a user with an activation key
     * @param user
     * @param activationKey
     * @return
     */
    @Requires({
        user &&
                !user.enabled &&
                user == activationKey?.user
    })
    User enableUserWithActivationKey(User user, ActivationKey activationKey) {
        activationKey.delete()
        user.enabled = true
        user.save()
        user
    }

    /**
     * Disable a user
     * @param user the user to disabled
     * @return the processed user
     */
    User disableUser(User user) {
        user.enabled = false
        user.save()
        user
    }

    private final static String USER_MUST_BE_AUTHORIZED_TO_BE_OWNER = "User must be authorized to be owner"

}
