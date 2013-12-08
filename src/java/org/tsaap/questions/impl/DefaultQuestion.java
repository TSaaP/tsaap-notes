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

import org.tsaap.questions.AnswerFragment;
import org.tsaap.questions.Question;
import org.tsaap.questions.QuestionFragment;
import org.tsaap.questions.TextFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultQuestion implements Question {

    private String title;
    private List<QuestionFragment> fragmentList = new ArrayList<QuestionFragment>();
    private List<AnswerFragment> answerFragmentList = new ArrayList<AnswerFragment>();


    private List<TextFragment> textFragmentList = new ArrayList<TextFragment>();

    /**
     * Get the title of the question
     *
     * @return the title of the question
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the question fragment list
     *
     * @return the question fragment list
     */
    public List<QuestionFragment> getFragmentList() {
        return fragmentList;
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    public void addAnswerFragment(AnswerFragment fragment) {
        fragmentList.add(fragment);
        answerFragmentList.add(fragment);
    }

    /**
     * Add an answer fragment of the question to the question
     *
     * @param fragment the fragment to add
     */
    public void addTextFragment(TextFragment fragment) {
        fragmentList.add(fragment);
        textFragmentList.add(fragment);
    }

    /**
     * Get answer fragment list
     *
     * @return the answer fragment list
     */
    public List<AnswerFragment> getAnswerFragmentList() {
        return answerFragmentList;
    }

    /**
     * Get the text fragment list
     *
     * @return the text fragment list
     */
    public List<TextFragment> getTextFragmentList() {
        return textFragmentList;
    }


}
