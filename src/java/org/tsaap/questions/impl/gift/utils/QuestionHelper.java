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
