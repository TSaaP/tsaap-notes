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

class LmsAssignmentService {

    LmsAssignmentHelper lmsAssignmentHelper
    LmsUserHelper lmsUserHelper

    /**
     * Find or create a context for a given lti_instance and a given lti_course from this instance
     * @param sql
     * @param lmsAssignment the lms context to find or create
     * @return the lms context
     */
    LmsAssignment findOrCreateAssignment(Sql sql, LmsAssignment lmsAssignment) {
        lmsAssignment.owner.userId = lmsUserHelper.findUserIdForUsername(sql, lmsAssignment.owner.username)
        Long assignmentId = findAssignmentIdForLmsAssignment(sql,lmsAssignment)
        if (!assignmentId) { // has to create the assignment
            if (lmsAssignment.owner.isLearner) {
                return updateLmsAssignmentWithError("Owner is not a teacher", lmsAssignment)
            }
            lmsAssignmentHelper.insertAssignment(sql, lmsAssignment)
        }
        if (!lmsAssignmentHelper.lmsAssignmentExists(sql, lmsAssignment)) { // has to create the lms assignment
            lmsAssignmentHelper.insertLmsAssignment(sql, lmsAssignment)
        }
        lmsAssignment
    }

    /**
     * Find assignment id for an lms assignment
     * @param lmsAssignment the lms assignment
     * @return the assignment id if any, null either
     */
    Long findAssignmentIdForLmsAssignment(Sql sql, LmsAssignment lmsAssignment) {
        if (lmsAssignment.assignmentId) {
            return lmsAssignment.assignmentId
        }
        lmsAssignment.assignmentId = lmsAssignmentHelper.selectLmsAssignmentId(sql, lmsAssignment.ltiConsumerKey,
                lmsAssignment.ltiCourseId, lmsAssignment.ltiActivityId)
    }

/**
 * Get lms assignment
 * @param message
 * @return
 */
    LmsAssignment updateLmsAssignmentWithError(String message, LmsAssignment lmsAssignment = null) {
        if (!lmsAssignment) {
            lmsAssignment = new LmsAssignment()
        }
        lmsAssignment.hasError = true
        lmsAssignment.errorCause = message
        lmsAssignment
    }


}
