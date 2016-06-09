package org.tsaap.lti

import groovy.sql.Sql
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.SQLException

/**
 * Created by dorian on 07/07/15.
 */
class LmsGradeServiceIntegrationSpec extends Specification {

    LmsGradeService lmsGradeService
    DataSource dataSource
    Sql sql
    LmsUserHelper lmsUserHelper
    LmsContextHelper lmsContextHelper

    def setup() {
        lmsGradeService = new LmsGradeService()
        sql = new Sql(dataSource)
        lmsUserHelper = new LmsUserHelper()
        lmsContextHelper = new LmsContextHelper()
    }

    def "test get users grades for context"() {

        when: "I want to get users grade for the context"
        def res = null
        try {
            sql.withTransaction { ->
                // Create users, roles and context for test
                lmsUserHelper.insertUserInDatabase(sql, 'test@mail.com', 'tea', 'cher', 'tche', 'pass', true)
                lmsUserHelper.insertUserInDatabase(sql, 'test2@mail.com', 'john', 'doe', 'jdoe', 'pass', true)
                lmsUserHelper.insertUserInDatabase(sql, 'test3@mail.com', 'jane', 'did', 'jdid', 'pass', true)
                long userId1 = lmsUserHelper.selectUserId(sql, "jdoe")
                long userId2 = lmsUserHelper.selectUserId(sql, "jdid")
                long userId3 = lmsUserHelper.selectUserId(sql, "tche")
                lmsUserHelper.insertUserRoleInDatabase(sql, 2, userId1)
                lmsUserHelper.insertUserRoleInDatabase(sql, 2, userId2)
                lmsUserHelper.insertUserRoleInDatabase(sql, 3, userId3)
                lmsContextHelper.insertContext(sql, "context1", "description", userId3, true, "url", "test")
                long contextId = lmsContextHelper.selectContextId(sql, "context1", "test")

                // Create lti consumer, context, users and lms context and users for test
                lmsUserHelper.insertLtiConsumerInDatabase(sql, 'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql, "key", "3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "10", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"10\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                lmsUserHelper.insertLtiUserInDatabase(sql, "key", "3", "11", "{\"data\":{\"instanceid\":\"3\",\"userid\":\"11\",\"typeid\":\"1\",\"launchid\":1369810096},\"hash\":\"e6e30cc48a52c8344c0f5be4ffc3ea2194490a86cf42aa811d843f375c5e1cea\"}")
                lmsContextHelper.insertLmsContext(sql, contextId, "3", "4", "key", "Moodle")
                lmsUserHelper.insertLmsUserInDatabase(sql, userId1, "key", "10")
                lmsUserHelper.insertLmsUserInDatabase(sql, userId2, "key", "11")

                // Create notes as question for test
                sql.execute("INSERT INTO note (author_id, content, context_id, date_created, parent_note_id, fragment_tag_id, score, grade, kind) VALUES " +
                        "($userId3,'::a question:: What ? {=this ~that}',$contextId,now(),null,null,0,null,1)")
                sql.execute("INSERT INTO note (author_id, content, context_id, date_created, parent_note_id, fragment_tag_id, score, grade, kind) VALUES " +
                        "($userId3,'::a question2:: What ? {=this2 ~that2}',$contextId,now(),null,null,0,null,1)")

                // Create live session and response for test
                def noteId1 = sql.firstRow("SELECT id FROM note WHERE content = '::a question:: What ? {=this ~that}'").id
                def noteId2 = sql.firstRow("SELECT id FROM note WHERE content = '::a question2:: What ? {=this2 ~that2}'").id
                sql.execute("INSERT INTO live_session (version, date_created, end_date, note_id, start_date, status, result_matrix_as_json) VALUES " +
                        "(0,now(),now(),$noteId1,now(),'Ended','[{\"_NO_RESPONSE_\":0,\"this\":100,\"that\":0}]')")
                sql.execute("INSERT INTO live_session (version, date_created, end_date, note_id, start_date, status, result_matrix_as_json) VALUES " +
                        "(0,now(),now(),$noteId2,now(),'Ended','[{\"_NO_RESPONSE_\":0,\"this2\":100,\"that2\":0}]')")
                def liveSessionId1 = sql.firstRow("SELECT id FROM live_session WHERE note_id = $noteId1").id
                def liveSessionId2 = sql.firstRow("SELECT id FROM live_session WHERE note_id = $noteId2").id
                sql.execute("INSERT INTO session_phase (version, date_created, end_date, start_date, status, rank, result_matrix_as_json, live_session_id, mapping_user_explanation, mapping_response_conflict_response) VALUES " +
                        "(2,now(),now(),now(),'Ended',1,'[{\"_NO_RESPONSE_\":0,\"this2\":50.0,\"that2\":50.0}]',$liveSessionId2,null,null)")
                sql.execute("INSERT INTO session_phase (version, date_created, end_date, start_date, status, rank, result_matrix_as_json, live_session_id, mapping_user_explanation, mapping_response_conflict_response) VALUES " +
                        "(2,now(),now(),now(),'Ended',2,'[{\"_NO_RESPONSE_\":0,\"this2\":100.0,\"that2\":0}]',$liveSessionId2,null,null)")
                sql.execute("INSERT INTO session_phase (version, date_created, end_date, start_date, status, rank, result_matrix_as_json, live_session_id, mapping_user_explanation, mapping_response_conflict_response) VALUES " +
                        "(2,now(),now(),now(),'Ended',3,null,$liveSessionId2,null,null)")
                sql.execute("INSERT INTO live_session_response (version, answer_list_as_string, live_session_id, percent_credit, user_id, session_phase_id, confidence_degree, explanation_id) VALUES " +
                        "(1,'[[\"0\"]]',$liveSessionId1,100,$userId1,null,null,null)")
                def sessionPhase1Id = sql.firstRow("SELECT id FROM session_phase WHERE live_session_id = $liveSessionId2 and rank = 1").id
                sql.execute("INSERT INTO live_session_response (version, answer_list_as_string, live_session_id, percent_credit, user_id, session_phase_id, confidence_degree, explanation_id) VALUES " +
                        "(1,'[[\"0\"]]',$liveSessionId2,100,$userId1,$sessionPhase1Id,5,null)")
                sql.execute("INSERT INTO live_session_response (version, answer_list_as_string, live_session_id, percent_credit, user_id, session_phase_id, confidence_degree, explanation_id) VALUES " +
                        "(1,'[[\"1\"]]',$liveSessionId2,0,$userId2,$sessionPhase1Id,1,null)")
                def sessionPhase2Id = sql.firstRow("SELECT id FROM session_phase WHERE live_session_id = $liveSessionId2 and rank = 2").id
                sql.execute("INSERT INTO live_session_response (version, answer_list_as_string, live_session_id, percent_credit, user_id, session_phase_id, confidence_degree, explanation_id) VALUES " +
                        "(2,'[[\"0\"]]',$liveSessionId2,null,$userId1,$sessionPhase2Id,5,null)")
                sql.execute("INSERT INTO live_session_response (version, answer_list_as_string, live_session_id, percent_credit, user_id, session_phase_id, confidence_degree, explanation_id) VALUES " +
                        "(2,'[[\"0\"]]',$liveSessionId2,100,$userId2,$sessionPhase2Id,5,null)")

                // Get users grade
                res = lmsGradeService.getUsersGradeForContext(sql, contextId)

                throw new SQLException()
            }
        }
        catch (SQLException e) {
        }

        then: "I get a map with users grades"
        res != null
        res.get("10") == 50
        res.get("11") == 50
    }
}
