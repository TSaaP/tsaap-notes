package org.tsaap.questions

import org.tsaap.directory.User
import org.tsaap.notes.Note
import org.tsaap.questions.impl.gift.GiftQuestionService

class LiveSessionResponse {

    LiveSession liveSession
    User user
    String answerListAsString
    Float percentCredit = null
    Integer confidenceDegree

    SessionPhase sessionPhase
    Note explanation

    static constraints = {
        answerListAsString nullable: true, blank: false, matches: '\\[,*(,?\\[".*"(,".*")*\\]|,?\\[\\])*,*\\]'
        percentCredit nullable: true
        confidenceDegree nullable: true
        sessionPhase nullable: true
        explanation nullable: true
    }

    static transients = ['giftQuestionService', 'userResponse']

    GiftQuestionService giftQuestionService
    UserResponse userResponse

    /**
     * Get the user response corresponding to this live session response
     * and update the percent credit of the current live session Response
     * @return the user response
     */
    UserResponse getUserResponse() {
        if (userResponse == null) {
            def answerBlockTextList
            def question = liveSession.note.question
            try {
                // convert the answer list as string in list of list of string
                GroovyShell gs = new GroovyShell()
                answerBlockTextList = gs.evaluate(answerListAsString)
            } catch (Exception e) {
                log.error(e.message)
                answerBlockTextList = []
                for(int i = 0 ; i < question.answerBlockList.size(); i++) {
                    answerBlockTextList << []
                }
            }

            userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList(user.username, question, answerBlockTextList)
            if (percentCredit == null) {
                percentCredit = userResponse.evaluatePercentCredit()
            }
        }
        userResponse
    }
}
