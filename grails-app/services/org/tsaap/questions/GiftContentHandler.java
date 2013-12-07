package org.tsaap.questions;

/**
 * @author franck Silvestre
 */
public interface GiftContentHandler {

    public void onStartQuestion();

    public void onEndQuestion();

    public void onString(String str);

    public void onStartTitle();

    public void onEndTitle();

    public void onStartAnswerSet();

    public void onEndAnswerSet();

    public void onStartAnswer();

    public void onEndAnswer();

    public void onEscapeCharacter(int escapeCharacter);



}
