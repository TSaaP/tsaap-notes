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
import grails.transaction.NotTransactional
import groovy.sql.Sql
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.commons.csv.CSVRecord
import org.gcontracts.annotations.Requires
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.contracts.Contract

class UserAccountService {

    public static final String DEFAULT_SUBSCRIPTION_SOURCE = "elaastic"

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
    User addUser(User user, Role mainRole, Boolean enabled = false, String language = 'fr', boolean checkEmailAccount = false, String subscriptionSource= DEFAULT_SUBSCRIPTION_SOURCE) {
        user.enabled = enabled
        user.save()
        if (!user.hasErrors()) {
            UserRole.create(user, mainRole)
            if (checkEmailAccount) {
                ActivationKey actKey = new ActivationKey(activationKey: UUID.randomUUID().toString(), user: user, subscriptionSource: subscriptionSource)
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
     * @throws ConditionViolationException
     * @return the list of users
     */
    @NotTransactional
    List<User> addUserListByOwner(List<User> userList, User owner, String language = 'fr', String subscriptionSource = DEFAULT_SUBSCRIPTION_SOURCE) throws ConditionViolationException {
        Contract.requires(owner.canBeUserOwner, USER_MUST_BE_AUTHORIZED_TO_BE_OWNER)
        Sql sql = new Sql(dataSource)
        userList.each { User user ->
            if (!user.username) {
                user.username = userProvisionAccountService.generateUsername(sql, user.firstName, user.lastName)
                user.clearPassword = userProvisionAccountService.generatePassword()
                user.password = user.clearPassword
            }
            user.owner = owner
            addUser(user, RoleEnum.STUDENT_ROLE.role, true, language,false, subscriptionSource)
        }
        sql.close()
        userList
    }

    /**
     * Add user list from a file reader CSV formatted
     * @param fileReader the file reader
     * @param owner the owner who trys to add user
     * @param language the favorite language of added users
     * @return the user list
     * @throws ConditionViolationException
     */
    @NotTransactional
    List<User> addUserListFromFileByOwner(InputStreamReader fileReader, User owner, String language= 'fr', String subscriptionSource = DEFAULT_SUBSCRIPTION_SOURCE) throws ConditionViolationException {
        Contract.requires(owner.canBeUserOwner, USER_MUST_BE_AUTHORIZED_TO_BE_OWNER)
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(";" as char).withFirstRecordAsHeader().parse(fileReader)
        List<User> userList = []
        for (CSVRecord record : records) {
            def user = new User(lastName: record.get(0), firstName: record.get(1))
            if (record.size() >= 4) {
                user.username = record.get(2)
                user.password = record.get(3)
                user.clearPassword = record.get(3)
            }
            userList << user
        }
        addUserListByOwner(userList,owner,language, subscriptionSource)
    }

    /**
     * Print user list in a csv file
     * @param userList the user list
     * @param fileWriter the file write
     * @return the file writer
     */
    @NotTransactional
    FileWriter printUserListInCSVFile(List<User> userList, FileWriter fileWriter) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(";" as char)
        CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFormat)
        try {
            csvPrinter.printRecord(FILE_HEADER)
            userList.each { User user ->
                def status = user.hasErrors() ? StatusType.INVALID.name() : StatusType.OK.name()
                def username = user.hasErrors() ? "" : user.username
                def password = user.hasErrors() ? "" : user.clearPassword
                csvPrinter.printRecord([user.lastName, user.firstName, username, password, status])
            }
        } catch (Exception e) {
            log.error(e.message)
        } finally {
            csvPrinter.close()
        }
        fileWriter
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
    //CSV file header
    private static final Object [] FILE_HEADER = ["Last name", "First name", "User name (login)", "password", "Status"] as Object[]
    private static final STATUS

}

enum StatusType {
    OK,
    INVALID
}