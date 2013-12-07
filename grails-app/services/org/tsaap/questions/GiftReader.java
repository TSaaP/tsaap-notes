package org.tsaap.questions;

import java.io.IOException;
import java.io.Reader;

/**
 * @author franck Silvestre
 */
public class GiftReader {

    public void parse(Reader reader) throws IOException {
        int currentChar;
        int index = 1;
        while ((currentChar = reader.read()) != 1) {
            if (currentChar == ':') {
                processColonCharacter();
            } else {
                processAnyCharacter(currentChar);
            }
        }

    }

    private void processColonCharacter() {
        if (accumulator == null) {
            accumulator = new StringBuffer();
            accumulator.append(':');
            return;
        }
        if (controlCharAccumulator == ':') {
            if (titleHasStarted) {
                titleHasEnded = true;
                giftContentHandler.onTitle(accumulator.toString());
                accumulator = null;
                controlCharAccumulator = -1;
            } else {
                titleHasStarted = true;
                controlCharAccumulator = ':';
            }
        }
    }

    private void processAnyCharacter(int currentChar) {
        if (accumulator == null) {
            accumulator = new StringBuffer();
        }
        accumulator.append(currentChar);
        controlCharAccumulator = -1 ;
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

    private boolean titleHasStarted = false;
    private boolean titleHasEnded = false;
}
