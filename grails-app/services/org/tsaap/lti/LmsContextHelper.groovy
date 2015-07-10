package org.tsaap.lti

import groovy.sql.Sql

/**
 * Created by dorian on 29/06/15.
 */
class LmsContextHelper {

    /**
     * find if an lsm_context exist for given lti instance id and lti context id
     * @param ltiCourseId lti course id
     * @param ltiActivityId lti activity id
     * @return a tsaap context id if the lms context exist else null
     */
    def selectLmsContext(Sql sql, String consumerKey, String ltiCourseId, String ltiActivityId) {
        def req = sql.firstRow("SELECT tsaap_context_id FROM lms_context WHERE lti_consumer_key = $consumerKey and lti_activity_id = $ltiActivityId and lti_course_id = $ltiCourseId")
        def res = null
        if(req != null)
        {
            res= req.tsaap_context_id
        }
        res
    }

    /**
     * Insert a new context in database
     * @param sql
     * @param contextName context name
     * @param description context description
     * @param owner context owner
     * @param isTeacher
     * @param url context url
     * @param source lti context source
     */
    def insertContext(Sql sql, String contextName, String description, long owner, Boolean isTeacher, String url, String source) {
        sql.execute("INSERT INTO context (context_name, date_created, description_as_note, last_updated, owner_id, owner_is_teacher, url, source) VALUES " +
                "('$contextName',now(),'$description',now(),$owner,$isTeacher,'$url','$source')")
    }

    /**
     * Select a context Id for a given contextName and source
     * @param sql
     * @param contextName tsaap context name
     * @param source lti context source
     * @return res the context id
     */
    def selectContextId(Sql sql, String contextName, String source) {
        def req = sql.firstRow("SELECT id FROM context WHERE context_name = $contextName and source = $source")
        Long res = req.id
        res
    }

    /**
     * Select a context name for a given context id
     * @param sql
     * @param tsaapContextId context id
     * @return res context name
     */
    def selectContextName(Sql sql, long tsaapContextId) {
        def req = sql.firstRow("SELECT context_name FROM context WHERE id = $tsaapContextId")
        def res = req.context_name
        res
    }

    /**
     * Insert a new Lms context in database
     * @param sql
     * @param tsaapContextId tsaap note context id
     * @param ltiCourseId lti course id
     * @param ltiActivityId lti activity id
     * @param consumerKey lti consumer key
     * @param source lti consumer name
     */
    def insertLmsContext(Sql sql, long tsaapContextId, String ltiCourseId, String ltiActivityId, String consumerKey, String source) {
        sql.execute("INSERT INTO lms_context VALUES ($tsaapContextId,$ltiCourseId,$ltiActivityId,$consumerKey,$source)")
    }

    /**
     * Select a lms context for a given tsaap context id
     * @param sql
     * @param contextId tsaap context id
     */
    def selectLmsContextForContextId(Sql sql,long contextId) {
        def res = sql.firstRow("SELECT tsaap_context_id FROM lms_context WHERE tsaap_context_id = $contextId")
        res
    }

    /**
     * Delete a lms context for a given tsaap context id
     * @param sql
     * @param contextId tsaap context id
     */
    def deleteLmsContext(Sql sql, long contextId) {
        sql.execute("DELETE FROM lms_context WHERE tsaap_context_id = $contextId")
    }

    /**
     * Select a consumer key and lti course id for a given context id
     * @param sql
     * @param contextId context id
     * @return res an array with consumer key and lti course id
     */
    def selectConsumerKeyAndCourseId(Sql sql, long contextId) {
        def req = sql.firstRow("SELECT lti_consumer_key,lti_course_id from lms_context WHERE tsaap_context_id = $contextId")
        def res = [req.lti_consumer_key,req.lti_course_id]
        res
    }
}
