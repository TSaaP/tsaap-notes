/*
 * Copyright 2016 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tsaap.questions

import grails.transaction.Transactional
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.hibernate.SessionFactory

@Transactional
class ResultListService {

    SessionFactory sessionFactory

    /**
     * Get result list on a live session
     * @param liveSession the live session
     * @return the built NPhasesLiveSessionResultList
     */
    NPhasesLiveSessionResultList getNPhasesLiveSessionResultListForLiveSession(LiveSession liveSession) {
        NPhasesLiveSessionResultList res = new NPhasesLiveSessionResultList(
                liveSessionId: liveSession.id,
                contextId: liveSession?.note?.contextId,
                contextName: liveSession?.note?.context?.contextName,
                liveSessionStartDate: liveSession.startDate,
                liveSessionEndDate: liveSession.endDate,
                questionId: liveSession.noteId,
                question: liveSession.note?.content
        )
        // the stats
        Session currentSession = sessionFactory.currentSession

        SessionPhase phase1 = liveSession.findFirstPhase()
        SessionPhase phase2 = liveSession.findSecondPhase()

        //
        // Results
        //

        String query = '''
        select
        last_name, first_name, username, lti_user_id, lti_consumer_key,
        response_1.answer_list_as_string as first_answer, note_1.content as first_explanation,
        response_1.confidence_degree as first_confidence_degree, response_1.`percent_credit` as first_score,
        response_2.answer_list_as_string as second_answer, note_2.content as second_explanation,
        response_2.confidence_degree as second_confidence_degree, response_2.`percent_credit` as second_score
        from
        user, `lms_user`,
        `live_session_response` as response_1, `note` as note_1,
        `live_session_response` as response_2, `note` as note_2
        where
        user.id =  `lms_user`.`tsaap_user_id` and
        user.id = response_1.`user_id` and
        response_1.`session_phase_id` = :phase1Id and
        note_1.id = response_1.`explanation_id` and
        user.id = response_2.`user_id` and
        response_2.`session_phase_id` = :phase2Id and
        note_2.id = response_2.`explanation_id`
        ORDER BY
        last_name, first_name
        '''


        SQLQuery sqlQuery = currentSession.createSQLQuery(query)
        res.resultList = sqlQuery.with {
                    addEntity(NPhasesLiveSessionResult)
                    setLong('phase1Id', phase1.id)
                    setLong('phase2Id', phase2.id)
                    list()
        }

        //
        // return res

        res

    }

    /**
     * Get result list on a live session id
     * @param liveSessionId the live session id
     * @return the built NPhasesLiveSessionResultList
     */
    NPhasesLiveSessionResultList getNPhasesLiveSessionResultListForLiveSessionId(Long liveSessionId) {
        LiveSession liveSession = LiveSession.get(liveSessionId)
        getNPhasesLiveSessionResultListForLiveSession(liveSession)
    }

    Map nPhaseSessionResultListLabels() {
        [
                contextId: "Context id",
                contextName: "Context name",
                liveSessionId: "Live session id",
                liveSessionStartDate: "Start date",
                liveSessionEndDate: "End date",
        ]
    }
}
