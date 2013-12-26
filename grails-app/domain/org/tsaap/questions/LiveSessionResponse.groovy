package org.tsaap.questions

import org.tsaap.directory.User
import org.tsaap.questions.impl.gift.GiftQuestionService

class LiveSessionResponse {

    LiveSession liveSession
    User user
    String answerListAsString
    Float percentCredit = null

    static constraints = {
        answerListAsString nullable: true, blank: false, matches: '\\[,*(,?\\[".*"(,".*")*\\]|,?\\[\\])*,*\\]'
        percentCredit nullable: true
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
        if (percentCredit == null) {
            def answerBlockTextList
            try {
                // convert the answer list as string in list of list of string
                GroovyShell gs = new GroovyShell()
                answerBlockTextList = gs.evaluate(answerListAsString)
                def question = liveSession.note.question
                userResponse = giftQuestionService.createUserResponseForQuestionAndAnswerBlockList(user.username, question, answerBlockTextList)
                percentCredit = userResponse.evaluatePercentCredit()
            } catch (Exception e) {
                log.error(e.message)
                percentCredit = 0
            }
        }
        userResponse
    }
}
