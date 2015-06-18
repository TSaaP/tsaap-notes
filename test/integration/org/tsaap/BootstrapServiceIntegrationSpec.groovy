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


import org.tsaap.directory.RoleEnum
import spock.lang.Specification

class BootstrapServiceIntegrationSpec extends Specification {

  BootstrapService bootstrapService

  def "role initialization"() {

    when: "roles are initialized"
    bootstrapService.initializeRoles()

    then: "we have 3 roles..."
    bootstrapService.studentRole != null
    bootstrapService.studentRole == RoleEnum.STUDENT_ROLE.role
    bootstrapService.teacherRole != null
    bootstrapService.teacherRole == RoleEnum.TEACHER_ROLE.role
    bootstrapService.adminRole != null
    bootstrapService.adminRole == RoleEnum.ADMIN_ROLE.role
    bootstrapService.adminRole.id == 1

  }

  def "user initialization"() {

    when: "users are initilized"
    bootstrapService.inializeDevUsers()

    then: "we have 3 users"
    bootstrapService.fsil != null
    bootstrapService.fsil.username == "fsil"
    bootstrapService.fsil.language == 'fr'
    bootstrapService.mary != null
    bootstrapService.mary.username == "mary"
    bootstrapService.mary.language == 'fr'
    bootstrapService.thom != null
    bootstrapService.thom.username == "thom"
    bootstrapService.thom.language == 'fr'
  }


}
