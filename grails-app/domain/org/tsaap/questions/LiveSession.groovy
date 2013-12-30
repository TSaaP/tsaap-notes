package org.tsaap.questions

import org.gcontracts.annotations.Requires
import org.tsaap.directory.User
import org.tsaap.notes.Note
import org.tsaap.questions.impl.gift.GiftQuestionService

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
    @Requires({ isStarted() })
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
        LiveSessionResponse.findByLiveSessionAndUser(this, user)
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
    List<Map<String, Float>> resultMatrix() {
        def responses = LiveSessionResponse.findAllByLiveSession(this)
        def matrix = []
        Question question = this.note.question
        def matrixSize = question.answerBlockList.size()
        for (int i = 0; i < matrixSize; i++) { // initialize the matrix from the question spec
            matrix[i] = [:]
            matrix[i][GiftQuestionService.NO_RESPONSE] = 0
            AnswerBlock answerBlock = question.answerBlockList[i]
            for (Answer answer : answerBlock.answerList) {
                matrix[i][answer.textValue] = 0
            }
        }
        def responseCount = responses.size()
        if (responseCount > 0) {
            for (LiveSessionResponse response : responses) {
                    for (int i = 0; i < matrixSize; i++) {
                        def currentMap = matrix[i]
                        def currentAnswerBlock = response.userResponse.userAnswerBlockList[i]
                        for (Answer currentAnswer : currentAnswerBlock.answerList) {
                            currentMap[currentAnswer.textValue] += 1
                        }
                    }
            }

            for (int i = 0; i < matrixSize; i++) { // conversion in percent
                Map<String, Float> currentMap = matrix[i]
                for (String currentKey : currentMap.keySet()) {
                    currentMap[currentKey] = (currentMap[currentKey] / responseCount) * 100
                }
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