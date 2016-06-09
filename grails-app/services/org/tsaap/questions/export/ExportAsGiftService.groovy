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
