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

package org.tsaap.lti.tp

import spock.lang.Specification


class UserSpec extends Specification {

    def "test user set names"() {

        given: "a user"
        User user1 = new User("user1")

        and: "a name, a first name and a fullname that are consistent"
        def lastname = "Pacino"
        def firstname = "Al"
        def fullname = "Al Pacino"

        when: "the names are set"
        user1.setNames(firstname, lastname, fullname)

        then: "the names are set as expected"
        user1.firstname == firstname
        user1.lastname == lastname
        user1.fullname == fullname

        when: "only fullname is set"
        user1.setNames(null, null, fullname)

        then: "the names are set with default values expected for fullname"
        user1.firstname == User.USER_DEFAULT_FIRSTNAME
        user1.lastname == user1.id
        user1.fullname == fullname

        when: "only lastname and fullname are given"
        user1.setNames(null, lastname, fullname)

        then: "the firstname is set with default value"
        user1.firstname == User.USER_DEFAULT_FIRSTNAME
        user1.lastname == lastname
        user1.fullname == fullname

        when: "only firstname and fullname are given"
        user1.setNames(firstname, null, fullname)

        then: "the lastname is based on the last part of the fullname"
        user1.firstname == firstname
        user1.lastname == user1.id
        user1.fullname == fullname

        when: "no fullname is provided"
        user1.setNames(firstname, lastname, null)

        then: "names are set as expected"
        user1.firstname == firstname
        user1.lastname == lastname
        user1.fullname == "$firstname $lastname"

    }

}