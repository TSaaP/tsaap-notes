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

import org.apache.log4j.Logger;
import org.tsaap.questions.Question;
import org.tsaap.questions.QuestionType;
import org.tsaap.questions.QuizContentHandler;
import org.tsaap.questions.TextBlock;
import org.tsaap.questions.impl.*;

/**
 * @author franck Silvestre
 */
public class GiftQuizContentHandler implements QuizContentHandler {


    /**
     * Get the quiz
     *
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
    public void onEndQuiz() {
    }

    /**
     * Receive notification of the beginning of a question
     */
    public void onStartQuestion() {
        currentQuestion = new DefaultQuestion();
        currentQuestion.setQuestionType(QuestionType.ExclusiveChoice);
    }

    /**
     * Receive notification of the end of a question
     */
    public void onEndQuestion() {
        postProcess(currentQuestion);
        quiz.addQuestion(currentQuestion);
        currentQuestion = null;
    }


    /**
     * Receive notification of the beginning of a title
     */
    public void onStartTitle() {
        currentTitle = new StringBuffer();
    }

    /**
     * Receive notification of the end of a title
     */
    public void onEndTitle() {
        currentQuestion.setTitle(currentTitle.toString());
        currentTitle = null;
    }

    /**
     * Receive notification of the beginning of an answer fragment
     */
    public void onStartAnswerBlock() {
        currentAnswerFragment = new DefaultAnswerBlock();
        answerCounter = 1;
    }

    /**
     * Receive notification of the end of an answer fragment
     */
    public void onEndAnswerBlock() {
        currentQuestion.addAnswerFragment(currentAnswerFragment);
        currentAnswerFragment = null;
    }

    /**
     * Receive notification of the beginning of an answer
     */
    public void onStartAnswer(String prefix) {
        currentAnswer = new DefaultAnswer();
        currentAnswer.setIdentifier(String.valueOf(answerCounter++));
        if ("=".equals(prefix)) {
            currentAnswer.setPercentCredit(100f);
        } else {
            currentAnswer.setPercentCredit(0f);
        }
    }

    /**
     * Receive notification of the end of an answer
     */
    public void onEndAnswer() {
        currentAnswerFragment.addAnswer(currentAnswer);
        currentAnswer = null;
    }

    /**
     * Notification of the beginning of a credit specification
     */
    public void onStartAnswerCredit() {
        answerCreditIsBeenBuilt = true;
    }

    /**
     * Notification of the end of a credit specification
     */
    public void onEndAnswerCredit() {
        answerCreditIsBeenBuilt = false;
    }

    /**
     * Receive notification of the beginning feedback
     */
    public void onStartAnswerFeedBack() {
        feedbackIsBeenBuilt = true;
    }

    /**
     * Receive notification of the end of a feedback
     */
    public void onEndAnswerFeedBack() {
        feedbackIsBeenBuilt = false;
    }

    /**
     * Receive notification of a new string
     *
     * @param str the received string
     */
    public void onString(final String str) {
        String trimedStr = str.trim();
        if (currentTitle != null) {
            currentTitle.append(trimedStr);
            logger.debug("currentTitle | " + currentTitle.toString());
        } else if (answerCreditIsBeenBuilt) {
            currentAnswer.setPercentCredit(new Float(trimedStr));
        } else if (feedbackIsBeenBuilt) {
            currentAnswer.setFeedback(trimedStr);
        } else if (currentAnswer != null) {
            currentAnswer.setTextValue(trimedStr);
        } else if (currentQuestion != null && currentAnswerFragment == null) {
            logger.debug("Text fragment | " + str);
            currentQuestion.addTextFragment(new TextBlock() {
                public String getText() {
                    return str;
                }
            });
        }
    }


    private void postProcess(Question question) {
       logger.error("Post processing of the current question");
       if (currentQuestion.getQuestionType() == QuestionType.ExclusiveChoice) {
           currentQuestion = new DefaultExclusiveChoiceQuestion(currentQuestion);
       }
    }

    private static Logger logger = Logger.getLogger(GiftQuizContentHandler.class);

    private DefaultQuiz quiz;
    private DefaultQuestion currentQuestion;
    private DefaultAnswerBlock currentAnswerFragment;
    private DefaultAnswer currentAnswer;
    private StringBuffer currentTitle;
    private boolean answerCreditIsBeenBuilt;
    private boolean feedbackIsBeenBuilt;
    private int answerCounter ;
}
