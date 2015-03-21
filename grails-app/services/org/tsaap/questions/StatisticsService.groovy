package org.tsaap.questions

import grails.transaction.Transactional
import org.hibernate.SQLQuery
import org.hibernate.Session
import org.hibernate.SessionFactory

@Transactional
class StatisticsService {

    SessionFactory sessionFactory

    /**
     * Get statistics on a live session
     * @param liveSession the live session
     * @return the built NPhasesLiveSessionStatistics
     */
    NPhasesLiveSessionStatistics getNPhasesLiveSessionStatisticsForLiveSession(LiveSession liveSession) {
        NPhasesLiveSessionStatistics res = new NPhasesLiveSessionStatistics(
                liveSessionId: liveSession.id,
                contextId: liveSession?.note?.contextId,
                contextName: liveSession?.note?.context?.contextName
        )
        // the stats
        Session currentSession = sessionFactory.currentSession

        SessionPhase phase1 = liveSession.findFirstPhase()
        SessionPhase phase2 = liveSession.findSecondPhase()

        //
        // Numbers of answers
        //

        String queryNumberOfAnswersOnPhase = 'select count(*) from live_session_response where session_phase_id = :phaseId ;'
        String queryNumberOfGoodAnswersOnPhase = 'select count(*) from live_session_response where session_phase_id = :phaseId and percent_credit = 100 ;'

        SQLQuery sqlQuery = currentSession.createSQLQuery(queryNumberOfAnswersOnPhase)
        def queryResults = sqlQuery.with {
                    setLong('phaseId', phase1.id)
                    list()
        }
        res.numberOfAnswersOnPhase1 = queryResults.get(0)

        queryResults = sqlQuery.with {
            setLong('phaseId', phase2.id)
            list()
        }

        res.numberOfAnswersOnPhase2 = queryResults.get(0)

        sqlQuery = currentSession.createSQLQuery(queryNumberOfGoodAnswersOnPhase)
       queryResults = sqlQuery.with {
            setLong('phaseId', phase1.id)
            list()
        }
        res.numberOfGoodAnswersOnPhase1 = queryResults.get(0)

        queryResults = sqlQuery.with {
            setLong('phaseId', phase2.id)
            list()
        }

        res.numberOfGoodAnswersOnPhase2 = queryResults.get(0)

        res.numberOfBadAnswersOnPhase1 = res.numberOfAnswersOnPhase1 - res.numberOfGoodAnswersOnPhase1
        res.numberOfBadAnswersOnPhase2 = res.numberOfAnswersOnPhase2 - res.numberOfGoodAnswersOnPhase2

        //
        // Numbers of explanations
        //

        String queryNumberOfExplanationsOnPhase = 'select count(*) from live_session_response, note as expl where session_phase_id = :phaseId and `explanation_id` = expl.id and content is not null;'
        String queryNumberOfGoodExplanationsOnPhase = 'select count(*) from live_session_response, note as expl where session_phase_id = :phaseId and `explanation_id` = expl.id and content is not null and percent_credit = 100 ;'

        sqlQuery = currentSession.createSQLQuery(queryNumberOfExplanationsOnPhase)
        queryResults = sqlQuery.with {
            setLong('phaseId', phase1.id)
            list()
        }
        res.numberOfExplanationsOnPhase1 = queryResults.get(0)

        queryResults = sqlQuery.with {
            setLong('phaseId', phase2.id)
            list()
        }

        res.numberOfExplanationsOnPhase2 = queryResults.get(0)

        sqlQuery = currentSession.createSQLQuery(queryNumberOfGoodExplanationsOnPhase)
        queryResults = sqlQuery.with {
            setLong('phaseId', phase1.id)
            list()
        }
        res.numberOfGoodExplanationsOnPhase1 = queryResults.get(0)

        queryResults = sqlQuery.with {
            setLong('phaseId', phase2.id)
            list()
        }

        res.numberOfGoodExplanationsOnPhase2 = queryResults.get(0)

        res.numberOfBadExplanationsOnPhase1 = res.numberOfExplanationsOnPhase1 - res.numberOfGoodExplanationsOnPhase1
        res.numberOfBadExplanationsOnPhase2 = res.numberOfExplanationsOnPhase2 - res.numberOfGoodExplanationsOnPhase2

        //
        // On evaluations
        //

        String queryNumberOfUserHavingGivenAnEvaluation = 'select count(*), avg(temp_table.number_exp_eval) from (select user_id, count(*) as number_exp_eval from `note_grade`, `note`, `live_session`  where `live_session`.id = :liveSessionId and `note_grade`.`note_id` = `note`.`id` and `note`.`parent_note_id` = `live_session`.`note_id` group by `user_id`) as temp_table;'
        String queryMeanOfEvaluationPerExplanations = ''
        String queryMeanOfExplanationPerEvaluator = ''
        String queryMeanOfStandardDeviationOnEvalutations = ''

        sqlQuery = currentSession.createSQLQuery(queryNumberOfUserHavingGivenAnEvaluation)
        queryResults = sqlQuery.with {
            setLong('liveSessionId', liveSession.id)
            list()
        }
        res.numberOfUserHavingGivenAnEvaluation = queryResults.get(0)

    }
}
