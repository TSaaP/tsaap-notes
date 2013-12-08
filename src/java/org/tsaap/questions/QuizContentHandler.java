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
     * Receive notification of the beginning of an answer fragment
     */
    public void onStartAnswerFragment();

    /**
     * Receive notification of the end of an answer fragment
     */
    public void onEndAnswerFragment();

    /**
     * Receive notification of the beginning of an answer
     */
    public void onStartAnswer();


    /**
     * Receive notification of the end of an answer
     */
    public void onEndAnswer();

}
