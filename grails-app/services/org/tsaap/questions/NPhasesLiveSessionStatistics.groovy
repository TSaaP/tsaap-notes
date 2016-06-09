/*
 * Copyright 2015 Tsaap Development Group
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
