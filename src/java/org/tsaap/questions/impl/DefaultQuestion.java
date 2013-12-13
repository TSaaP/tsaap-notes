/*
 * Copyright 2013 Tsaap Development Group
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

package org.tsaap.questions.impl;

import org.tsaap.questions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author franck Silvestre
 */
public class DefaultQuestion implements Question {

    private String title;
    private List<QuestionBlock> blockList = new ArrayList<QuestionBlock>();
    private List<AnswerBlock> answerBlockList = new ArrayList<AnswerBlock>();
    private List<TextBlock> textBlockList = new ArrayList<TextBlock>();
    private QuestionType questionType = QuestionType.Undefined;
    private String globalId = UUID.randomUUID().toString();

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
     * @param questionType the question type
     */
    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    /**
     * Get the global Id of the question
     *
     * @return the global Id
     */
    public String getGlobalId() {
        return globalId;
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
    public void addAnswerFragment(AnswerBlock fragment) {
        blockList.add(fragment);
        answerBlockList.add(fragment);
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    public void addTextFragment(TextBlock fragment) {
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


}
