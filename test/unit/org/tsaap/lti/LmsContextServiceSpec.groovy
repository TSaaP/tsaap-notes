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
import groovy.sql.Sql
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LmsContextService)
class LmsContextServiceSpec extends Specification {

    LmsContextService lmsContextService
    LmsContextHelper lmsContextHelper
    LmsUserHelper lmsUserHelper
    Sql sql
    LmsContext lmsContext

    def setup() {
        lmsContextService = new LmsContextService()
        lmsContext = new LmsContext(ltiConsumerKey:'key', ltiCourseId:'3', ltiActivityId:'4',
                ltiConsumerName: 'Moodle Tsaap', contextTitle:'Tsaap Teach: Tsaap',
                owner: new LmsUser(username: 'jdoe',isLearner: false))
    }

    void "test to create a tsaap note context for given lti context and consumer "() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsContextHelper = Mock(LmsContextHelper)
        lmsUserHelper = Mock(LmsUserHelper)
        lmsContextService.lmsContextHelper = lmsContextHelper
        lmsContextService.lmsUserHelper = lmsUserHelper

        when: "I try to create a tsaap note context and user is a teacher"
        def res = lmsContextService.findOrCreateContext(sql, lmsContext)

        then: "a context is created and I get his id and his name"
        1 * lmsContextHelper.selectLmsContextId(sql, 'key', '3', '4') >> null
        1 * lmsUserHelper.findUserIdForUsername(sql, "jdoe") >> 88
        1 * lmsContextHelper.insertContext(sql, lmsContext)
        1 * lmsContextHelper.insertLmsContext(sql, lmsContext)
        res.contextTitle == 'Tsaap Teach: Tsaap'


        when: "I try to create a tsaap note context and user is a learner"
        lmsContext.owner.isLearner = true
        lmsContextService.findOrCreateContext(sql, lmsContext)

        then: "I get an exception"
        1 * lmsContextHelper.selectLmsContextId(sql, 'key', '3', '4') >> null
        1 * lmsUserHelper.findUserIdForUsername(sql, "jdoe") >> 88
        thrown(LtiContextInitialisationException)
    }

    void "test to find a tsaap note context for given lti context and consumer"() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsContextHelper = Mock(LmsContextHelper)
        lmsUserHelper = Mock(LmsUserHelper)
        lmsContextService.lmsContextHelper = lmsContextHelper
        lmsContextService.lmsUserHelper = lmsUserHelper

        when: "I try to find a tsaap note context and user is a teacher"
        def res = lmsContextService.findOrCreateContext(sql, lmsContext)

        then: "a context is found and I get his id and his name"
        1 * lmsContextHelper.selectLmsContextId(sql, 'key', '3', '4') >> 55
        1 * lmsUserHelper.findUserIdForUsername(sql, "jdoe") >> 88
        1 * lmsContextHelper.selectContextName(sql, 55) >> 'Tsaap Teach: Tsaap'
        res.contextTitle == 'Tsaap Teach: Tsaap'
        res.contextId == 55

        when: "I try to find a tsaap note context and user is a learner"
        lmsContext.owner.isLearner = true
        def res2 = lmsContextService.findOrCreateContext(sql, lmsContext)

        then: "a context is found and I get his id and his name"
        1 * lmsContextHelper.selectLmsContextId(sql, 'key', '3', '4') >> 55
        1 * lmsUserHelper.findUserIdForUsername(sql, "jdoe") >> 88
        1 * lmsContextHelper.selectContextName(sql, 55) >> 'Tsaap Teach: Tsaap'
        1 * lmsContextHelper.checkIfUserIsAContextFollower(sql, 88, 55) >> false
        1 * lmsContextHelper.addUserToContextFollower(sql, 88, 55)
        res2.contextTitle == 'Tsaap Teach: Tsaap'
        res2.contextId == 55
    }

    void "test to delete a lms context for a tsaap context"() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsContextHelper = Mock(LmsContextHelper)
        lmsContextService.lmsContextHelper = lmsContextHelper

        when: "I try to delete an existing lms context for a tsaap context"
        lmsContextService.deleteLmsContextForContext(sql, 55)

        then: "the lms context is delete"
        1 * lmsContextHelper.selectLmsContextForContextId(sql, 55) >> 55
        1 * lmsContextHelper.deleteLmsContext(sql, 55)

        when: "I try to delete an unexisting lms context for a tsaap context"
        lmsContextService.deleteLmsContextForContext(sql, 99)

        then: "Nothing is delete"
        1 * lmsContextHelper.selectLmsContextForContextId(sql, 99) >> null
    }
}
