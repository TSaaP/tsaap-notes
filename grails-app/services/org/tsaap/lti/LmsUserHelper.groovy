package org.tsaap.lti

import groovy.sql.Sql

import java.sql.Connection

/**
 * Created by dorian on 18/06/15.
 */
class LmsUserHelper {

    /**
     * Get a lms user for a lti user id
     * @param sql
     * @param ltiUserId lti user id
     * @return an user id if exist else null
     */
    def static selectLmsUser(Sql sql, String ltiUserId) {
        def req = sql.firstRow("SELECT * FROM lms_user WHERE lti_user_id = $ltiUserId")
        def res = null
        if(req != null){
            res = req.tsaap_user_id
        }
        res
    }

    /**
     * Get the most recent user who begin with username param
     * @param sql
     * @param username user username
     * @return an username if found else null
     */
    def static selectUsernameIfExist(Sql sql, String username) {
        def req = sql.firstRow("SELECT username FROM user WHERE username LIKE '"+username+"%' ORDER BY username DESC")
        def res = null
        if(req != null){
            res = req.username
        }
        res
    }

    /**
     * Insert an user
     * @param sql
     * @param email user email
     * @param firstname user first name
     * @param lastname user last name
     * @param username user username
     * @param password user password
     */
    def static insertUserInDatabase(Sql sql, String email, String firstname, String lastname, String username, String password) {
        sql.execute("INSERT INTO user (account_expired,account_locked,email,enabled,first_name,last_name," +
                "normalized_username,password,password_expired,username,version,language) " +
                "VALUES (0,0,'$email',1,'$firstname','$lastname','$username','$password',0,'$username',0,'en')")
    }

    /**
     * Get user id for an username
     * @param sql
     * @param username user username
     * @return the user id
     */
    def static selectUserId(Sql sql, String username) {
        def req = sql.firstRow("SELECT id FROM user where username = $username ")
            Long res = req.id
        res
    }

    /**
     * Insert a user role for an user
     * @param sql
     * @param role user role
     * @param userId user id
     */
    def static insertUserRoleInDatabase(Sql sql, Integer role, Long userId){
        sql.execute("INSERT INTO user_role VALUES ($role,$userId)")
    }

    /**
     * Insert a lms user
     * @param sql
     * @param userId user id
     * @param consumerKey
     * @param ltiUserId lti user id
     */
    def static insertLmsUserInDatabase(Sql sql, Long userId, String consumerKey, String ltiUserId) {
        sql.execute("INSERT INTO lms_user VALUES ($userId,$consumerKey,$ltiUserId)")
    }

    /**
     * Get tsaap username and password for a lti user
     * @param sql
     * @param ltiUserId LMS user id
     * @return res a map containing tsaap username and password for the LMS user
     */
    def static selectUsernameAndPassword(Sql sql, String ltiUserId) {
        def req = sql.firstRow("SELECT username,password FROM user,lms_user WHERE tsaap_user_id = id AND lti_user_id = $ltiUserId")
        def res = [username:req.username,password:req.password]
        res
    }


    /**
     * Insert an lti_consumer in database
     * @param sql
     * @param consumerKey consumer key
     * @param name consumer name for app
     * @param secret consumer secret
     * @param ltiVersion
     * @param consumerName consumer real name
     * @param consumerVersion
     * @param consumerGuid
     * @param cssPath
     * @param protect lti is protected
     * @param enabled lti is enabled
     * @param from  consumer beginning date
     * @param until consumer expiration date
     */
    def static insertLtiConsumerInDatabase(Sql sql, String consumerKey, String name, String secret, String ltiVersion, String consumerName,
                                    String consumerVersion, String consumerGuid, String cssPath, Integer protect,
                                    Integer enabled, Date from, Date until) {
        sql.execute("INSERT INTO lti_consumer (consumer_key, name, secret, lti_version, consumer_name, consumer_version, consumer_guid, css_path, protected, enabled, enable_from, enable_until, last_access, created, updated) " +
                    "VALUES ('$consumerKey', '$name', '$secret', '$ltiVersion', '$consumerName', '$consumerVersion', '$consumerGuid', $cssPath, $protect, $enabled, $from, $until, now(), now(), now())")
    }

    /**
     * Insert a lti_context in database
     * @param sql
     * @param consumerKey consumer key
     * @param contextId LMS id
     * @param ltiContextId course id
     * @param ltiResourceId LMS id
     * @param title course title
     * @param settings course settings
     */
    def static insertLtiContextInDatabase(Sql sql, String consumerKey, String contextId, String ltiContextId, String ltiResourceId,
                                   String title, String settings) {
        sql.execute("INSERT INTO lti_context (consumer_key, context_id, lti_context_id, lti_resource_id, title, settings, primary_consumer_key, primary_context_id, share_approved, created, updated) " +
                    "VALUES ('$consumerKey', '$contextId', '$ltiContextId', '$ltiResourceId', '$title', '$settings', null, null, null, now(), now())")
    }

    /**
     * Insert a lti_user in database
     * @param sql
     * @param consumerKey consumer key
     * @param contextId LMS id
     * @param userId LMS user_id
     * @param ltiResultSourcedid user's informations
     */
    def static insertLtiUserInDatabase(Sql sql, String consumerKey, String contextId, String userId, String ltiResultSourcedid) {
        sql.execute("INSERT INTO lti_user (consumer_key, context_id, user_id, lti_result_sourcedid, created, updated) " +
                    "VALUES ('$consumerKey', '$contextId', '$userId', '$ltiResultSourcedid', now(), now())")
    }
}
