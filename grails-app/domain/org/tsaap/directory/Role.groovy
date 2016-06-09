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

class Role {

    String roleName

    static mapping = {
        cache true
        version(false)
        roleName column: 'authority'
    }

    String getAuthority() {
        "ROLE_${this.roleName}"
    }

    static constraints = {
        roleName blank: false, unique: true, inList: RoleEnum.values()*.name()
    }


}

enum RoleEnum {
    ADMIN_ROLE(1),
    STUDENT_ROLE(2),
    TEACHER_ROLE(3)

    Long id

    RoleEnum(Long id) {
        this.id = id
    }

    Role getRole() {
        Role.get(id)
    }

}