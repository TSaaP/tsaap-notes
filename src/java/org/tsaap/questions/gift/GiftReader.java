package org.tsaap.questions.gift;

import org.tsaap.questions.QuizContentHandler;
import org.tsaap.questions.QuizReader;
import org.tsaap.questions.QuizReaderException;

import java.io.IOException;
import java.io.Reader;

/**
 * @author franck Silvestre
 */
public class GiftReader implements QuizReader {

    public void parse(Reader reader) throws IOException, GiftReaderException {
        int currentChar;
        quizContentHandler.onStartQuiz();
        while ((currentChar = reader.read()) != 1) {
            checkQuestionHasStarted();
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
        checkQuestionHasEnded();
        quizContentHandler.onEndQuiz();

    }

    private void checkQuestionHasStarted() {
        if (!questionHasStarted) {
            questionHasStarted = true;
            quizContentHandler.onStartQuestion();
        }
    }

    private void checkQuestionHasEnded() throws GiftReaderException {
            if (!questionHasEnded) {
                throw new GiftReaderException("End of file but question is not ended.");
            }
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
                quizContentHandler.onEndTitle();
            } else {
                titleHasStarted = true;
                quizContentHandler.onStartTitle();
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
        flushAccumulator();
        quizContentHandler.onStartAnswerSet();

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
        flushAccumulator();
        quizContentHandler.onEndAnswerSet();

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
        quizContentHandler.onString(accumulator.toString());
        accumulator = null;
    }

    public QuizContentHandler getQuizContentHandler() {
        return quizContentHandler;
    }

    public void setQuizContentHandler(QuizContentHandler quizContentHandler) {
        this.quizContentHandler = quizContentHandler;
    }

    private QuizContentHandler quizContentHandler;
    private StringBuffer accumulator;
    private int controlCharAccumulator;
    private boolean escapeMode;

    private boolean titleHasStarted;
    private boolean titleHasEnded;
    private boolean answerSetHasStarted;
    private boolean answerSetHasEnded;
    private boolean questionHasStarted;
    private boolean questionHasEnded;
}
