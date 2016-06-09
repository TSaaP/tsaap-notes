package org.tsaap.questions

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.gcontracts.annotations.Requires
import org.tsaap.directory.User

//import org.hibernate.mapping.List
import org.tsaap.ia.conflict.SocioCognitiveConflictService

class SessionPhase {

    public static final int MAX_RANK = 3

    Date dateCreated
    String status = LiveSessionStatus.NotStarted.name()
    Date startDate
    Date endDate
    String resultMatrixAsJson
    Integer rank
    // Json string
    String mappingUserExplanation
    String mappingResponseConflictResponse

    LiveSession liveSession

    static constraints = {
        status inList: LiveSessionStatus.values()*.name()
        startDate nullable: true
        endDate nullable: true
        resultMatrixAsJson nullable: true
        mappingUserExplanation nullable: true
        mappingResponseConflictResponse nullable: true
    }

    /**
     * Flag to indicate if the phase is not started
     * @return true if the phase is not started,false otherwise
     */
    boolean isNotStarted() {
        status == LiveSessionStatus.NotStarted.name()
    }

    /**
     * Flag to indicate if the phase is started
     * @return true if the phase is started,false otherwise
     */
    boolean isStarted() {
        status == LiveSessionStatus.Started.name()
    }

    /**
     * Flag to indicate if the phase is stopped
     * @return true if the phase is stopped,false otherwise
     */
    boolean isStopped() {
        status == LiveSessionStatus.Ended.name()
    }

    /**
     * Start the current phase
     */
    @Requires({ isNotStarted() })
    def start() {
        status = LiveSessionStatus.Started.name()
        startDate = new Date()
        save(flush: true)
    }

    /**
     * Stop the current phase
     */
    @Requires({ isStarted() })
    def stop(boolean shouldBuildResultMatrix = true) {
        status = LiveSessionStatus.Ended.name()
        endDate = new Date()
        if (shouldBuildResultMatrix) {
            updateResultMatrixAsJson()
        }
        if (rank == 2) {
            updateMappingUserExplanationAsJson()
        }
        if (rank == 1) {
            updateMappingConflictResponseResponseAsJson()
        }
        save(flush: true)
        if (hasErrors()) {
            log.error(errors.allErrors)
        }
    }

    private def void updateResultMatrixAsJson() {
        def matrix = buildResultMatrix()
        JsonBuilder builder = new JsonBuilder(matrix ?: [:])
        resultMatrixAsJson = builder.toString()
    }

    /**        if (rank == 3) {liveSessionService}* Get the response of the current session phase for a given user
     * @param user the given user
     * @return the live session response if it exists
     */
    LiveSessionResponse getResponseForUser(User user) {
        LiveSessionResponse.findBySessionPhaseAndUser(this, user, [fetch: [explanation: 'join']])
    }

    /**
     * get the count of responses for the current live session
     * @return the count of responses
     */
    Integer responseCount() {
        if (rank == MAX_RANK) {
            return countSubmittedEvaluations(liveSession)
        }
        LiveSessionResponse.countBySessionPhase(this)
    }

    private def countSubmittedEvaluations(LiveSession liveSession) {
        def count = LiveSession.executeQuery(
                '''
                select count(distinct ng.user) from LiveSessionResponse lsr, NoteGrade ng
                where lsr.liveSession = :liveSessionId and lsr.explanation = ng.note
                ''',
                [liveSessionId: liveSession])
        count[0]
    }

    /**
     * Construct the result matrix of the current live session
     * @return the result matrix
     */
    List<Map<String, Float>> getResultMatrix() {
        JsonSlurper parser = new JsonSlurper()
        if (resultMatrixAsJson == null) {
            updateResultMatrixAsJson()
            save(flush: true)
        }
        def matrix = parser.parseText(resultMatrixAsJson)
        matrix
    }

    ResultMatrixService resultMatrixService

    /**
     * Construct the result matrix of the current live session
     * @return the result matrix
     */
    List<Map<String, Float>> buildResultMatrix() {
        def responses = LiveSessionResponse.findAllBySessionPhase(this)
        Question question = this.liveSession.note.question
        resultMatrixService.buildResultMatrixForQuestionAndResponses(question, responses)
    }

    /**
     *
     * @return true if have to stop the live session when this phase is stopped
     */
    boolean stopLiveSessionWhenIsStopped() {
        rank == MAX_RANK
    }

    /**
     * the map matching users to explanation to evaluate
     * @return the map
     */
    Map<String, List<Long>> getMappingUserExplanationAsMap() {
        JsonSlurper parser = new JsonSlurper()
        if (mappingUserExplanation == null) {
            updateMappingUserExplanationAsJson()
            save(flush: true)
        }
        Map<String, List<Long>> res = parser.parseText(mappingUserExplanation)
        res
    }

    private def void updateMappingUserExplanationAsJson() {
        def map = buildMappingUserExplanation()
        JsonBuilder builder = new JsonBuilder(map ?: [:])
        mappingUserExplanation = builder.toString()
    }

    SocioCognitiveConflictService socioCognitiveConflictService

    /**
     * Construct the map matching users to explanation to evaluate
     * @return
     */
    Map<Long, List<Long>> buildMappingUserExplanation() {
        def responseList = LiveSessionResponse.findAllBySessionPhase(this)
        socioCognitiveConflictService.explanationIdListByUserId(responseList)
    }

    /**
     * the map matching users to explanation to evaluate
     * @return the map
     */
    Map<String, Long> getMappingResponseConflictResponseAsMap() {
        JsonSlurper parser = new JsonSlurper()
        if (mappingResponseConflictResponse == null) {
            updateMappingConflictResponseResponseAsJson()
            save(flush: true)
        }
        Map<String, Long> res = parser.parseText(mappingResponseConflictResponse)
        res        //
    }

    private def void updateMappingConflictResponseResponseAsJson() {
        def map = buildMappingResponsesConflict()
        JsonBuilder builder = new JsonBuilder(map ?: [:])
        mappingResponseConflictResponse = builder.toString()
    }

    /**
     * Construct the map matching users to explanation to evaluate
     * @return
     */
    Map<Long, Long> buildMappingResponsesConflict() {
        def responseList = LiveSessionResponse.findAllBySessionPhase(this)
        socioCognitiveConflictService.responseConflictIdByResponseId(responseList)
    }

    /**
     *
     * @param response the response to find the conflict response
     * @return the conflict response
     */
    LiveSessionResponse findConflictResponseForResponse(LiveSessionResponse response) {
        if (response == null) {
            return null
        }
        Map<String, Long> map = getMappingResponseConflictResponseAsMap()
        String key = response.id as String
        Long val = map.get(key)
        LiveSessionResponse.get(val)
    }

    List<LiveSessionResponse> findAllResponsesToEvaluateForResponse(LiveSessionResponse response) {
        if (response == null) {
            return []
        }
        Map<String, List<Long>> map = getMappingUserExplanationAsMap()
        String key = response.userId as String
        List<Long> respIds = map.get(key)
        LiveSessionResponse.getAll(respIds)
    }

    static transients = ['resultMatrix', 'resultMatrixService', 'MAX_RANK', 'socioCognitiveConflictService']
}
