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
            } else {
                processAnyCharacter(currentChar);
            }
            logger.debug("Current char  | "+(char)currentChar);
            if (accumulator != null) {
                logger.debug("Accumulator | " +accumulator.toString() );
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

    private void endQuiz() throws GiftReaderException {
        if (!questionHasEnded && !answerFragmentHasEnded) {
            throw new GiftReaderException("End of file but question is not ended.");
        }
        if (!questionHasEnded) {
            flushAccumulator();
            questionHasEnded = true;
            quizContentHandler.onEndQuestion();
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
        if (answerFragmentHasStarted) {
            throw new GiftReaderException("You must escape the '{' putting an '\\' before.");
        }
        flushAccumulator();
        answerFragmentHasStarted = true;
        answerFragmentHasEnded = false;
        quizContentHandler.onStartAnswerFragment();

    }

    private void processRightBracketCharacter() throws GiftReaderException {
        if (escapeMode) {
            processAnyCharacter('}');
            return;
        }
        if (!answerFragmentHasStarted) {
            throw new GiftReaderException("You must escape the '}' putting an '\\' before.");
        }
        flushAccumulator();
        answerFragmentHasEnded = true;
        answerFragmentHasStarted = false;
        quizContentHandler.onEndAnswerFragment();

    }

    private void processAnyCharacter(int currentChar) {
        if (accumulator == null) {
            accumulator = new StringBuffer();
        }
        accumulator.append((char)currentChar);
        controlCharAccumulator = -1;
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

    private boolean titleHasStarted;
    private boolean titleHasEnded;
    private boolean answerFragmentHasStarted;
    private boolean answerFragmentHasEnded;
    private boolean questionHasStarted;
    private boolean questionHasEnded;
}
