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

package org.tsaap.questions.impl;

import org.tsaap.questions.Question;
import org.tsaap.questions.UserAnswerBlock;
import org.tsaap.questions.UserResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultUserResponse implements UserResponse {

    private String userIdentifier;
    private Question question;
    private List<UserAnswerBlock> userAnswerBlockList = new ArrayList<UserAnswerBlock>();


    /**
     * Get the user identifier
     *
     * @return the user identifier
     */
    public String getUserIdentifier() {
        return userIdentifier;
    }

    /**
     * Get the question
     *
     * @return the question the response is for
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Get the user answer block list
     *
     * @return the user answer block list
     */
    public List<UserAnswerBlock> getUserAnswerBlockList() {
        return userAnswerBlockList;
    }


    /**
     * Set the user identifier
     *
     * @param userIdentifier the user identifier
     */
    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    /**
     * Set the question
     *
     * @param question the question
     */
    public void setQuestion(Question question) {
        this.question = question;
    }


    /**
     * Get the percent credit the user receive for his answers on the question
     *
     * @return
     */
    public Float evaluatePercentCredit() {
        Float res = 0f;
        for (UserAnswerBlock userAnswerBlock : userAnswerBlockList) {
            res += userAnswerBlock.evaluatePercentCredit();
        }
        return res;
    }
}
