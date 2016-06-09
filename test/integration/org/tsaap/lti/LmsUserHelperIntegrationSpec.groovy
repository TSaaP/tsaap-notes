package org.tsaap.lti

import groovy.sql.Sql
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.SQLException

/**
 * Created by dorian on 22/06/15.
 */
class LmsUserHelperIntegrationSpec extends Specification {

    DataSource dataSource
    LmsUserHelper lmsUserHelper
    Sql sql

    def setup() {
        lmsUserHelper = new LmsUserHelper()
        sql = new Sql(dataSource)
    }

    def "test select an username and password in database"() {

        when: "I want to know tsaap username and password for a given lti user"

        def req = null
        def res = null
        def userId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                req = sql.firstRow("SELECT id FROM user WHERE username = 'jdoe'")
                userId = req.id
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "10", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"10\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                lmsUserHelper.insertLmsUserInDatabase(sql, userId, 'key', '10')
                res = lmsUserHelper.selectUsernameAndPassword(sql, '10')
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get tsaap username and password for this lti user"
        res.username == 'jdoe'
        res.password == 'pass'
    }

    def "test select an user id in database"() {

        when: "I want to know the user id for a given username"
        def userId
        def req = null
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                req = sql.firstRow("SELECT * FROM user WHERE username = 'jdoe'")
                userId = req.id
                res = lmsUserHelper.selectUserId(sql, "jdoe")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get jdoe user id"
        res == userId

    }

    def "test select an username in database"() {

        when: "I want to know if an username in database begin with the username passed in parameter"
        def res = null
        def res2 = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                res = lmsUserHelper.selectUsernameIfExist(sql, "jdoe")
                res2 = lmsUserHelper.selectUsernameIfExist(sql, "drol")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "an username is found for jdoe but not for drol"
        res == "jdoe"
        res2 == null
    }

    def "test insert lms user in database"() {
        when: "I want to insert a lms user"
        def res = null
        def req = null
        def userId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                req = sql.firstRow("SELECT id FROM user WHERE username = 'jdoe'")
                userId = req.id
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "10", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"10\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                lmsUserHelper.insertLmsUserInDatabase(sql, userId, 'key', '10')
                res = sql.firstRow("SELECT * FROM lms_user WHERE tsaap_user_id = $userId")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the lms user is correctly add in database"
        res.tsaap_user_id == userId
        res.lti_consumer_key == 'key'
        res.lti_user_id == '10'
    }

    def "test select lms user in database"() {

        when: "I want to find if a lti user is attach to a lms user"
        def req = null
        def res = null
        def res2 = null
        def userId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                req = sql.firstRow("SELECT id FROM user WHERE username = 'jdoe'")
                userId = req.id
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "10", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"10\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                lmsUserHelper.insertLmsUserInDatabase(sql, userId, 'key', '10')
                res = lmsUserHelper.selectLmsUser(sql, '10')
                res2 = lmsUserHelper.selectLmsUser(sql, '12')
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "we got a tsaap user id for lti_user_id 10 and null for 12"
        res == userId
        res2 == null

    }

    def "test insert user role in database"() {

        when: "I want to insert a user role in database"
        def req = null
        def res = null
        def userId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                req = sql.firstRow("SELECT id FROM user WHERE username = 'jdoe'")
                userId = req.id
                lmsUserHelper.insertUserRoleInDatabase(sql, 2, userId)
                res = sql.firstRow("SELECT * FROM user_role WHERE user_id = $userId")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the user role is correctly add in database"
        res.role_id == 2
        res.user_id == userId
    }

    def "test insert user in database"() {

        when: "I want to insert a user in database"
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                res = sql.firstRow("SELECT * FROM user WHERE username = 'jdoe'")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the user is correctly add in database"
        res.username == 'jdoe'
        res.first_name == 'john'
    }

    def "test lti_consumer insertion"() {

        when: "we create a lti_consumer"
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                res = sql.firstRow("SELECT * FROM lti_consumer WHERE consumer_key='key' AND secret = 'azer'")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the lti_consumer is correctly add in database"
        res.consumer_key == 'key'
        res.name == 'Moodle'
    }

    def "test lti_context insertion"() {

        when: "we create a lti_context"
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                res = sql.firstRow("SELECT * FROM lti_context WHERE consumer_key='key' AND context_id='3'")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the lti_context is correctly add in database"
        res.consumer_key == 'key'
        res.title == 'Tsaap teach: Tsaap'
    }

    def "test lti_user insertion"() {

        when: "we create a lti_user"
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "10", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"10\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                res = sql.firstRow("SELECT * FROM lti_user WHERE consumer_key='key' AND user_id='10'")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the lti_user is correctly add in database"
        res.consumer_key == 'key'
        res.user_id == '10'

    }

    def "test select if account is enable"() {

        when: "I want to know if an account is enable for a given username"
        def res = null
        def res2 = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "jane", "doe", "jdoe2", "pass", false)
                res = lmsUserHelper.selectUserIsEnable(sql, "jdoe")
                res2 = lmsUserHelper.selectUserIsEnable(sql, "jdoe2")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get if user is enable"
        res
        !res2
    }

    def "test enable user account"() {

        when: "I want to enable an user account"
        def res = null
        def res2 = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "jane", "doe", "jdoe", "pass", false)
                res = lmsUserHelper.selectUserIsEnable(sql, "jdoe")
                lmsUserHelper.enableUser(sql, "jdoe")
                res2 = lmsUserHelper.selectUserIsEnable(sql, "jdoe")
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the account is enable"
        res == false
        res2 == true
    }

    def "test select lti user id"() {

        when: "I want to get a lti user id for a given user id"
        def req = null
        def res = null
        def userId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, "user@mail.com", "john", "doe", "jdoe", "pass", true)
                req = sql.firstRow("SELECT id FROM user WHERE username = 'jdoe'")
                userId = req.id
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "10", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"10\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                lmsUserHelper.insertLmsUserInDatabase(sql, userId, 'key', '10')
                res = lmsUserHelper.selectLtiUserId(sql, userId)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get lti user id"
        res == "10"
    }
}
