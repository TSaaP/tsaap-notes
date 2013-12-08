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

import org.tsaap.questions.Question;
import org.tsaap.questions.QuestionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class DefaultQuestion implements Question {

    private String title;
    private List<QuestionFragment> fragmentList = new ArrayList<QuestionFragment>();

    /**
     * Get the title of the question
     *
     * @return the title of the question
     */
    public String getTitle() {
        return null;
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
     * Add a fragment of the question to the question
     * @param fragment the fragment to add
     */
    public void addFragment(QuestionFragment fragment) {
        fragmentList.add(fragment);
    }
}
