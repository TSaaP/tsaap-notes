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

package org.tsaap

import grails.plugin.spock.IntegrationSpec
import org.tsaap.directory.RoleEnum

class BootstrapServiceIntegrationSpec extends IntegrationSpec {

  BootstrapService bootstrapService

  def "role initialization"() {

    when:
      bootstrapService.initializeRoles()

    then:
      bootstrapService.studentRole != null
      bootstrapService.studentRole == RoleEnum.STUDENT_ROLE.role
      bootstrapService.teacherRole != null
      bootstrapService.teacherRole == RoleEnum.TEACHER_ROLE.role
      bootstrapService.adminRole != null
      bootstrapService.adminRole == RoleEnum.ADMIN_ROLE.role

  }

  def "admin user"() {
    when:
      bootstrapService.initializeUsers()
    then:
      bootstrapService.adminUser != null
      bootstrapService.adminUser.username == "admin"
  }


}
