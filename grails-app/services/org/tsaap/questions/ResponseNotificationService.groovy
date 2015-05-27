package org.tsaap.questions

import grails.plugin.mail.MailService
import grails.transaction.Transactional
import groovy.sql.Sql
import org.hibernate.SessionFactory
import org.tsaap.notes.Note

import javax.sql.DataSource

class ResponseNotificationService {

    static transactional = false

    MailService mailService
    DataSource dataSource
    SessionFactory sessionFactory

    def notififyUsersOnResponses() {
        Map notifications = findAllResponsesNotifications()
        notifications.each { user, questionMap ->
            try {
                mailService.sendMail {
                    to user.email
                    subject "[tsaap-notes] questions response"
                    html view: "/email/responsesNotification", model: [user: user,
                                                                   questionMap: questionMap]
                }
            } catch (Exception e) {
                log.error("Error with ${user.email} : ${e.message}")
            }
        }
    }

    Map findAllResponsesNotifications() {
        def sql = new Sql(sessionFactory.currentSession.connection())
        def req = """
              SELECT tnote1.author_id as question_author, tuser1.first_name, tuser1.email, tcontext.id as context_id, tcontext.context_name,
              tnote1.fragment_tag_id as tag_id,
              tnote1.id as question_id, tnote1.content as question, tuser2.username as response_author,
              tnote2.id as response_id, tnote2.content as response
              FROM note as tnote1, context as tcontext, note as tnote2, user as tuser1, user as tuser2, tag as ttag
              WHERE tnote2.date_created <= NOW()
              AND tnote2.date_created > date_sub(now(),interval 5 minute)
              AND tnote1.author_id = tuser1.id
              AND tnote1.context_id = tcontext.id
              AND tnote1.id = tnote2.parent_note_id
              AND tnote2.author_id = tuser2.id
              GROUP BY response"""
        def rows = sql.rows(req)
        def notifications = [:]
        def questions = [:]
        rows.each {
            def key = [question_author: it.question_author, first_name: it.first_name, email: it.email]
            if (notifications[key] == null) {
                notifications[key] = [:]
            }
            def question_key = [context_id: it.context_id, context_name: it.context_name, fragment_tag: it.tag_id,
                                question_id: it.question_id, question_content: it.question]
            if(questions[question_key] == null) {
                questions[question_key] = []
            }
            questions[question_key] << [response_author: it.response_author, response_id:it.response_id, response_content: it.response]
            notifications[key] << questions
        }
        sql.close()
        notifications
    }
}
