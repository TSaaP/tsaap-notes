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

package org.tsaap

import groovy.sql.Sql
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService
import org.tsaap.directory.UserProvisionAccountService


class BootstrapService {

    def dataSource
    UserAccountService userAccountService
    UserProvisionAccountService userProvisionAccountService


    Role studentRole
    Role teacherRole
    Role adminRole

    User fsil
    User mary
    User thom
    User john
    User erik
    User admin

    List<User> fakeUserList


    def initializeReferenceData() {
        initializeRoles()
        initializeAdmin()
        initializeFakeUsers()
    }

    def initializeRoles() {
        Sql sql = new Sql(dataSource)
        studentRole = Role.findByRoleName(RoleEnum.STUDENT_ROLE.name())
        if (!studentRole) {
            sql.executeInsert("insert into role (id,authority) values (2,${RoleEnum.STUDENT_ROLE.name()})")
            studentRole = RoleEnum.STUDENT_ROLE.role
        }
        teacherRole = Role.findByRoleName(RoleEnum.TEACHER_ROLE.name())
        if (!teacherRole) {
            sql.executeInsert("insert into role (id,authority) values (3,${RoleEnum.TEACHER_ROLE.name()})")
            teacherRole = RoleEnum.TEACHER_ROLE.role
        }
        adminRole = Role.findByRoleName(RoleEnum.ADMIN_ROLE.name())
        if (!adminRole) {
            sql.executeInsert("insert into role (id,authority) values (1,${RoleEnum.ADMIN_ROLE.name()})")
            adminRole = RoleEnum.ADMIN_ROLE.role
        }
    }

    def initializeAdmin() {
        admin = User.findByUsername("admin")
        if (!admin) {
            def user = new User(firstName: "Admin", lastName: "Tsaap", username: "admin", password: "1234", email: 'admin@tsaap.org')
            admin = userAccountService.addUser(user, adminRole, true, 'en')
        }
    }

    def inializeDevUsers() {
        fsil = User.findByUsername("fsil")
        if (!fsil) {
            def user = new User(firstName: "Franck", lastName: "Sil", username: "fsil", password: "1234", email: 'fsil@fsil.com')
            fsil = userAccountService.addUser(user, teacherRole, true, 'fr')

        }
        mary = User.findByUsername("mary")
        if (!mary) {
            def user = new User(firstName: "Mary", lastName: "Sil", username: "mary", password: "1234", email: 'mary@mary.com')
            mary = userAccountService.addUser(user, studentRole, true, 'fr')
        }
        thom = User.findByUsername("thom")
        if (!thom) {
            def user = new User(firstName: "Thom", lastName: "Thom", username: "thom", password: "1234", email: 'thom@thom.com')
            thom = userAccountService.addUser(user, studentRole, true, 'fr')
        }
        john = User.findByUsername("john")
        if (!john) {
            def user = new User(firstName: "John", lastName: "John", username: "john", password: "1234", email: 'john@john.com')
            john = userAccountService.addUser(user, studentRole, true, 'fr')
        }
        erik = User.findByUsername("erik")
        if (!erik) {
            def user = new User(firstName: "Erik", lastName: "Erik", username: "erik", password: "1234", email: 'erik@erik.com')
            erik = userAccountService.addUser(user, studentRole, true, 'fr')
        }
    }

    def initializeFakeUsers() {
        fakeUserList = buildFakeUserList()
    }

    List<User> buildFakeUserList() {
        def res = []
        for(int i=1; i <= 9 ; i++) {
            def username ="John_Doe___${i}"
            def fakeUser = User.findByUsername(username)
            if (!fakeUser) {
                def user = new User(firstName: "John", lastName: "Doe", username: username, password: userProvisionAccountService.generatePassword(), email: "${username}@fakeuser.com")
                fakeUser = userAccountService.addUser(user, studentRole, true, 'fr')
            }
            res << fakeUser
        }
        res
    }


}
