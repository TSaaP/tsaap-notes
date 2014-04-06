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
    @Requires({context.owner == user})
    List<String> findAllGiftQuestionsWithNotesAsFeedbackForContext(User user,Context context) {
        def questions = Note.findAllByContextAndAQuestion(context,true)
        def res = []
        questions.each {
            def notesOnQuestion = Note.findAllByParentNoteAndAQuestion(it, false)
            def generalFeedback = buildGeneralFeedback(notesOnQuestion)
            res.add(questionHelper.insertGeneralFeedbackInGiftQuestion(generalFeedback,it.content))
        }
        res
    }

    private String buildGeneralFeedback(List<Note> notes) {
        StringBuilder sb = new StringBuilder()
        notes.each {
            sb.append("@${it.author.username}: ")
            sb.append(it.content)
            sb.append("\n")
        }
        sb.toString()
    }
}
