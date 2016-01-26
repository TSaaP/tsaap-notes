/*
 * Copyright 2016 Tsaap Development Group
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

package org.tsaap.lti.tp

import spock.lang.Specification


class UserSpec extends Specification {

def "test user set names"() {

    given: "a user"
    User user1 = new User("user1")

    and:"a name, a first name and a fullname that are consistent"
    def lastname = "Pacino"
    def firstname = "Al"
    def fullname = "Al Pacino"

    when: "the names are set"
    user1.setNames(firstname,lastname, fullname)

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