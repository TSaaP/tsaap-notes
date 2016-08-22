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

class LmsContextService {

    LmsContextHelper lmsContextHelper
    LmsUserHelper lmsUserHelper

    /**
     * Find or create a context for a given lti_instance and a given lti_course from this instance
     * @param sql
     * @param lmsContext the lms context to find or create
     * @return the lms context
     */
    LmsContext findOrCreateContext(Sql sql, LmsContext lmsContext) {
        lmsContext.contextId = lmsContextHelper.selectLmsContextId(sql, lmsContext.ltiConsumerKey, lmsContext.ltiCourseId, lmsContext.ltiActivityId)
        lmsContext.owner.userId = lmsUserHelper.findUserIdForUsername(sql, lmsContext.owner.username)
        if (lmsContext.contextId == null) {
            createLmsContext(sql, lmsContext)
        } else {
            updateContextTitleAndFollower(sql, lmsContext)
        }
        lmsContext
    }

    /**
     * Delete an Lms context for a given tsaap note context
     * @param sql
     * @param contextId tsaap note context id
     */
    def deleteLmsContextForContext(Sql sql, long contextId) {
        def fetchedContext = lmsContextHelper.selectLmsContextForContextId(sql, contextId)
        if (fetchedContext != null) {
            lmsContextHelper.deleteLmsContext(sql, contextId)
        }
    }

    private void updateContextTitleAndFollower(Sql sql, LmsContext lmsContext) {
        lmsContext.contextTitle = lmsContextHelper.selectContextName(sql, lmsContext.contextId)
        def owner = lmsContext.owner
        if (owner.isLearner) {
            def isFollower = lmsContextHelper.checkIfUserIsAContextFollower(sql, owner.userId, lmsContext.contextId)
            if (!isFollower) {
                lmsContextHelper.addUserToContextFollower(sql, owner.userId, lmsContext.contextId)
            }
        }
    }

    private void createLmsContext(Sql sql, LmsContext lmsContext) {
        def owner = lmsContext.owner
        if (!owner.isLearner) {
            lmsContextHelper.insertContext(sql, lmsContext)
            lmsContextHelper.insertLmsContext(sql, lmsContext)
        } else {
            throw new LtiContextInitialisationException("error.lti.context.initialisation")
        }
    }
}
