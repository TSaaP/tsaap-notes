package org.tsaap

import groovy.sql.Sql
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService

class BootstrapService {

  def dataSource
  UserAccountService userAccountService


  Role studentRole
  Role teacherRole
  Role adminRole

  User fsil
  User mary


  def initializeReferenceData() {
    initializeRoles()
  }

  def initializeRoles() {
    Sql sql = new Sql(dataSource)
    studentRole = Role.findByAuthority(RoleEnum.STUDENT_ROLE.name())
    if (!studentRole) {
      sql.executeInsert("insert into role (id,authority,version) values (2,${RoleEnum.STUDENT_ROLE.name()},1)")
      studentRole = RoleEnum.STUDENT_ROLE.role
    }
    teacherRole = Role.findByAuthority(RoleEnum.TEACHER_ROLE.name())
    if (!teacherRole) {
      sql.executeInsert("insert into role (id,authority,version) values (3,${RoleEnum.TEACHER_ROLE.name()},1)")
      teacherRole = RoleEnum.TEACHER_ROLE.role
    }
    adminRole = Role.findByAuthority(RoleEnum.ADMIN_ROLE.name())
    if (!adminRole) {
      sql.executeInsert("insert into role (id,authority,version) values (1,${RoleEnum.ADMIN_ROLE.name()},1)")
      adminRole = RoleEnum.ADMIN_ROLE.role
    }

  }

  def inializeDevUsers() {
    fsil = User.findByUsername("fsil")
    if (!fsil) {
      def user = new User(firstName: "Franck", lastName: "Sil", username: "fsil", password: "1234", email: 'fsil@fsil.com')
      fsil =  userAccountService.addUser(user,studentRole, true)

    }
    mary = User.findByUsername("mary")
    if (!mary) {
      def user = new User(firstName: "Mary", lastName: "Sil", username: "mary", password: "1234", email: 'mary@mary.com')
      mary = userAccountService.addUser(user, teacherRole,true)
    }
  }

}
