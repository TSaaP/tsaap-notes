package org.tsaap

import groovy.sql.Sql
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService
import org.tsaap.notes.Context
import org.tsaap.notes.ContextService

class BootstrapService {

  def dataSource
  UserAccountService userAccountService
  ContextService contextService


  Role studentRole
  Role teacherRole
  Role adminRole

  User fsil
  User mary

  Context science


  def initializeReferenceData() {
    initializeRoles()
  }

  def initializeRoles() {
    Sql sql = new Sql(dataSource)
    studentRole = Role.findByAuthority(RoleEnum.STUDENT_ROLE.name())
    if (!studentRole) {
      sql.executeInsert("insert into role (id,authority) values (2,${RoleEnum.STUDENT_ROLE.name()})")
      studentRole = RoleEnum.STUDENT_ROLE.role
    }
    teacherRole = Role.findByAuthority(RoleEnum.TEACHER_ROLE.name())
    if (!teacherRole) {
      sql.executeInsert("insert into role (id,authority) values (3,${RoleEnum.TEACHER_ROLE.name()})")
      teacherRole = RoleEnum.TEACHER_ROLE.role
    }
    adminRole = Role.findByAuthority(RoleEnum.ADMIN_ROLE.name())
    if (!adminRole) {
      sql.executeInsert("insert into role (id,authority) values (1,${RoleEnum.ADMIN_ROLE.name()})")
      adminRole = RoleEnum.ADMIN_ROLE.role
    }
  }

  def inializeDevUsers() {
    fsil = User.findByUsername("fsil")
    if (!fsil) {
      def user = new User(firstName: "Franck", lastName: "Sil", username: "fsil", password: "1234", email: 'fsil@fsil.com')
      fsil = userAccountService.addUser(user, studentRole, true)

    }
    mary = User.findByUsername("mary")
    if (!mary) {
      def user = new User(firstName: "Mary", lastName: "Sil", username: "mary", password: "1234", email: 'mary@mary.com')
      mary = userAccountService.addUser(user, teacherRole, true)
    }
  }


  def initializeDevContext() {
    science = Context.findByContextName('science')
    if (!science) {
      science = contextService.saveContext(new Context(owner: mary, contextName: 'science', descriptionAsNote: "everything about #science, but sur mainly on #computer", url: 'http://fr.wikipedia.org/wiki/Science'))
    }
  }

}
