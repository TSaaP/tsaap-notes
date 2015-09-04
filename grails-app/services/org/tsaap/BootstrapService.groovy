package org.tsaap

import groovy.sql.Sql
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService
import org.tsaap.notes.Context
import org.tsaap.notes.ContextService
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService
import org.tsaap.notes.Tag

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
    User admin

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
            def user = new User(firstName: "Admin", lastName: "Tsaap", username: "admin", password: "1234", email: 'admin@tsaap.org', language: 'en')
            admin = userAccountService.addUser(user, adminRole, true)
        }
    }

    def inializeDevUsers() {
        fsil = User.findByUsername("fsil")
        if (!fsil) {
            def user = new User(firstName: "Franck", lastName: "Sil", username: "fsil", password: "1234", email: 'fsil@fsil.com', language: 'fr')
            fsil = userAccountService.addUser(user, studentRole, true)

        }
        mary = User.findByUsername("mary")
        if (!mary) {
            def user = new User(firstName: "Mary", lastName: "Sil", username: "mary", password: "1234", email: 'mary@mary.com', language: 'fr')
            mary = userAccountService.addUser(user, teacherRole, true)
        }
        thom = User.findByUsername("thom")
        if (!thom) {
            def user = new User(firstName: "Thom", lastName: "Thom", username: "thom", password: "1234", email: 'thom@thom.com', language: 'fr')
            thom = userAccountService.addUser(user, studentRole, true)
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
        if(!football) {
            football = contextService.saveContext(new Context(owner: thom, contextName: 'football',descriptionAsNote: 'everything about #football',url: 'http://fr.wikipedia.org/wiki/Football', source: null))
        }
        goal = Tag.findOrSaveWhere(name: 'goal')
        for (int i=0 ; i<10 ; i++) {
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

}
