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
import org.tsaap.questions.Quiz;

import java.util.ArrayList;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultQuiz implements Quiz {

    private List<Question> questionList = new ArrayList<Question>();


    /**
     * Add a question to the quiz
     *
     * @param question the question to add
     */
    public void addQuestion(Question question) {
        questionList.add(question);
    }

    /**
     * Get the question list of the quiz
     *
     * @return the question list
     */
    public List<Question> getQuestionList() {
        return questionList;
    }
}
