package org.tsaap.questions

import grails.plugin.mail.MailService
import groovy.sql.Sql
import org.hibernate.SessionFactory
import org.springframework.context.MessageSource
import org.tsaap.directory.UnsubscribeKey
import javax.sql.DataSource

class ResponseNotificationService {

    static transactional = false

    MailService mailService
    DataSource dataSource
    SessionFactory sessionFactory
    MessageSource messageSource
    UnsubscribeKey key

    /**
     * Send mails to users mentioned in the last 5 minutes
     *
     * @return
     */
    def notifyUsersOnResponsesAndMentions() {
        Map notificationsMentions = findAllMentionsAndResponsesNotifications()

        if (notificationsMentions.size() > 0) {
            def questionMap = null
            notificationsMentions.each { user, map ->

                def sub = messageSource.getMessage("email.mention.notification.title", null, new Locale(map.language))
                try {
                    mailService.sendMail {
                        to map.email
                        subject sub
                        html view: "/email/responsesNotification", model: [user        : [user: map.user, language: map.language, first_name: map.first_name, email: map.email],
                                                                           mentionsList: map.mentionsList,
                                                                           key         : map.key]
                    }
                } catch (Exception e) {
                    log.error("Error with ${map.email} : ${e.message}")
                }
            }
        }
    }

    /**
     * Find notifications of users mentioned in the last 5 minutes
     *
     * @return a map with the user as key, and information and all notifications about the user as value
     */
    Map findAllMentionsAndResponsesNotifications() {
        def sql = new Sql(sessionFactory.currentSession.connection())
        def req = """
                SELECT distinct tmention.mention_id as receiver_id, tuser1.first_name, tuser1.email, tsettings.language, tcontext.id as context_id,
                tcontext.context_name, tnote.fragment_tag_id as tag_id, if((tnote.fragment_tag_id is null),null,ttag.name) tag_name, tuser2.username as author, tnote.content, tkey.unsubscribe_key as ukey
                FROM note_mention as tmention, note as tnote, context as tcontext, user as tuser1, user as tuser2, tag as ttag, settings as tsettings, unsubscribe_key as tkey
                WHERE tnote.date_created > date_sub(now(),interval 5 minute)
                and tnote.id = tmention.note_id
                AND tnote.context_id = tcontext.id
                and tnote.author_id != tmention.mention_id
                and tmention.mention_id = tuser1.id
                and tnote.author_id = tuser2.id
                and (tnote.fragment_tag_id is null or ttag.id = tnote.fragment_tag_id)
                and tuser1.id = tsettings.user_id
                and tsettings.mention_notifications = 1
                and tkey.user_id = tuser1.id
                and tcontext.closed = FALSE
                order by receiver_id
                """

        def rows = sql.rows(req)
        def notifications = [:]
        rows.each {
            def key = it.receiver_id
            if (notifications[key] == null) {
                notifications[key] = [key: it.ukey, first_name: it.first_name, email: it.email, language: it.language, mentionsList: []]
            }
            notifications[key].mentionsList << [context_id       : it.context_id, context_name: it.context_name, fragment_tag: it.tag_id,
                                                fragment_tag_name: it.tag_name, mention_author: it.author, mention_content: it.content]
        }
        sql.close()
        notifications
    }
}
