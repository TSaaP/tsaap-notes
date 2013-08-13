/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tsaap.directory

class User {

  transient springSecurityService

  String firstName
  String lastName
  String username
  String normalizedUsername
  String email
  String password
  String fullname
  boolean enabled
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired

  void setUsername(String val) {
    this.username = val
    normalizedUsername = val?.toLowerCase()
  }

  static constraints = {
    firstName blank: false
    lastName blank: false
    normalizedUsername blank: false, unique: true
    username blank: false, unique: true, validator: { val ->
      val ==~ /^[a-zA-Z0-9_]{1,15}$/
    }
    password blank: false, minSize: 4
    email email: true, unique: true
  }

  static mapping = {
    password column: '`password`'
  }

  static transients = ['fullname']

  String getFullname() {
    "$firstName $lastName"
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
