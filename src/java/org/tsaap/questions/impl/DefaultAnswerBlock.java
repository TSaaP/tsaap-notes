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

import org.tsaap.questions.Answer;
import org.tsaap.questions.AnswerBlock;

import java.util.ArrayList;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultAnswerBlock implements AnswerBlock {

    private List<Answer> answerList = new ArrayList<Answer>();

    /**
     * Get the answer list that compose the answer block
     *
     * @return the answer list of the block
     */
    public List<Answer> getAnswerList() {
        return answerList;
    }

    /**
     * Add answer to the answer block
     *
     * @param answer the answer to add
     */
    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }
}
