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

import org.tsaap.directory.User
import org.tsaap.notes.Note
import org.tsaap.questions.impl.gift.GiftQuestionService

import java.util.regex.Matcher
import java.util.regex.Pattern

class LiveSessionResponse {

    LiveSession liveSession
    User user
    String answerListAsString
    Float percentCredit = null
    Integer confidenceDegree

    SessionPhase sessionPhase
    Note explanation

    static constraints = {
        answerListAsString nullable: true, blank: false, matches: '\\[,*(,?\\[".*"(,".*")*\\]|,?\\[\\])*,*\\]'
        percentCredit nullable: true
        confidenceDegree nullable: true, min: 0, max: 5
        sessionPhase nullable: true
        explanation nullable: true
    }

    GiftQuestionService giftQuestionService
    UserResponse userResponse

    /**
     * Get the user response corresponding to this live session response
     * and update the percent credit of the current live session Response
     * @return the user response
     */
    UserResponse getUserResponse() {
        if (userResponse == null) {
            def answerBlockTextList
            def question = liveSession.note.question
            try {
                // convert the answer list as string in list of list of string
                GroovyShell gs = new GroovyShell()
                answerBlockTextList = gs.evaluate(answerListAsString)
            } catch (Exception e) {
                log.error(e.message)
                answerBlockTextList = []
                for (int i = 0; i < question.answerBlockList.size(); i++) {
                    answerBlockTextList << []
                }
            }
            userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList(user.username, question, answerBlockTextList)
            if (percentCredit == null) {
                percentCredit = userResponse.evaluatePercentCredit()
            }
        }
        userResponse
    }

    String prettyAnswers() {
        def answers = answerListAsString.replaceAll("[\\[\\]\\\"]", "")

        Pattern digitPattern = Pattern.compile("(\\d+)");

        Matcher matcher = digitPattern.matcher(answers);
        StringBuffer result = new StringBuffer();
        while (matcher.find())
        {
            matcher.appendReplacement(result, String.valueOf(Integer.parseInt(matcher.group(1)) + 1));
        }
        matcher.appendTail(result);

        return result.toString();
    }


    static transients = ['giftQuestionService', 'userResponse']

    static mapping = {
        explanation fetch: 'join'
    }
}
