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

package org.tsaap.lti

import grails.test.mixin.TestFor
import org.tsaap.lti.tp.ResourceLink
import org.tsaap.lti.tp.User
import spock.lang.Specification

@TestFor(LmsGradeService)
class LmsGradeServiceSpec extends Specification {

    LmsGradeService lmsGradeService
    ResourceLink resourceLink
    User user

    def setup() {
        lmsGradeService = new LmsGradeService()
    }

    void "test send users grades for context"() {

        given: "the collaborators"
        resourceLink = Mock(ResourceLink)
        user = Mock(User)

        when: "I want to send users grade for a given context"
        lmsGradeService.sendUserGradeToLms(resourceLink, user, 66)

        then: "the grades are correctly sent"
        1 * resourceLink.doOutcomesService(resourceLink.EXT_WRITE, _, user) >> true

    }
}
