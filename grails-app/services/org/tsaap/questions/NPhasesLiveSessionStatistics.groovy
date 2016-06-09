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

import groovy.transform.ToString


/**
 * Class for encapsulation of live session statistics.
 */
@ToString(includeNames = true)
class NPhasesLiveSessionStatistics extends NPhasesLiveSessionData {

    Integer numberOfPresents

    Integer numberOfAnswersOnPhase1
    Integer numberOfGoodAnswersOnPhase1
    Integer numberOfBadAnswersOnPhase1

    Integer numberOfAnswersOnPhase2
    Integer numberOfGoodAnswersOnPhase2
    Integer numberOfBadAnswersOnPhase2

    Integer numberOfExplanationsOnPhase1
    Integer numberOfGoodExplanationsOnPhase1
    Integer numberOfBadExplanationsOnPhase1

    Integer numberOfExplanationsOnPhase2
    Integer numberOfGoodExplanationsOnPhase2
    Integer numberOfBadExplanationsOnPhase2

    Integer numberOfUserHavingGivenAnEvaluation
    Double meanOfEvaluationPerExplanations
    Integer numberOfEvaluatedExplanations
    Double meanOfExplanationPerEvaluator
    Double meanOfStandardDeviationOnEvalutations
}
