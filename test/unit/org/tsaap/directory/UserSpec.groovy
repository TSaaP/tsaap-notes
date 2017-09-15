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

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/
@TestFor(User)
class UserSpec extends Specification {

    @Unroll
    def "'#username' is valid username is #usernameIsOK"() {

        given: "a user"
        User user = new User(firstName: "franck", lastName: "s", username: username, email: "mail@mail.com", password: "password")
        println "-${user.username}-"

        expect: "validation rules on username are coorectly applyed"
        user.validate() == usernameIsOK
        user.normalizedUsername == username.toLowerCase()

        where:
        username                | usernameIsOK
        "is not a word"         | false
        "franck"                | true
        "Mary"                  | true
        "franck-s"              | false
        "Mary_s"                | true
        "fr@nck"                | false
        "Mar%"                  | false
        "éric"                  | false
        "69"                    | true
        "thisusernameistoolong" | false
    }

    def "user get full name string"() {

        given: "a user"
        User user = new User(firstName: "franck", lastName: "s", username: "fs", email: "mail@mail.com", password: "password")

        when: "I want a string with this user fullname"
        def res = user.getFullname()

        then: "I get a string with this user fullname"
        res == "franck s"
    }

    def "user must have email or owner"() {
        given:"a user with an email"
        User user = new User(firstName: "franck", lastName: "s", username: "francks", email: "mail@mail.com", password: "password")

        expect: "the user is valid"
        user.errors.each { println it }
        user.validate()
        !user.hasErrors()

        when:"the user has no email"
        user.email = null

        then: "the user is no more valid"
        !user.validate()
        user.errors.each { println it }

        when:"the user has no email but references an owner"
        user.owner = Mock(User)

        then: "the user is valid"
        user.errors.each { println it }
        user.validate()

    }
}