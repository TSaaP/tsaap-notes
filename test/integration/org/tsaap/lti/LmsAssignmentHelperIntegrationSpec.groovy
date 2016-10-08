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
import org.tsaap.BootstrapTestService
import org.tsaap.assignments.Assignment
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.SQLException

class LmsAssignmentHelperIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    DataSource dataSource
    Sql sql
    LmsAssignmentHelper lmsAssignmentHelper
    LmsUserHelper lmsUserHelper
    LmsUser lmsUser
    LmsUser lmsUser2
    Assignment assignment
    LmsAssignment lmsAssignment3

    def setup() {
        bootstrapTestService.initializeTests()
        lmsAssignmentHelper = new LmsAssignmentHelper()
        lmsUserHelper = new LmsUserHelper()
        sql = new Sql(dataSource)
        lmsUser = new LmsUser(email: "jdoe@nomail.com", firstname: "john", lastname: 'doe', username: 'jdoe', password: 'pass', isEnabled: true)
        lmsUser2 = new LmsUser(email: "jtes@nomail.com", firstname: "jess", lastname: "test", username: "jtes", password: "pass", isEnabled: true)
        assignment = bootstrapTestService.assignment1
        lmsAssignment3 = new LmsAssignment(ltiConsumerName: "Moodle-Tsaap",
                owner: lmsUser, ltiCourseId: "3", ltiActivityId: "4", ltiConsumerKey: 'key', assignmentId: assignment.id, title:"lti assignment")
    }


    def "test select assignment id in database"() {

        given: "an assignment and its global id"
        assignment
        String globalId = assignment.globalId

        when: "I  select an assignment id from a global id"
        def res = lmsAssignmentHelper.selectAssignmentId(sql, globalId)

        then: "I get the context id"
        res == assignment.id
    }


    def "test insert lms assignment in database"() {

        given: "an assignment"
        assignment

        when: "I want to insert a lms assignment for a given lti_context and assignment"
        def res = null
        def userId = null
        def lmsAssignmentExist = false
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsAssignmentHelper.insertLmsAssignment(sql, lmsAssignment3)
                res = lmsAssignmentHelper.selectLmsAssignmentId(sql, 'key', "3", "4")
                lmsAssignmentExist = lmsAssignmentHelper.lmsAssignmentExists(sql, lmsAssignment3)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "The lms context is correctly insert"
        res == lmsAssignment3.assignmentId
        lmsAssignmentExist

    }


    def "test delete lms assignment for an assignment"() {

        when: "I want to delete a lms context for a given tsaap context id"
        def res = null
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                userId = lmsUserHelper.findUserIdForUsername(sql, "jdoe")
                lmsAssignmentHelper.insertLmsAssignment(sql, lmsAssignment3)
                lmsAssignmentHelper.deleteLmsAssignment(sql, lmsAssignment3.assignmentId)
                res = lmsAssignmentHelper.lmsAssignmentExists(sql, lmsAssignment3)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "the lms context is delele"
        !res
    }

    def "test insert assignment in database"() {

        when: "I want to insert an assignment in database"
        def res = null
        Assignment assignment = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql, lmsUser)
                res = lmsAssignmentHelper.insertAssignment(sql, lmsAssignment3)
                assignment = Assignment.findById(res)
                throw new SQLException()
            }
        }
        catch (SQLException e) {
            log.error e.message
        }

        then: "I get the context id"
        res != null

        and: "the assignment is consistent with the previous insert"
        assignment
        assignment.globalId
        assignment.version == 1
        assignment.title == lmsAssignment3.title
        lmsAssignment3.assignmentId == assignment.id

    }
}
