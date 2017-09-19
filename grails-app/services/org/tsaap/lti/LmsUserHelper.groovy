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
 * Lti consumer (LMS) helper class
 */
class LmsUserHelper {

    /**
     * Get a lms user for a lti user id
     * @param sql
     * @param ltiUserId lti user id
     * @return an user id if exist else null
     */
    Long findUserIdForLtiUserId(Sql sql, String ltiUserId, String ltiConsumerKey) {
        def req = sql.firstRow("SELECT tsaap_user_id FROM lms_user WHERE lti_user_id = $ltiUserId and lti_consumer_key = $ltiConsumerKey")
        def res = null
        if (req != null) {
            res = req.tsaap_user_id
        }
        res
    }


    /**
     * Insert a user
     * @param sql
     * @param LmsUser the lms user to insert
     * @return the lms user with user id set
     */
    LmsUser insertUserInDatabase(Sql sql, LmsUser lmsUser) {
        sql.execute("INSERT INTO user (account_expired,account_locked,email,enabled,first_name,last_name,normalized_username,password,password_expired,username,version) VALUES (0,0,$lmsUser.email,$lmsUser.isEnabled,$lmsUser.firstname,$lmsUser.lastname,$lmsUser.username,$lmsUser.password,0,$lmsUser.username,0)")
        lmsUser.userId = findUserIdForUsername(sql, lmsUser.username)
        lmsUser
    }

    /**
     * Get local user id for an username
     * @param sql
     * @param username user username
     * @return the user id
     */
    Long findUserIdForUsername(Sql sql, String username) {
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
    def insertUserRoleInDatabase(Sql sql, Long role, Long userId) {
        sql.execute("INSERT INTO user_role VALUES ($role,$userId)")
    }

    /**
     * Insert a lms user
     * @param sql
     * @param userId user id
     * @param consumerKey
     * @param ltiUserId lti user id
     */
    def insertLmsUserInDatabase(Sql sql, Long userId, String consumerKey, String ltiUserId) {
        sql.execute("INSERT INTO lms_user VALUES ($userId,$consumerKey,$ltiUserId)")
    }

    /**
     * Get tsaap username and password for a lti user
     * @param sql
     * @param ltiUserId LMS user id
     * @return res a map containing tsaap username and password for the LMS user
     */
    def selectUsernameAndPassword(Sql sql, String consumerKey, String ltiUserId) {
        def req = sql.firstRow("SELECT username,password FROM user,lms_user WHERE tsaap_user_id = id AND lti_user_id = $ltiUserId AND lti_consumer_key = $consumerKey")
        def res = [username: req.username, password: req.password]
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
     * @param from consumer beginning date
     * @param until consumer expiration date
     */
    def insertLtiConsumerInDatabase(Sql sql, String consumerKey, String name, String secret, String ltiVersion, String consumerName,
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
    def insertLtiContextInDatabase(Sql sql, String consumerKey, String contextId, String ltiContextId, String ltiResourceId,
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
    def insertLtiUserInDatabase(Sql sql, String consumerKey, String contextId, String userId, String ltiResultSourcedid) {
        sql.execute("INSERT INTO lti_user (consumer_key, context_id, user_id, lti_result_sourcedid, created, updated) " +
                "VALUES ('$consumerKey', '$contextId', '$userId', '$ltiResultSourcedid', now(), now())")
    }

    /**
     * Select if an user account is enable for a given username
     * @param sql
     * @param username user username
     * @return res account is enable
     */
    boolean selectUserIsEnable(Sql sql, String username) {
        def req = sql.firstRow("SELECT enabled FROM user WHERE username = $username")
        def res = req.enabled
        res
    }

    /**
     * Enable tsaap account for a given username
     * @param sql
     * @param username user username
     */
    def enableUser(Sql sql, def username) {
        sql.executeUpdate("UPDATE user SET enabled = 1 WHERE username = $username")
    }

    /**
     * Select a lti user id for a given user id
     * @param sql
     * @param userId user id
     */
    def selectLtiUserId(Sql sql, long userId) {
        def req = sql.firstRow("SELECT lti_user_id from lms_user WHERE tsaap_user_id = $userId")
        def res = req.lti_user_id
        res
    }
}
