package org.tsaap.questions

import grails.plugin.mail.MailService
import groovy.sql.Sql
import org.hibernate.SessionFactory

import javax.sql.DataSource

class ResponseNotificationService {

    static transactional = false

    MailService mailService
    DataSource dataSource
    SessionFactory sessionFactory

    def notififyUsersOnResponsesAndMentions() {
        Map notifications = findAllResponsesNotifications()
        Map notificationsMentions = findAllMentionsNotifications()
        def sub = "[tsaap-notes] Someone as reply to your note"
        notifications.each { user, questionMap ->
            def mentionsList = null
            if (notificationsMentions.containsKey(user)) {
                mentionsList = notificationsMentions.get(user)
                sub = "[tsaap-notes] Someone as reply to your note and someone mentioned you"
            }
            try {
                mailService.sendMail {
                    to user.email
                    subject sub
                    html view: "/email/responsesNotification", model: [user: user,
                                                                   questionMap: questionMap,
                                                                   mentionsList: mentionsList]
                }
                notificationsMentions.remove(user)
            } catch (Exception e) {
                log.error("Error with ${user.email} : ${e.message}")
            }
        }
        if(notificationsMentions.size()>0) {
            def questionMap = null
            sub = "[tsaap-notes] Someone mentioned you on his note"
            notificationsMentions.each { user, mentionsList ->
                try {
                    mailService.sendMail {
                        to user.email
                        subject sub
                        html view: "/email/responsesNotification", model: [user       : user,
                                                                           questionMap: questionMap,
                                                                           mentionsList: mentionsList]
                    }
                    notificationsMentions.remove(user)
                } catch (Exception e) {
                    log.error("Error with ${user.email} : ${e.message}")
                }
            }
        }
    }

    Map findAllResponsesNotifications() {
        def sql = new Sql(sessionFactory.currentSession.connection())
        def req = """
              SELECT tnote1.author_id as question_author, tuser1.first_name, tuser1.email, tcontext.id as context_id, tcontext.context_name,
              tnote1.fragment_tag_id as tag_id, if((tnote1.fragment_tag_id is null),null,ttag.name) tag_name,
              tnote1.id as question_id, tnote1.content as question, tuser2.username as response_author,
              tnote2.id as response_id, tnote2.content as response
              FROM note as tnote1, context as tcontext, note as tnote2, user as tuser1, user as tuser2, tag as ttag
              WHERE tnote2.date_created <= NOW()
              AND tnote2.date_created > date_sub(now(),interval 5 minute)
              AND tnote1.author_id = tuser1.id
              AND tnote1.context_id = tcontext.id
              AND tnote1.id = tnote2.parent_note_id
              AND tnote2.author_id = tuser2.id
              and ttag.id = tnote1.fragment_tag_id
              union
              SELECT tnote1.author_id as question_author, tuser1.first_name, tuser1.email, tcontext.id as context_id, tcontext.context_name,
              tnote1.fragment_tag_id as tag_id, if((tnote1.fragment_tag_id is null),null,ttag.name) tag_name,
              tnote1.id as question_id, tnote1.content as question, tuser2.username as response_author,
              tnote2.id as response_id, tnote2.content as response
              FROM note as tnote1, context as tcontext, note as tnote2, user as tuser1, user as tuser2, tag as ttag
              WHERE tnote2.date_created <= NOW()
              AND tnote2.date_created > date_sub(now(),interval 5 minute)
              AND tnote1.author_id = tuser1.id
              AND tnote1.context_id = tcontext.id
              AND tnote1.id = tnote2.parent_note_id
              AND tnote2.author_id = tuser2.id
              and tnote1.fragment_tag_id is null"""
        def rows = sql.rows(req)
        def notifications = [:]
        def questions = [:]
        rows.each {
            def key = [user_id: it.question_author, first_name: it.first_name, email: it.email]
            if (notifications[key] == null) {
                notifications[key] = [:]
            }
            def question_key = [context_id: it.context_id, context_name: it.context_name, fragment_tag: it.tag_id, fragment_tag_name: it.tag_name,
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

    Map findAllMentionsNotifications() {
        def sql = new Sql(sessionFactory.currentSession.connection())
        def req = """SELECT tmention.mention_id as receiver_id, tuser1.first_name, tuser1.email, tcontext.id as context_id,
                     tcontext.context_name, tnote.fragment_tag_id as tag_id, if((tnote.fragment_tag_id is null),null,ttag.name) tag_name, tuser2.username as author, tnote.content
                     FROM note_mention as tmention, note as tnote, context as tcontext, user as tuser1, user as tuser2, tag as ttag
                     WHERE tnote.date_created > date_sub(now(),interval 5 minute) and tnote.date_created <= NOW()
                     and tnote.id = tmention.note_id
                     AND tnote.context_id = tcontext.id
                     and tnote.author_id != tmention.mention_id
                     and tmention.mention_id = tuser1.id
                     and tnote.author_id = tuser2.id
                     and tnote.parent_note_id is null
                    and ttag.id = tnote.fragment_tag_id
                    union
                    SELECT tmention.mention_id as receiver_id, tuser1.first_name, tuser1.email, tcontext.id as context_id,
                    tcontext.context_name, tnote.fragment_tag_id as tag_id, if((tnote.fragment_tag_id is null),null,ttag.name) tag_name, tuser2.username as author, tnote.content
                    FROM note_mention as tmention, note as tnote, context as tcontext, user as tuser1, user as tuser2, tag as ttag
                    WHERE tnote.date_created > date_sub(now(),interval 5 minute) and tnote.date_created <= NOW()
                    and tnote.id = tmention.note_id
                    AND tnote.context_id = tcontext.id
                    and tnote.author_id != tmention.mention_id
                    and tmention.mention_id = tuser1.id
                    and tnote.author_id = tuser2.id
                    and tnote.parent_note_id is null
                    and tnote.fragment_tag_id is null;"""
        def rows = sql.rows(req)
        def notifications = [:]
        rows.each {
            def key = [user_id: it.receiver_id, first_name: it.first_name, email: it.email]
            if (notifications[key] == null) {
                notifications[key] = []
            }
            notifications[key] << [context_id: it.context_id, context_name: it.context_name, fragment_tag: it.tag_id, fragment_tag_name: it.tag_name, mention_author: it.author, mention_content: it.content]
        }
        sql.close()
        notifications
    }
}
