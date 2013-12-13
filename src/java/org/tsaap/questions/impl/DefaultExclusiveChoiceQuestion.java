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

import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultExclusiveChoiceQuestion extends DefaultQuestion {

    private DefaultQuestion defaultQuestion;


    public DefaultExclusiveChoiceQuestion(DefaultQuestion defaultQuestion) {
        this.defaultQuestion = defaultQuestion;
    }

    /**
     * @return the unique answer block of the EC question
     */
    public AnswerBlock getAnswerBlock() {
        return getAnswerBlockList().get(0);
    }

    /**
     * Return the good answer for the question
     * @return the good answer
     */
    public Answer getGoodAnswer() {
        for(Answer answer : getAnswerBlock().getAnswerList()) {
            if (answer.getPercentCredit() == 100) {
                return answer;
            }
        }
        return null;
    }

    /**
     * Get the title of the question
     *
     * @return the title of the question
     */
    @Override
    public String getTitle() {
        return defaultQuestion.getTitle();
    }

    /**
     * Get the question type
     *
     * @return the question type
     */
    @Override
    public QuestionType getQuestionType() {
        return defaultQuestion.getQuestionType();
    }

    /**
     * Set the question type
     * @param questionType the question type
     */
    @Override
    public void setQuestionType(QuestionType questionType) {
        defaultQuestion.setQuestionType(questionType);
    }

    /**
     * Get the global Id of the question
     *
     * @return the global Id
     */
    @Override
    public String getGlobalId() {
        return defaultQuestion.getGlobalId();
    }

    @Override
    public void setTitle(String title) {
        defaultQuestion.setTitle(title);
    }

    /**
     * Get the question fragment list
     *
     * @return the question fragment list
     */
    @Override
    public List<QuestionBlock> getBlockList() {
        return defaultQuestion.getBlockList();
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    @Override
    public void addAnswerBlock(AnswerBlock fragment) {
        defaultQuestion.addAnswerBlock(fragment);
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    @Override
    public void addTextBlock(TextBlock fragment) {
        defaultQuestion.addTextBlock(fragment);
    }

    /**
     * Get answer fragment list
     *
     * @return the answer fragment list
     */
    @Override
    public List<AnswerBlock> getAnswerBlockList() {
        return defaultQuestion.getAnswerBlockList();
    }

    /**
     * Get the text fragment list
     *
     * @return the text fragment list
     */
    @Override
    public List<TextBlock> getTextBlockList() {
        return defaultQuestion.getTextBlockList();
    }
}
