/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        bootstrapService.fsil.settings.language == 'fr'
        bootstrapService.mary != null
        bootstrapService.mary.username == "mary"
        bootstrapService.mary.settings.language == 'fr'
        bootstrapService.thom != null
        bootstrapService.thom.username == "thom"
        bootstrapService.thom.settings.language == 'fr'
    }

    def "context initialization"() {

        given: "dev users initialized"
        bootstrapService.inializeDevUsers()

        when: "context are initialized"
        bootstrapService.initializeDevContext()
        bootstrapService.initializeDevContextWithFragment()

        then: "we have two context"
        bootstrapService.football != null
        bootstrapService.football.contextName == "football"
        bootstrapService.science != null
        bootstrapService.science.contextName == "science"
    }

}
