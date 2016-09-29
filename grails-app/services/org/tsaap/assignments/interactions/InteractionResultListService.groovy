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

package org.tsaap.assignments.interactions

import org.tsaap.assignments.ChoiceInteractionResponse
import org.tsaap.assignments.Interaction
import org.tsaap.contracts.Contract


class InteractionResultListService {

    static transactional = false

    /**
     * Construct the result matrix of an interaction
     * @return the result matrix
     */
    List<Float> buildResultListForInteractionAndResponses(Interaction interaction, List<ChoiceInteractionResponse> responses) {
        Contract.requires(interaction.isResponseSubmission(), INTERACTION_IS_NOT_RESPONSE_SUBMISSION)
        def matrix = []
        ResponseSubmissionSpecification spec = interaction.interactionSpecification
        def matrixSize = spec.itemCount + 1
        for (int i = 0; i < matrixSize; i++) { // initialize the matrix from the question spec
            matrix[i] = 0
        }
        def responseCount = responses.size()
        if (responseCount > 0) {
            for (ChoiceInteractionResponse response : responses) {
                def currentChoices = response.choiceList()
                if (!currentChoices) { // no response count stored in matrix[0]
                    matrix[0] += 1
                }
                for (int i = 0; i < currentChoices.size(); i++) {
                    matrix[currentChoices[i]] += 1
                }
            }
            for (int i = 0; i < matrixSize; i++) { // conversion in percent
                matrix[i] = ((matrix[i] / responseCount) * 100).setScale(3, BigDecimal.ROUND_HALF_UP)
            }
        }
        matrix
    }

    public static final String INTERACTION_IS_NOT_RESPONSE_SUBMISSION = "Interaction is not a response submission interaction"
}
