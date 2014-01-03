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
     * @param answer the answer to add
     */
    public void addAnswer(Answer answer) {
        answerList.add(answer);
    }
}
