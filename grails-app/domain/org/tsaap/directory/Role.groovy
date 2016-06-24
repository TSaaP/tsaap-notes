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

package org.tsaap.directory

class Role {

    String roleName

    static mapping = {
        cache true
        version(false)
        roleName column: 'authority'
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