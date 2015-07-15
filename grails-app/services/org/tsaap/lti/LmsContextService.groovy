package org.tsaap.lti

import groovy.sql.Sql


class LmsContextService {

    LmsContextHelper lmsContextHelper
    LmsUserHelper lmsUserHelper

    /**
     * Find or create a context for a given lti_instance and a given lti_course from this instance
     * @param sql
     * @param consumerKey consumer key
     * @param ltiCourseId lti course id (context_id)
     * @param ltiActivityId lti activity id (lti_context_id)
     * @param consumerName consumer name
     * @param username user who's connect from moodle, tsaap notes username
     * @param isLearner user is a learner or not
     * @return res an arrayList who contain tsaap notes context name and context id
     */
    def findOrCreateContext(Sql sql, String consumerKey, String ltiCourseId, String ltiActivityId, String consumerName, String contextTitle, String username, Boolean isLearner){
        def contextName = null
        def contextId = lmsContextHelper.selectLmsContext(sql,consumerKey,ltiCourseId,ltiActivityId)
        if(contextId == null) {
            contextName = contextTitle
            // We create a new context
            if(!isLearner) {
                def userId = lmsUserHelper.selectUserId(sql,username)
                lmsContextHelper.insertContext(sql,contextName,"",userId,true,"",consumerName)
                contextId = lmsContextHelper.selectContextId(sql,contextName,consumerName)
                lmsContextHelper.insertLmsContext(sql,contextId,ltiCourseId,ltiActivityId,consumerKey,consumerName)
            }
            else {
                throw new LtiContextInitialisationException("error.lti.context.initialisation")
            }
        }
        else {
            contextName = lmsContextHelper.selectContextName(sql,contextId)
        }
        [contextName,contextId]
    }

    /**
     * Delete an Lms context for a given tsaap note context
     * @param sql
     * @param contextId tsaap note context id
     */
    def deleteLmsContextForContext(Sql sql, long contextId) {
        def exist = lmsContextHelper.selectLmsContextForContextId(sql,contextId)
        if(exist != null) {
            lmsContextHelper.deleteLmsContext(sql,contextId)
        }
    }
}
