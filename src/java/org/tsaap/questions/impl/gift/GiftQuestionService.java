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

import org.tsaap.questions.Answer;
import org.tsaap.questions.Question;
import org.tsaap.questions.Quiz;
import org.tsaap.questions.UserResponse;
import org.tsaap.questions.impl.DefaultAnswerBlock;
import org.tsaap.questions.impl.DefaultUserAnswerBlock;
import org.tsaap.questions.impl.DefaultUserResponse;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author franck Silvestre
 */
public class GiftQuestionService {

    /**
     * Get question from its gift text specification
     *
     * @param giftText the gift text
     * @return the result question
     */
    public Question getQuestionFromGiftText(String giftText) throws IOException, GiftReaderException {
        Quiz quiz = getQuizFromGiftText(giftText);
        return quiz.getQuestionList().get(0);
    }

    /**
     * Get quiz from its gift text specification
     *
     * @param giftText the gift text
     * @return the result quiz
     */
    public Quiz getQuizFromGiftText(String giftText) throws IOException, GiftReaderException {
        GiftQuizContentHandler handler = new GiftQuizContentHandler();
        GiftReader quizReader = new GiftReader();
        quizReader.setQuizContentHandler(handler);
        StringReader reader = new StringReader(giftText);
        quizReader.parse(reader);
        return handler.getQuiz();
    }

    /**
     * Create a user response for a question. The user response is specified by a text
     * representation of the response.
     *
     * @param userId              the user identifier
     * @param question            the question
     * @param answerBlockTextList the list of answer text block  of the response
     * @return the created user response
     */
    public UserResponse createUserResponseForQuestionAndAnswerBlockList(String userId, Question question, List<List<String>> answerBlockTextList) throws GiftUserResponseAnswerNotFoundInChoiceList, GiftUserResponseAnswerBlockListSizeIsNotValidInResponse {
        if (question.getAnswerBlockList().size() != answerBlockTextList.size()) {
            throw new GiftUserResponseAnswerBlockListSizeIsNotValidInResponse();
        }
        DefaultUserResponse userResponse = new DefaultUserResponse();
        userResponse.setUserIdentifier(userId);
        userResponse.setQuestion(question);
        for (int i = 0; i < question.getAnswerBlockList().size(); i++) {
            DefaultAnswerBlock currentAnsBlock = (DefaultAnswerBlock) question.getAnswerBlockList().get(i);
            DefaultUserAnswerBlock currentUserAnsBlock = new DefaultUserAnswerBlock();
            userResponse.getUserAnswerBlockList().add(currentUserAnsBlock);
            for (String userAnsString : answerBlockTextList.get(i)) {
                boolean answerHasBeenFound = false;
                for (Answer answer : currentAnsBlock.getAnswerList()) {
                    if (answer.getTextValue().equals(userAnsString)) {
                        currentUserAnsBlock.getAnswerList().add(answer);
                        answerHasBeenFound = true;
                        break;
                    }
                }
                if (!answerHasBeenFound) {
                    throw new GiftUserResponseAnswerNotFoundInChoiceList();
                }
            }
        }

        return userResponse;
    }


}
