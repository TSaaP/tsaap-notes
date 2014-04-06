package org.tsaap.questions.export

import org.gcontracts.annotations.Requires
import org.tsaap.directory.User
import org.tsaap.notes.Context
import org.tsaap.notes.Note
import org.tsaap.questions.impl.gift.utils.QuestionHelper


class ExportAsGiftService {

    QuestionHelper questionHelper

    /**
     * Find all gift questions with notes as feedback for a given context
     * @param context the context
     * @return the list of questions as gift strings
     */
    @Requires({ context.owner == user })
    List<String> findAllGiftQuestionsWithNotesAsFeedbackForContext(User user, Context context, String feedbackPrefix) {
        def criteria = Note.createCriteria()
        def questions = criteria.list {
            eq('context',context)
            like('content',"::%")
        }
        def res = []
        questions.each {
            def notesOnQuestion = Note.findAllByParentNote(it)
            if (notesOnQuestion) {
                def generalFeedback = buildGeneralFeedback(notesOnQuestion, feedbackPrefix)
                res.add(questionHelper.insertGeneralFeedbackInGiftQuestion(generalFeedback, it.content))
            } else {
                res.add(it.content)
            }
        }
        res
    }

    private String buildGeneralFeedback(List<Note> notes, String feedbackPrefix) {
        StringBuilder sb = new StringBuilder("$feedbackPrefix<br>")
        notes.each {
            sb.append("@${it.author.username}: ")
            sb.append(it.content)
            sb.append("<br>")
        }
        sb.toString()
    }
}
