/*
 * Copyright 2014 Tsaap Development Group
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

package org.tsaap.questions.impl.gift.utils;

/**
 * Created by franck on 06/04/2014.
 */
public class QuestionHelper {

    private static final String escapedCloseBracketPlaceholder = "[[CB_PH]]";
    private static final String NewLinePlaceholder = "[[NL]]";

    /**
     * Insert the general feedback in a gift question
     *
     * @param generalFeedback
     * @param giftQuestion
     * @return the new gift question with the general feedback
     * @throws org.tsaap.questions.impl.gift.utils.GeneralFeedbackAlreadySetException
     * @throws org.tsaap.questions.impl.gift.utils.NoCloseBracketException
     */
    public String insertGeneralFeedbackInGiftQuestion(String generalFeedback, String giftQuestion)
            throws GeneralFeedbackAlreadySetException, NoCloseBracketException {
        if (giftQuestion.contains("####")) {
            throw new GeneralFeedbackAlreadySetException();
        }
        // in gift format some characters must be escaped
        String processedFeedback = generalFeedback.replace("\n", NewLinePlaceholder)
                .replace("\\", "\\\\")
                .replace(":", "\\:")
                .replace("{", "\\{")
                .replace("=", "\\=")
                .replace("~", "\\~")
                .replace("#", "\\#")
                .replace("}", "\\}")
                .replace(NewLinePlaceholder, "\n");
        // insert the processed feedback
        String processedGiftQuestion = giftQuestion.replace("\\}", escapedCloseBracketPlaceholder);
        if (processedGiftQuestion.lastIndexOf("}") == -1) {
            throw new NoCloseBracketException();
        }
        return processedGiftQuestion.replace("}", "####" + processedFeedback + "}")
                .replace(escapedCloseBracketPlaceholder, "\\}");
    }

}
