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
import org.tsaap.notes.*

class BootstrapService {

    def dataSource
    UserAccountService userAccountService
    ContextService contextService
    NoteService noteService


    Role studentRole
    Role teacherRole
    Role adminRole

    User fsil
    User mary
    User thom
    User john
    User erik
    User admin

    User elaasticTeacher
    User elaasticLearner

    Context science
    Context football

    Tag goal

    Note note

    def initializeReferenceData() {
        initializeRoles()
        initializeAdmin()
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


    def initializeDevContext() {
        science = Context.findByContextName('science')
        if (!science) {
            science = contextService.saveContext(new Context(owner: mary, contextName: 'science', descriptionAsNote: "everything about #science, but sur mainly on #computer", url: 'http://fr.wikipedia.org/wiki/Science', source: null))
        }

    }

    def initializeDevContextWithFragment() {
        football = Context.findByContextName('football')
        if (!football) {
            football = contextService.saveContext(new Context(owner: thom, contextName: 'football', descriptionAsNote: 'everything about #football', url: 'http://fr.wikipedia.org/wiki/Football', source: null))
        }
        goal = Tag.findOrSaveWhere(name: 'goal')
        for (int i = 0; i < 10; i++) {
            note = Note.findByContent("goal$i")
            if (!note) {
                note = noteService.addStandardNote(thom, "goal$i", football, goal, null)
            }
        }
        note = Note.findByContent("goal11")
        if (!note) {
            note = noteService.addStandardNote(mary, "goal11", football, goal, null)
        }
        note = Note.findByContent("goal12")
        if (!note) {
            note = noteService.addStandardNote(fsil, "goal12", football, goal, null)
        }
    }

    def inializeElaasticUsers() {
      elaasticTeacher = User.findByUsername("elaT")
      if (!elaasticTeacher) {
          def user = new User(firstName: "ElaasticTeacher", lastName: "Ela", username: "elaT", password: "1234", email: 'elaT@elaT.com')
          elaasticTeacher = userAccountService.addUser(user, teacherRole, true, 'fr')

      }

      elaasticLearner = User.findByUsername("elaL")
      if (!elaasticLearner) {
          def user = new User(firstName: "ElaasticLearner", lastName: "Ela", username: "elaL", password: "1234", email: 'elaL@elaL.com')
          elaasticLearner = userAccountService.addUser(user, studentRole, true, 'fr')

      }
    }

}
