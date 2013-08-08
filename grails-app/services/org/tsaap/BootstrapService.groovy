package org.tsaap

import groovy.sql.Sql
import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User

class BootstrapService {

  def dataSource

  Role studentRole
  Role teacherRole
  Role adminRole

  User adminUser


  def initializeReferenceData() {
    initializeRoles()
    initializeUsers()
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

  def initializeUsers() {
    adminUser = User.findByUsername("adminUser")
    if (!adminUser) {
      adminUser = new User(firstName: "admin", lastName: "admin", username: "admin", password: "admin",email: 'admin@admin.com').save()
    }
  }
}
