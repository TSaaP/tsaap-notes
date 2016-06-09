package org.tsaap.lti

import groovy.sql.Sql
import org.tsaap.lti.tp.Outcome
import org.tsaap.lti.tp.ResourceLink
import org.tsaap.lti.tp.User


class LmsGradeService {

    /**
     * Get grades for lti users in a given context
     * @param sql
     * @param contextId context id
     * @return a map of grades
     */
    def getUsersGradeForContext(Sql sql, Long contextId) {
        def questionNumber = sql.firstRow("SELECT count(live_session.id) as questions FROM live_session,note " +
                "where status = 'Ended' " +
                "and kind = 1 " +
                "and note.id = note_id " +
                "and note.context_id = $contextId").questions
        def rows = sql.rows("select lti_user.user_id, live_session.id as session_id, percent_credit " +
                "from live_session_response,live_session,note,lms_user,lms_context,lti_user " +
                "where status = 'Ended' " +
                "and kind = 1 " +
                "and note.id = note_id " +
                "and live_session_id = live_session.id " +
                "and note.context_id = $contextId " +
                "and live_session_response.user_id = lms_user.tsaap_user_id " +
                "and lms_user.lti_user_id = lti_user.user_id " +
                "and lti_user.context_id = lms_context.lti_course_id " +
                "and lms_context.tsaap_context_id = $contextId " +
                "order by user_id, session_id, session_phase_id")
        def grades = [:]
        def grade = [:]
        rows.each {
            def key = it.user_id
            if (grades[key] == null) {
                grades[key] = [:]
                grade = [:]
            }
            def grade_key = it.session_id
            if (grade[grade_key] == null) {
                grade[grade_key] = 0
            }
            grade[grade_key] = it.percent_credit
            grades[key] << grade
        }
        sql.close()
        def finalGrades = [:]
        grades.each { user, gradesMap ->
            def finalKey = user
            if (finalGrades[finalKey] == null) {
                finalGrades[finalKey] = 0
            }
            int finalGrade = 0
            gradesMap.each { questionGrade, credit ->
                if (credit == null) {
                    credit = 0
                }
                finalGrade += credit
            }
            finalGrade = finalGrade / questionNumber
            finalGrades[finalKey] = finalGrade
        }
        finalGrades
    }

    /**
     * Send grade to Lms for a given lti user
     * @param resourceLink
     * @param user lti user
     * @param grade user grade to send
     */
    def sendUserGradeToLms(ResourceLink resourceLink, User user, int grade) {
        Float finalGrade = grade / 100
        Outcome outcome = new Outcome("$finalGrade")
        resourceLink.doOutcomesService(resourceLink.EXT_WRITE, outcome, user)
    }
}
