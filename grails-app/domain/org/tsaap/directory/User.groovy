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

import org.tsaap.assignments.Assignment
import org.tsaap.assignments.LearnerAssignment

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

    boolean canBeUserOwner = false
    User owner
    String clearPassword

    static hasOne = [settings: Settings]

    void setUsername(String val) {
        this.username = val
        normalizedUsername = val?.toLowerCase()
    }

    static constraints = {
        firstName blank: false
        lastName blank: false
        normalizedUsername blank: false, unique: true, validator: { val ->
            (val ==~ /^[a-zA-Z0-9_\-]{1,15}$/) ?: 'user.normalizedUsername.invalid'
        }
        password blank: false, minSize: 4
        owner nullable: true
        email email: true, unique: true, nullable: true, validator: { targetEmail, obj ->
            targetEmail || obj.owner
        }
        settings nullable: true
    }

    static mapping = {
        password column: '`password`'
        version(true)
    }

    static transients = ['fullname', 'isTeacher', 'isLearner', "clearPassword"]

    String getFullname() {
        "$firstName $lastName"
    }

    String toString() {
        username
    }

    /**
     *
     * @return true if the user is a learner
     */
    boolean isLearner() {
        UserRole.get(this.id, RoleEnum.STUDENT_ROLE.id)
    }

    /**
     *
     * @return true if the user is teacher
     */
    boolean isTeacher() {
        UserRole.get(this.id, RoleEnum.TEACHER_ROLE.id)
    }

    /**
     *
     * @return true if the user is admin
     */
    boolean isAdmin() {
        UserRole.get(this.id, RoleEnum.ADMIN_ROLE.id)
    }

    /**
     * Check if a user is registered in an assignment
     * @param assignment the assignment
     * @return true if the user is registered in the given assignment
     */
    boolean isRegisteredInAssignment(Assignment assignment) {
        LearnerAssignment.findByLearnerAndAssignment(this,assignment)
    }

    Set<Role> getAuthorities() {
        def res = UserRole.findAllByUser(this).collect { it.role } as Set
        res*.roleName
        res
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
