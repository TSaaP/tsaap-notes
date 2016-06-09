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

    def setup() {
        lmsContextHelper = new LmsContextHelper()
        lmsUserHelper = new LmsUserHelper()
        sql = new Sql(dataSource)
    }


    def "test select context id in database"() {

        when: "I want to select a context id from a context name and source"
        def userId = null
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Context", "", userId, true, "", "Source")
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Context", "", userId, true, "", "Source")
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "test 2 with ' special caractère", "", userId, true, "", 'Source')
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql, contextId, "3", "4", 'key', 'Moodle-Tsaap')
                res = lmsContextHelper.selectLmsContext(sql, 'key', "3", "4")
                res2 = lmsContextHelper.selectLmsContext(sql, 'key', '8', '12')
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get an id for an existing lms context and null for the other"
        res != null
        res == contextId
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql, contextId, "3", "4", 'key', 'Moodle-Tsaap')
                res = lmsContextHelper.selectLmsContext(sql, 'key', "3", "4")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "The lms context is correctly insert"
        res != null
        res == contextId

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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql, contextId, "3", "4", 'key', 'Moodle-Tsaap')
                res = lmsContextHelper.selectLmsContextForContextId(sql, contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get the lms context"
        res != null
        res.tsaap_context_id == contextId
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql, contextId, "3", "4", 'key', 'Moodle-Tsaap')
                lmsContextHelper.deleteLmsContext(sql, contextId)
                res = lmsContextHelper.selectLmsContextForContextId(sql, contextId)
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                contextId = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                lmsContextHelper.insertLmsContext(sql, contextId, "3", "4", 'key', 'Moodle-Tsaap')
                res = lmsContextHelper.selectConsumerKeyAndCourseId(sql, contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get consumer key and lti course id"
        res == ["key", "3"]
    }

    def "test check if an user is a context follower"() {

        when: "I want to know if users are context follower or not"
        def userId
        def userId2
        def contextId
        def res = null
        def res2 = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                contextId = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                lmsUserHelper.insertUserInDatabase(sql, "jtes@nomail.com", "jess", "test", "jtes", "pass", true)
                userId2 = lmsUserHelper.selectUserId(sql, "jtes")
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
                lmsUserHelper.insertUserInDatabase(sql, "jdoe@nomail.com", "john", "doe", "jdoe", "pass", true)
                userId = lmsUserHelper.selectUserId(sql, "jdoe")
                lmsContextHelper.insertContext(sql, "Tsaap teach: Tsaap", "", userId, true, "", 'Moodle-Tsaap')
                contextId = lmsContextHelper.selectContextId(sql, "Tsaap teach: Tsaap", 'Moodle-Tsaap')
                lmsUserHelper.insertUserInDatabase(sql, "jtes@nomail.com", "jess", "test", "jtes", "pass", true)
                userId2 = lmsUserHelper.selectUserId(sql, "jtes")
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
