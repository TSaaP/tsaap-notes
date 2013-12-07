package org.tsaap.questions;

/**
 * @author franck Silvestre
 */
public interface GiftContentHandler {

    public void onString(String str);

    public void onTitle(String title);

    public void onStartAnswerSet();

    public void onEndAnswerSet();

    public void onStartAnswer();

    public void onEndAnswer();

    public void onEscapeCharacter(int escapeCharacter);

}
