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

package org.tsaap.questions.export

import org.gcontracts.annotations.Requires
import org.tsaap.directory.User
import org.tsaap.notes.Context
import org.tsaap.notes.Note
import org.tsaap.questions.impl.gift.utils.QuestionHelper

import java.text.DecimalFormat


class ExportAsGiftService {

    public static final double MIN_GRADE_FOR_FEEDBACK = 2.5
    QuestionHelper questionHelper

    /**
     * Find all gift questions with notes as feedback for a given context
     * @param context the context
     * @return the list of questions as gift strings
     */
    @Requires({ context.owner == user })
    List<String> findAllGiftQuestionsWithNotesAsFeedbackForContext(User user, Context context, String feedbackPrefix) {
        def questions = findAllQuestionsForContext(context)
        def res = []
        questions.each {
            def notesOnQuestion = Note.findAllByParentNoteAndGradeGreaterThanEquals(it, MIN_GRADE_FOR_FEEDBACK, [sort: "grade", order: "desc"])
            if (notesOnQuestion) {
                def generalFeedback = buildGeneralFeedback(notesOnQuestion, feedbackPrefix)
                res.add(questionHelper.insertGeneralFeedbackInGiftQuestion(generalFeedback, it.content))
            } else {
                res.add(it.content)
            }
        }
        res
    }

    /**
     * Find all gift questions (without feedback) for a given context
     * @param context the context
     * @return the list of questions as gift strings
     */
    @Requires({ context.owner == user })
    List<String> findAllGiftQuestionsForContext(User user, Context context) {
        def questions = findAllQuestionsForContext(context)
        def res = []
        questions.each {
            res.add(it.content)
        }
        res
    }

    private def findAllQuestionsForContext(Context context) {
        def criteria = Note.createCriteria()
        def questions = criteria.list {
            eq('context', context)
            like('content', "::%")
        }
        questions
    }

    private String buildGeneralFeedback(List<Note> notes, String feedbackPrefix) {
        StringBuilder sb = new StringBuilder("$feedbackPrefix<br>")
        DecimalFormat df = new DecimalFormat("###,##0.00");
        notes.each {
            sb.append("${df.format(it.grade)} /5 @${it.author.username}: ")
            sb.append(it.content)
            sb.append("<br>")
        }
        sb.toString()
    }
}
