package org.tsaap.questions

import org.tsaap.questions.impl.gift.GiftQuestionService

class ResultMatrixService {

    static transactional = false

    /**
     * Construct the result matrix of the current live session
     * @return the result matrix
     */
    List<Map<String, Float>> buildResultMatrixForQuestionAndResponses(Question question, List<LiveSessionResponse> responses) {
        def matrix = []
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
