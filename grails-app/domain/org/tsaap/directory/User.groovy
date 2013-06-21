package org.tsaap.directory

class User {

  transient springSecurityService

  String username
  String password
  String globalId
  boolean enabled
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired

  void setUsername(String val) {
    this.@username = val.toLowerCase()
  }

  static constraints = {
    username blank: false, unique: true, validator: {
      val -> val.toLowerCase()=~/\w+/
    }
    password blank: false
  }

  static mapping = {
    password column: '`password`'
  }

  Set<Role> getAuthorities() {
    UserRole.findAllByUser(this).collect { it.role } as Set
  }

  def beforeInsert() {
    encodePassword()
  }

  def beforeUpdate() {
    if (isDirty('password')) {
      encodePassword()
    }
  }

  protected void encodePassword() {
    password = springSecurityService.encodePassword(password)
  }
}
