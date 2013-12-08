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

package org.tsaap.questions.impl.gift;

import org.tsaap.questions.QuizContentHandler;
import org.tsaap.questions.impl.DefaultAnswerFragment;
import org.tsaap.questions.impl.DefaultQuestion;
import org.tsaap.questions.impl.DefaultQuiz;

/**
 * @author franck Silvestre
 */
public class GiftQuizContentHandler implements QuizContentHandler {

    private DefaultQuiz quiz;
    private DefaultQuestion currentQuestion;
    private DefaultAnswerFragment currentAnswerFragment;

    /**
     * Get the quiz
     * @return the quiz
     */
    public DefaultQuiz getQuiz() {
        return quiz;
    }



    /**
     * Receive notification of the beginning of a quiz
     */
    public void onStartQuiz() {
       quiz = new DefaultQuiz();
    }

    /**
     * Receive notification of the end of a quiz
     */
    public void onEndQuiz() {}

    /**
     * Receive notification of the beginning of a question
     */
    public void onStartQuestion() {
       currentQuestion = new DefaultQuestion();
    }

    /**
     * Receive notification of the end of a question
     */
    public void onEndQuestion() {
        quiz.addQuestion(currentQuestion);
        currentQuestion = null;
    }

    /**
     * Receive notification of a new string
     *
     * @param str the received string
     */
    public void onString(String str) {

    }

    /**
     * Receive notification of the beginning of a title
     */
    public void onStartTitle() {

    }

    /**
     * Receive notification of the end of a title
     */
    public void onEndTitle() {

    }

    /**
     * Receive notification of the beginning of an answer fragment
     */
    public void onStartAnswerFragment() {

    }

    /**
     * Receive notification of the end of an answer fragment
     */
    public void onEndAnswerFragment() {

    }

    /**
     * Receive notification of the beginning of an answer
     */
    public void onStartAnswer() {

    }

    /**
     * Receive notification of the end of an answer
     */
    public void onEndAnswer() {

    }
}
