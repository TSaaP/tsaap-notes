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

/**
 * Class helper for lms assignment management
 */
class LmsAssignmentHelper {

    /**
     * find if an lsm_assignment exist for given lti instance id and lti context id
     * @param ltiCourseId lti course id
     * @param ltiActivityId lti activity id
     * @return an assignment id if the lms assignment exist else null
     */
    Long selectLmsAssignmentId(Sql sql, String consumerKey, String ltiCourseId, String ltiActivityId) {
        def req = sql.firstRow("SELECT assignment_id FROM lms_assignment WHERE lti_consumer_key = $consumerKey and lti_activity_id = $ltiActivityId and lti_course_id = $ltiCourseId")
        def res = null
        if (req != null) {
            res = req.assignment_id
        }
        res
    }


    /**
     * Select an assignment Id for a given globalId
     * @param sql
     * @param globalId the global id
     * @return  the assignment id
     */
    Long selectAssignmentId(Sql sql, String globalId) {
        def req = sql.firstRow("SELECT id FROM assignment WHERE global_id = $globalId")
        Long res = req.id
        res
    }

    /**
     * Insert a new Lms assignment in database
     * @param sql
     * @param lmsAssignment the lms context to insert
     */
    def insertLmsAssignment(Sql sql, LmsAssignment lmsAssignment) {
        sql.executeInsert("INSERT INTO lms_assignment VALUES ($lmsAssignment.assignmentId,$lmsAssignment.ltiCourseId,$lmsAssignment.ltiActivityId,$lmsAssignment.ltiConsumerKey,$lmsAssignment.ltiConsumerName)")
    }


    /**
     * Delete a lms context for a given tsaap context id
     * @param sql
     * @param contextId tsaap context id
     */
    def deleteLmsAssignment(Sql sql, long assignmentId) {
        sql.execute("DELETE FROM lms_assignment WHERE assignment_id = $assignmentId")
    }



    /**
     * Check existence of a local lms assignment
     * @param sql
     * @param lmsAssignment
     * @return true if the local lms context exists
     */
    boolean lmsAssignmentExists(Sql sql, LmsAssignment lmsAssignment) {
        def req = sql.firstRow("select count(*) as count_assignment FROM lms_assignment WHERE assignment_id = $lmsAssignment.assignmentId and lti_course_id = $lmsAssignment.ltiCourseId and lti_activity_id = $lmsAssignment.ltiActivityId and lti_consumer_key = $lmsAssignment.ltiConsumerKey")
        def res = req.count_assignment == 1
        res
    }

    /**
     * Insert a new assignment in the database
     * @param sql the sql connection
     * @param lmsAssignment the lms assignment containing all required data
     * @return the id of the assignment created
     */
    Long insertAssignment(Sql sql, LmsAssignment lmsAssignment) {
        def keys = sql.executeInsert("INSERT INTO assignment (version,date_created,last_updated,global_id,owner_id,title) VALUES (1,now(),now(),uuid(),${lmsAssignment.owner.userId}, ${lmsAssignment.title})")
        def assignmentId = keys[0][0]
        lmsAssignment.assignmentId = assignmentId
        assignmentId
    }
}
