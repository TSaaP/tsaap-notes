package org.tsaap.questions;

import java.io.IOException;
import java.io.Reader;

/**
 * @author franck Silvestre
 */
public class GiftReader {

    public void parse(Reader reader) throws IOException, GiftReaderException {
        int currentChar;
        giftContentHandler.onStartQuestion();
        while ((currentChar = reader.read()) != 1) {
            if (currentChar == ':') {
                processColonCharacter();
            } else if (currentChar == '\\') {
                processAntiSlashCharacter();
            } else if (currentChar == '{') {
                processLeftBracketCharacter();
            } else if (currentChar == '}') {
                processRightBracketCharacter();
            } else {
                processAnyCharacter(currentChar);
            }
        }
        giftContentHandler.onEndQuestion();

    }

    private void processColonCharacter() throws GiftReaderException {
        if (escapeMode) {
            processAnyCharacter(':');
            return;
        }
        if (titleHasEnded) {
            throw new GiftReaderException("You must escape the ':' putting an '\\' before.");
        }
        if (controlCharAccumulator == -1) {
            controlCharAccumulator = ':';
            return;
        }
        if (controlCharAccumulator == ':') {
            if (titleHasStarted) {
                titleHasEnded = true;
                giftContentHandler.onEndTitle();
            } else {
                titleHasStarted = true;
                giftContentHandler.onStartTitle();
            }
            controlCharAccumulator = -1;
            flushAccumulator();
        }

    }

    private void processAntiSlashCharacter() {
        if (escapeMode) {
            processAnyCharacter('\\');
            return;
        }
        escapeMode = true;
    }

    private void processLeftBracketCharacter() throws GiftReaderException {
        if (escapeMode) {
            processAnyCharacter('{');
            return;
        }
        if (answerSetHasStarted) {
            throw new GiftReaderException("You must escape the '{' putting an '\\' before.");
        }
        answerSetHasStarted = true;
        answerSetHasEnded = false;
        giftContentHandler.onStartAnswerSet();
    }

    private void processRightBracketCharacter() throws GiftReaderException {
        if (escapeMode) {
            processAnyCharacter('}');
            return;
        }
        if (!answerSetHasStarted) {
            throw new GiftReaderException("You must escape the '}' putting an '\\' before.");
        }
        answerSetHasEnded = true;
        answerSetHasStarted = false;
        giftContentHandler.onStartAnswerSet();
    }

    private void processAnyCharacter(int currentChar) {
        if (accumulator == null) {
            accumulator = new StringBuffer();
        }
        accumulator.append(currentChar);
        controlCharAccumulator = -1;
        escapeMode = false;
    }

    private void flushAccumulator() {
        giftContentHandler.onString(accumulator.toString());
        accumulator = null;
    }

    public GiftContentHandler getGiftContentHandler() {
        return giftContentHandler;
    }

    public void setGiftContentHandler(GiftContentHandler giftContentHandler) {
        this.giftContentHandler = giftContentHandler;
    }

    private GiftContentHandler giftContentHandler;
    private StringBuffer accumulator;
    private int controlCharAccumulator;
    private boolean escapeMode;

    private boolean titleHasStarted;
    private boolean titleHasEnded;
    private boolean answerSetHasStarted;
    private boolean answerSetHasEnded;
}
