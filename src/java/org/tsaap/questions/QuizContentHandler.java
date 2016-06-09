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

package org.tsaap.questions;

/**
 * @author franck Silvestre
 */
public interface QuizContentHandler {

    /**
     * Receive notification of the beginning of a quiz
     */
    public void onStartQuiz();

    /**
     * Receive notification of the end of a quiz
     */
    public void onEndQuiz();

    /**
     * Receive notification of the beginning of a question
     */
    public void onStartQuestion();

    /**
     * Receive notification of the end of a question
     */
    public void onEndQuestion();


    /**
     * Receive notification of a new string
     */
    public void onString(String str);

    /**
     * Receive notification of the beginning of a title
     */
    public void onStartTitle();

    /**
     * Receive notification of the end of a title
     */
    public void onEndTitle();

    /**
     * Receive notification of the beginning of an answer block
     */
    public void onStartAnswerBlock();

    /**
     * Receive notification of the end of an answer block
     */
    public void onEndAnswerBlock();

    /**
     * Receive notification of the beginning of an answer
     */
    public void onStartAnswer(String prefix);


    /**
     * Receive notification of the end of an answer
     */
    public void onEndAnswer();

    /**
     * Notification of the beginning of a credit specification
     */
    public void onStartAnswerCredit();


    /**
     * Notification of the end of a credit specification
     */
    public void onEndAnswerCredit();


    /**
     * Receive notification of the beginning feedback
     */
    public void onStartAnswerFeedBack();


    /**
     * Receive notification of the end of a feedback
     */
    public void onEndAnswerFeedBack();

}
