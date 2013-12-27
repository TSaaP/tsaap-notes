package org.tsaap.questions

import org.gcontracts.annotations.Requires
import org.tsaap.directory.User
import org.tsaap.notes.Note

class LiveSession {

    Date dateCreated
    String status = LiveSessionStatus.NotStarted.name()
    Date startDate
    Date endDate

    Note note

    static constraints = {
        status inList: LiveSessionStatus.values()*.name()
        startDate nullable: true
        endDate nullable: true
    }

    /**
     * Flag to indicate if the live session is not started
     * @return true if the live session is not started,false otherwise
     */
    boolean isNotStarted() {
        status == LiveSessionStatus.NotStarted.name()
    }

    /**
     * Flag to indicate if the live session is started
     * @return true if the live session is started,false otherwise
     */
    boolean isStarted() {
        status == LiveSessionStatus.Started.name()
    }

    /**
     * Flag to indicate if the live session is stopped
     * @return true if the live session is stopped,false otherwise
     */
    boolean isStopped() {
        status == LiveSessionStatus.Ended.name()
    }

    /**
     * Start the current live session
     */
    @Requires({ isNotStarted() })
    def start() {
        status = LiveSessionStatus.Started.name()
        startDate = new Date()
        save(flush: true)
    }

    /**
     * Stop the current live session
     */
    @Requires( {isStarted()} )
    def stop() {
        status = LiveSessionStatus.Ended.name()
        endDate = new Date()
        save(flush: true)
    }

    /**
     * Get the response of the current live session for a given user
     * @param user the given user
     * @return the live session response if it exists
     */
    LiveSessionResponse getResponseForUser(User user) {
        LiveSessionResponse.findByLiveSessionAndUser(this,user)
    }

    /**
     * get the count of responses for the current live session
     * @return the count of responses
     */
    Integer responseCount() {
        LiveSessionResponse.countByLiveSession(this)
    }

    /**
     * Construct the result matrix of the current live session
     * @return the result matrix
     */
    List<Map<String,Float>> resultMatrix() {
        def responses = LiveSessionResponse.findAllByLiveSession(this)
        if (!responses) {
            return []
        }
        def matrix = []
        LiveSessionResponse firstResponse = responses[0]
        def matrixSize = firstResponse.userResponse.userAnswerBlockList.size()
        for(int i=0; i < matrixSize  ; i++) {
            matrix.add([:])
        }
        for(LiveSessionResponse response : responses) {
            for(int i=0; i < matrixSize ; i++) {
                def currentMap = matrix[i]
                def currentAnswerBlock = response.userResponse.userAnswerBlockList[i]
                for(Answer currentAnswer : currentAnswerBlock.answerList) {
                    if (currentMap[currentAnswer.textValue] == null) {
                        currentMap[currentAnswer.textValue] = 0
                    }
                    currentMap[currentAnswer.textValue] += 1
                }
            }
        }
        def responseCount = responses.size()
        for(int i=0; i < matrixSize ; i++) { // conversion in percent
            Map<String,Float> currentMap = matrix[i]
            for(String currentKey : currentMap.keySet()) {
               currentMap[currentKey] = (currentMap[currentKey] / responseCount ) * 100
            }
        }
        matrix
    }
}

enum LiveSessionStatus {
    NotStarted,
    Started,
    Ended
}