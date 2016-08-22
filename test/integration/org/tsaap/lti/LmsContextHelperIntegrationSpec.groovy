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

import groovy.sql.Sql
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.SQLException

/**
 * Created by dorian on 30/06/15.
 */
class LmsContextHelperIntegrationSpec extends Specification {

    DataSource dataSource
    Sql sql
    LmsContextHelper lmsContextHelper
    LmsUserHelper lmsUserHelper
    LmsUser lmsUser
    LmsUser lmsUser2
    LmsContext lmsContext
    LmsContext lmsContext2
    LmsContext lmsContext3

    def setup() {
        lmsContextHelper = new LmsContextHelper()
        lmsUserHelper = new LmsUserHelper()
        sql = new Sql(dataSource)
        lmsUser = new LmsUser(email: "jdoe@nomail.com", firstname: "john", lastname: 'doe',username: 'jdoe', password: 'pass', isEnabled: true)
        lmsUser2 = new LmsUser(email:  "jtes@nomail.com", firstname:  "jess", lastname:  "test", username:  "jtes", password:  "pass", isEnabled: true)
        lmsContext = new LmsContext(contextTitle: "Context", ltiConsumerName: "Source", owner: lmsUser)
        lmsContext2 = new LmsContext(contextTitle: "test 2 with ' special caractère", ltiConsumerName: "Source", owner: lmsUser)
        lmsContext3 = new LmsContext(contextTitle: "Tsaap teach: Tsaap", ltiConsumerName: "Moodle-Tsaap",
                owner: lmsUser, ltiCourseId: "3", ltiActivityId: "4", ltiConsumerKey: 'key')
    }


    def "test select context id in database"() {

        when: "I want to select a context id from a context name and source"
        def userId = null
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext)
                res = lmsContextHelper.selectContextId(sql, "Context", "Source")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get the context id"
        res != null
    }

    def "test select context name in database"() {

        when: "I want to select a context name from a context id"
        def contextId = null
        def res = null
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext)
                contextId = lmsContextHelper.selectContextId(sql, "Context", "Source")
                res = lmsContextHelper.selectContextName(sql, contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get the context name"
        res == "Context"
    }

    def "test insert context in database"() {

        when: "I want to insert a context in database"
        def res = null
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext2)
                res = lmsContextHelper.selectContextId(sql, "test 2 with ' special caractère", 'Source')
                throw new SQLException()
            }
        }
        catch (SQLException e) {
            log.error e.message
        }

        then: "I get the context id"
        res != null
    }


    def "test select lms context in database"() {

        when: "I want to select an lms context id if the lms context is attach to the given lti context"
        def req = null
        def res = null
        def res2 = null
        def contextId
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql,lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                lmsContextHelper.insertLmsContext(sql, lmsContext3)
                res = lmsContextHelper.selectLmsContextId(sql, 'key', "3", "4")
                res2 = lmsContextHelper.selectLmsContextId(sql, 'key', '8', '12')
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get an id for an existing lms context and null for the other"
        res != null
        res == lmsContext3.contextId
        res2 == null

    }

    def "test insert lms context in database"() {

        when: "I want to insert a lms context for a given lti_context and context"
        def req = null
        def res = null
        def userId = null
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                lmsContextHelper.insertLmsContext(sql, lmsContext3)
                res = lmsContextHelper.selectLmsContextId(sql, 'key', "3", "4")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "The lms context is correctly insert"
        res != null
        res == lmsContext3.contextId

    }

    def "test select lms context for a context"() {

        when: "I want to select a lms context for a given tsaap context id"
        def req = null
        def res = null
        def userId = null
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                lmsContextHelper.insertLmsContext(sql, lmsContext3)
                res = lmsContextHelper.selectLmsContextForContextId(sql, lmsContext3.contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get the lms context"
        res != null
        res.tsaap_context_id == lmsContext3.contextId
    }

    def "test delete lms context for a context"() {

        when: "I want to delete a lms context for a given tsaap context id"
        def req = null
        def res = null
        def userId = null
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                lmsContextHelper.insertLmsContext(sql, lmsContext3)
                lmsContextHelper.deleteLmsContext(sql, lmsContext3.contextId)
                res = lmsContextHelper.selectLmsContextForContextId(sql, lmsContext3.contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the lms context is delele"
        res == null
    }

    def "test select consumer key and lti course id"() {

        when: "I want to get consumer key and lti course id for a given context id"
        def res = null
        def userId
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                lmsContextHelper.insertLmsContext(sql, lmsContext3)
                res = lmsContextHelper.selectConsumerKeyAndCourseId(sql, lmsContext3.contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get consumer key and lti course id"
        res == ["key", "3"]
    }

    def "test check if a user is a context follower"() {

        when: "I want to know if users are context follower or not"
        def userId
        def userId2
        def contextId
        def res = null
        def res2 = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                contextId = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                lmsUserHelper.insertUserInDatabase(sql, lmsUser2)
                userId2 = lmsUserHelper.findUserIdForUsername(sql, "jtes")
                res = lmsContextHelper.checkIfUserIsAContextFollower(sql, userId2, contextId)
                lmsContextHelper.addUserToContextFollower(sql, userId2, contextId)
                res2 = lmsContextHelper.checkIfUserIsAContextFollower(sql, userId2, contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "We got correct answer"
        !res
        res2
    }

    def "test add an user as a context followers"() {

        when: "I want to add an user to followers of a context"
        def userId
        def userId2
        def contextId
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsContextHelper.insertContext(sql, lmsContext3)
                contextId = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                lmsUserHelper.insertUserInDatabase(sql, lmsUser2)
                userId2 = lmsUserHelper.findUserIdForUsername(sql, "jtes")
                lmsContextHelper.addUserToContextFollower(sql, userId2, contextId)
                res = lmsContextHelper.checkIfUserIsAContextFollower(sql, userId2, contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "The user is correctly add to followers for this context"
        res
    }
}
