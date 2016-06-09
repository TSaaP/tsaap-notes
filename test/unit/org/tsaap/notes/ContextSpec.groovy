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

package org.tsaap.notes

import grails.test.mixin.TestFor
import org.tsaap.directory.User
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions*/
@TestFor(Context)
class ContextSpec extends Specification {

    @Unroll
    def "'#name' is valid context name is #nameIsOK"() {

        when: "a new context is created"
        def user = Mock(User)
        Context context = new Context(contextName: name, url: 'http://www.w3.org', descriptionAsNote: 'a description', owner: user)

        then: "the validation gives..."
        println "-${context.contextName}-"
        context.validate() == nameIsOK

        where: "name and nameIsOK"
        name            | nameIsOK
        "is not a word" | true
        "franck"        | true
        "Mary"          | true
        "franck-s"      | true
        "Mary_s"        | true
        "fr@nck"        | true
        "Mar%"          | true
        "éric"          | true
        "69"            | true
        "thi_s/is/OK"   | true
    }

}