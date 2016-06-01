package org.tsaap.notes

import org.tsaap.directory.Role
import org.tsaap.directory.RoleEnum
import org.tsaap.directory.User
import org.tsaap.directory.UserAccountService
import org.tsaap.questions.ResponseNotificationService
import spock.lang.Specification

/**
 * Created by dylan on 27/05/15.
 */
class ResponseNotificationIntegrationSpec extends Specification {

    ResponseNotificationService responseNotificationService
    NoteService noteService
    UserAccountService userAccountService
    ContextService contextService
    User paul
    User mary
    Context context

    def setup() {
        paul = userAccountService.addUser(new User(firstName: "Paul", lastName: "D", username: "learner_paul", password: "password", email: "paul@nomail.com", language: 'fr'), Role.findByRoleName(RoleEnum.STUDENT_ROLE.name()))
        mary = userAccountService.addUser(new User(firstName: "Mary", lastName: "S", username: "learner_Mary", password: "password", email: "mary@nomail.com", language: 'fr'), Role.findByRoleName(RoleEnum.STUDENT_ROLE.name()))
        context = contextService.saveContext(new Context(owner: paul, contextName: "Context1", url: 'http://www.w3.org', descriptionAsNote: 'a description', source: null))

    }

    void "finding all notes with an user are mentioned"() {
        given: "the user mary mention user paul in a note"
        Tag tag = Tag.findOrSaveWhere(name: 'tagtest')
        Note note1 = noteService.addStandardNote(mary, "mary's mention @learner_paul", context, tag)

        when: "we want to know if someone in the five last minutes as mentioned someone"
        Map result = responseNotificationService.findAllMentionsAndResponsesNotifications()

        then: "we got a map with one notification for Paul"
        result.containsKey(paul.id)
        def res = result.get(paul.id)
        res.mentionsList.contains([context_id     : context.id, context_name: context.contextName,
                                   fragment_tag   : tag.id, fragment_tag_name: tag.name, mention_author: mary.username,
                                   mention_content: note1.content])
    }

    void "finding all responses to an user note"() {
        given: "the user mary responds to user paul in a note"
        Tag tag = Tag.findOrSaveWhere(name: 'tagtest')
        Note note1 = noteService.addStandardNote(paul, "Test response", context, tag)
        Note note2 = noteService.addStandardNote(mary, "mary's mention @learner_paul", context, tag, note1)

        when: "we want to know if someone in the five last minutes responded"
        Map result = responseNotificationService.findAllMentionsAndResponsesNotifications()

        then: "we got a map with one notification for Paul"
        result.containsKey(paul.id)
        def res = result.get(paul.id)
        res.mentionsList.contains([context_id     : context.id, context_name: context.contextName,
                                   fragment_tag   : tag.id, fragment_tag_name: tag.name, mention_author: mary.username,
                                   mention_content: note2.content])
    }
}
