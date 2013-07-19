package org.tsaap

import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User

class BootstrapService {

  Role studentRole
  Role teacherRole
  Role adminRole

  User adminUser


  def initializeReferenceData() {
    initializeRoles()
    initializeUsers()
  }

  def initializeRoles() {
    studentRole = Role.findByAuthority(RoleEnum.STUDENT_ROLE.name())
    if (!studentRole) {
      studentRole = new Role(authority: RoleEnum.STUDENT_ROLE.name()).save()
    }
    teacherRole = Role.findByAuthority(RoleEnum.TEACHER_ROLE.name())
    if (!teacherRole) {
      teacherRole = new Role(authority: RoleEnum.TEACHER_ROLE.name()).save()
    }
    adminRole = Role.findByAuthority(RoleEnum.ADMIN_ROLE.name())
    if (!adminRole) {
      adminRole = new Role(authority: RoleEnum.ADMIN_ROLE.name()).save()
    }
  }

  def initializeUsers() {
    adminUser = User.findByUsername("adminUser")
    if (!adminUser) {
      adminUser = new User(username: "admin", password: "admin").save()
    }
  }
}
