package org.tsaap.notes

import org.tsaap.BootstrapTestService
import org.tsaap.questions.ResponseNotificationService
import spock.lang.Specification

/**
 * Created by dylan on 27/05/15.
 */
class ResponseNotificationIntegrationSpec extends Specification {

    ResponseNotificationService responseNotificationService
    BootstrapTestService bootstrapTestService
    NoteService noteService

    def setup() {
        bootstrapTestService.initializeTests()
        bootstrapTestService.context1.refresh()
        bootstrapTestService.context2.refresh()
    }

    void "finding all responses to notify"() {
        given: "the user paul ask two questions"
        Tag tag = Tag.findOrSaveWhere(name: 'tagtest')
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerPaul,"paul's question 1",bootstrapTestService.context1,tag)
        Note note2 = noteService.addStandardNote(bootstrapTestService.learnerPaul,"paul's question 2",bootstrapTestService.context1,tag)

        and: "two other users answer to paul's questions"
        Note note3 = noteService.addStandardNote(bootstrapTestService.learnerMary,"mary's response 1",bootstrapTestService.context1,tag,note1)
        Note note4 = noteService.addStandardNote(bootstrapTestService.learnerMary,"mary's response 2",bootstrapTestService.context2,tag,note2)
        Note note5 = noteService.addStandardNote(bootstrapTestService.teacherJeanne,"jeanne's response 1",bootstrapTestService.context1,tag,note1)

        when: "we want to know if someone in the five last minutes answer to an user question"
        Map result = responseNotificationService.findAllResponsesNotifications()

        then: "we got a map with 1 notifications for Paul's questions."
        result.containsKey([user_id: bootstrapTestService.learnerPaul.id, first_name: bootstrapTestService.learnerPaul.firstName, email: bootstrapTestService.learnerPaul.email, language: bootstrapTestService.learnerPaul.language])
        def res2 = result.get([user_id: bootstrapTestService.learnerPaul.id, first_name: bootstrapTestService.learnerPaul.firstName, email: bootstrapTestService.learnerPaul.email, language: bootstrapTestService.learnerPaul.language])
        res2.containsKey([context_id: bootstrapTestService.context1.id, context_name: bootstrapTestService.context1.contextName, fragment_tag: tag.id, fragment_tag_name: tag.name,
                          question_id: note1.id, question_content: "paul's question 1"])
        def res3 = res2.get([context_id: bootstrapTestService.context1.id, context_name: bootstrapTestService.context1.contextName, fragment_tag: tag.id, fragment_tag_name: tag.name,
                             question_id: note1.id, question_content: "paul's question 1"])
        res3.contains([response_author: bootstrapTestService.learnerMary.username, response_id: note3.id, response_content: "mary's response 1"])
    }

    void "finding all notes with an user are mentioned" () {
        given: "the user mary mention user paul in a note"
        Tag tag = Tag.findOrSaveWhere(name: 'tagtest')
        Note note1 = noteService.addStandardNote(bootstrapTestService.learnerMary,"mary's mention @learner_paul",bootstrapTestService.context1,tag)

        when: "we want to know if someone in the five last minutes as mentioned someone"
        Map result = responseNotificationService.findAllMentionsNotifications()

        then: "we got a map with one notification for Paul"
        result.containsKey([user_id: bootstrapTestService.learnerPaul.id, first_name: bootstrapTestService.learnerPaul.firstName, email: bootstrapTestService.learnerPaul.email, language: bootstrapTestService.learnerPaul.language])
        def res = result.get([user_id: bootstrapTestService.learnerPaul.id, first_name: bootstrapTestService.learnerPaul.firstName, email: bootstrapTestService.learnerPaul.email, language: bootstrapTestService.learnerPaul.language])
        res.contains([context_id: bootstrapTestService.context1.id, context_name: bootstrapTestService.context1.contextName, fragment_tag: tag.id, fragment_tag_name: tag.name, mention_author: bootstrapTestService.learnerMary.username, mention_content: note1.content])
    }
}
