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
