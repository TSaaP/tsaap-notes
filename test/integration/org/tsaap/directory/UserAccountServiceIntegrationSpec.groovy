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

import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.BootstrapService
import spock.lang.Specification

class UserAccountServiceIntegrationSpec extends Specification {

  BootstrapService bootstrapService
  UserAccountService userAccountService
  SpringSecurityService springSecurityService

  def "add user"() {
    bootstrapService.initializeRoles()

    when: "adding a user"
    def u = userAccountService.addUser(new User(firstName: "Mary", lastName: "S",
                                              username: "Mary_test",
                                              email: "mary@nomail.com",
                                              password: "password"),
                                     RoleEnum.STUDENT_ROLE.role)
    println "${u.errors}"

    then: "the user is persisted in the datastore"
    User user = User.findByUsername("Mary_test")
    user != null
    !user.enabled
    user.normalizedUsername == "mary_test"
    user.email == "mary@nomail.com"
    !user.authorities.empty
    user.authorities.first().authority == RoleEnum.STUDENT_ROLE.name()
    user.password == springSecurityService.encodePassword("password")
  }

  def "enable user"() {
    bootstrapService.initializeRoles()

    when:
    def mary = userAccountService.addUser(new User(firstName: "Mary", lastName: "S",
                                                   username: "Mary_test",
                                                   email: "mary@nomail.com",
                                                   password: "password"),
                                          RoleEnum.STUDENT_ROLE.role)
    userAccountService.enableUser(mary)

    then:
    User user = User.findByUsername("Mary_test")
    user != null
    user.enabled
  }

  def "disable user"() {
    bootstrapService.initializeRoles()

    when:
    def mary = userAccountService.addUser(new User(firstName: "Mary", lastName: "S",
                                                   username: "Mary_test",
                                                   email: "mary@nomail.com",
                                                   password: "password"),
                                          RoleEnum.STUDENT_ROLE.role)
    userAccountService.enableUser(mary)
    userAccountService.disableUser(mary)

    then:
    User user = User.findByUsername("Mary_test")
    user != null
    !user.enabled
  }

}
