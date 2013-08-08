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

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/
@TestFor(User)
class UserSpec extends Specification {

  @Unroll
  def "'#username' is valid username is #usernameIsOK"() {
    when:
      User user = new User(firstName: "franck", lastName: "s", username: username, email: "mail@mail.com", password: "password")
    then:
      println "-${user.username}-"
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

}