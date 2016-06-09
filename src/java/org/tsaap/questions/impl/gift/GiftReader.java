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

package org.tsaap.questions.impl.gift;

import org.apache.log4j.Logger;
import org.tsaap.questions.QuizContentHandler;
import org.tsaap.questions.QuizReader;

import java.io.IOException;
import java.io.Reader;

/**
 * @author franck Silvestre
 */
public class GiftReader implements QuizReader {

    private static Logger logger = Logger.getLogger(GiftReader.class);

    public void parse(Reader reader) throws IOException, GiftReaderException {
        int currentChar;
        quizContentHandler.onStartQuiz();
        while ((currentChar = reader.read()) != -1) {
            checkQuestionHasStarted();
            if (currentChar == ':') {
                processColonCharacter();
            } else if (currentChar == '\\') {
                processAntiSlashCharacter();
            } else if (currentChar == '{') {
                processLeftBracketCharacter();
            } else if (currentChar == '}') {
                processRightBracketCharacter();
            } else if (currentChar == '=') {
                processEqualCharacter();
            } else if (currentChar == '~') {
                processTildeCharacter();
            } else if (currentChar == '#') {
                processSharpCharacter();
            } else if (currentChar == '%') {
                processPercentCharacter();
            } else {
                processAnyCharacter(currentChar);
            }
            logger.debug("Current char  | " + (char) currentChar);
            if (accumulator != null) {
                logger.debug("Accumulator | " + accumulator.toString());
            }
            logger.debug("control caracter accumulator | " + (char) controlCharAccumulator);
        }
        endQuiz();
        quizContentHandler.onEndQuiz();

    }

    private void checkQuestionHasStarted() {
        if (!questionHasStarted) {
            questionHasStarted = true;
            quizContentHandler.onStartQuestion();
        }
    }

    private void endQuiz() throws GiftReaderQuestionWithInvalidFormatException {
        if (!questionHasEnded && !answerFragmentHasEnded) {
            throw new GiftReaderQuestionWithInvalidFormatException();
        }
        if (!questionHasEnded) {
            flushAccumulator();
            questionHasEnded = true;
            quizContentHandler.onEndQuestion();
        }

    }

    private void processColonCharacter() throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter(':');
            return;
        }
        if (titleHasEnded) {
            throw new GiftReaderNotEscapedCharacterException();
        }
        if (controlCharAccumulator == -1) {
            flushAccumulator();
            controlCharAccumulator = ':';
            return;
        }
        if (controlCharAccumulator == ':') {
            if (titleHasStarted) {
                titleHasEnded = true;
                quizContentHandler.onEndTitle();
            } else {
                titleHasStarted = true;
                quizContentHandler.onStartTitle();
            }
            controlCharAccumulator = -1;
        }

    }

    private void processAntiSlashCharacter() throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter('\\');
            return;
        }
        escapeMode = true;
    }

    private void processLeftBracketCharacter() throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter('{');
            return;
        }
        if (answerFragmentHasStarted) {
            throw new GiftReaderNotEscapedCharacterException();
        }
        flushAccumulator();
        answerFragmentHasStarted = true;
        answerFragmentHasEnded = false;
        quizContentHandler.onStartAnswerBlock();

    }

    private void processRightBracketCharacter() throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter('}');
            return;
        }
        if (!answerFragmentHasStarted) {
            throw new GiftReaderNotEscapedCharacterException();
        }
        flushAccumulator();
        answerFragmentHasEnded = true;
        answerFragmentHasStarted = false;
        if (answerHasStarted) {
            answerHasStarted = false;
            quizContentHandler.onEndAnswer();
        }
        quizContentHandler.onEndAnswerBlock();

    }

    private void processEqualCharacter() throws GiftReaderException {
        processAnswerPrefix('=');
    }

    private void processTildeCharacter() throws GiftReaderException {
        processAnswerPrefix('~');
    }

    private void processAnswerPrefix(char prefix) throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter(prefix);
            return;
        }
        if (!answerFragmentHasStarted) {
            throw new GiftReaderNotEscapedCharacterException();
        }
        flushAccumulator();
        if (answerFeedbackHasStarted) {
            answerFeedbackHasStarted = false;
            getQuizContentHandler().onEndAnswerFeedBack();
        }
        if (answerHasStarted) { // the '=' or '~' char marks the end of the current answer
            getQuizContentHandler().onEndAnswer();
        } else {
            answerHasStarted = true;
        }
        answerCreditHasStarted = false;
        answerCreditHasEnded = false;
        getQuizContentHandler().onStartAnswer(String.valueOf(prefix)); // it marks the beginning of a new one too
    }

    private void processSharpCharacter() throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter('#');
            return;
        }
        if (!answerHasStarted || answerFeedbackHasStarted) {
            throw new GiftReaderNotEscapedCharacterException();
        }
        flushAccumulator();
        answerFeedbackHasStarted = true;
        getQuizContentHandler().onStartAnswerFeedBack(); // it marks the beginning of a new one too
    }

    private void processPercentCharacter() throws GiftReaderNotEscapedCharacterException {
        if (escapeMode) {
            processAnyCharacter('%');
            return;
        }
        if (!answerHasStarted || answerCreditHasEnded) {
            throw new GiftReaderNotEscapedCharacterException();
        }
        flushAccumulator();
        if (answerCreditHasStarted) {
            answerCreditHasStarted = false;
            answerCreditHasEnded = true;
            getQuizContentHandler().onEndAnswerCredit();
        } else {
            answerCreditHasStarted = true;
            getQuizContentHandler().onStartAnswerCredit();
        }

    }

    private void processAnyCharacter(int currentChar) throws GiftReaderNotEscapedCharacterException {
        if (accumulator == null) {
            accumulator = new StringBuffer();
        }
        accumulator.append((char) currentChar);
        if (controlCharAccumulator != -1) { // if a control caracter is present,
            if (controlCharAccumulator != '\\') {  // it must be a \
                throw new GiftReaderNotEscapedCharacterException();
            }
            controlCharAccumulator = -1;
        }
        escapeMode = false;
    }

    private void flushAccumulator() {
        if (accumulator != null) {
            quizContentHandler.onString(accumulator.toString());
            accumulator = null;
        }
    }

    public QuizContentHandler getQuizContentHandler() {
        return quizContentHandler;
    }

    public void setQuizContentHandler(QuizContentHandler quizContentHandler) {
        this.quizContentHandler = quizContentHandler;
    }

    private QuizContentHandler quizContentHandler;
    private StringBuffer accumulator;
    private int controlCharAccumulator = -1;
    private boolean escapeMode;

    private boolean questionHasStarted;
    private boolean questionHasEnded;
    private boolean titleHasStarted;
    private boolean titleHasEnded;
    private boolean answerFragmentHasStarted;
    private boolean answerFragmentHasEnded;
    private boolean answerHasStarted;
    private boolean answerFeedbackHasStarted;
    private boolean answerCreditHasStarted;
    private boolean answerCreditHasEnded;


}
