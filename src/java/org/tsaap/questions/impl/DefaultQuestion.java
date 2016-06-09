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

import org.tsaap.questions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultQuestion implements Question {

    private String title;
    private List<QuestionBlock> blockList = new ArrayList<QuestionBlock>();
    private List<AnswerBlock> answerBlockList = new ArrayList<AnswerBlock>();
    private List<TextBlock> textBlockList = new ArrayList<TextBlock>();
    private QuestionType questionType = QuestionType.Undefined;
    private String generalFeedback;

    /**
     * Get the title of the question
     *
     * @return the title of the question
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the question type
     *
     * @return the question type
     */
    public QuestionType getQuestionType() {
        return questionType;
    }

    /**
     * Set the question type
     *
     * @param questionType the question type
     */
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the question fragment list
     *
     * @return the question fragment list
     */
    public List<QuestionBlock> getBlockList() {
        return blockList;
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    public void addAnswerBlock(AnswerBlock fragment) {
        blockList.add(fragment);
        answerBlockList.add(fragment);
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    public void addTextBlock(TextBlock fragment) {
        blockList.add(fragment);
        textBlockList.add(fragment);
    }

    /**
     * Get answer fragment list
     *
     * @return the answer fragment list
     */
    public List<AnswerBlock> getAnswerBlockList() {
        return answerBlockList;
    }

    /**
     * Get the text fragment list
     *
     * @return the text fragment list
     */
    public List<TextBlock> getTextBlockList() {
        return textBlockList;
    }

    /**
     * Get the general feedback
     *
     * @return the general feedback
     */
    public String getGeneralFeedback() {
        return generalFeedback;
    }

    /**
     * Set the general feedback
     *
     * @param generalFeedback
     */
    public void setGeneralFeedback(String generalFeedback) {
        this.generalFeedback = generalFeedback;
    }

}
