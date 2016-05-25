package org.tsaap.notes

import grails.plugin.mail.MailService
import groovy.sql.Sql
import org.springframework.context.MessageSource
import org.tsaap.directory.UnsubscribeKey
import org.tsaap.directory.UnsubscribeKeyService
import org.tsaap.directory.User

import javax.sql.DataSource

class NotificationService {

  static transactional = false

  MailService mailService
  ContextService contextService
  DataSource dataSource
  MessageSource messageSource
  UnsubscribeKeyService unsubscribeKeyService
  UnsubscribeKey key

  /**
   * Notify the given user on  notes of the day coming from the context the user
   * follows
   * @param user the user
   */
  def notifyUsersOnTodayNotes() {
    Map notifications = findAllNotifications()
    notifications.each { user, contextList ->


      try {
        def sub = messageSource.getMessage("email.notes.notification.title",null,new Locale(user.language))
        mailService.sendMail {
          to user.email
          subject sub
          html view: "/email/notesNotification", model: [user: user,
                  contextList: contextList, key:contextList.key]
        }
      } catch (Exception e) {
        log.error("Error with ${user.email} : ${e.message}")
      }
    }
  }

/**
 * The notifications is a map :<br>
 * <ul>
 *   <li> key : a map that specifies a user [user_id:..., first_name:...,email:...]
 *   <li> value : a list of map, each map specifies data context [context_id:..., context_name:..., count_notes:...]
 * </ul>
 * The count_notes is the count of new notes in the current context since yesterday same time.
 * @return the notifications as a map
 */
  private Map findAllNotifications() {
    def sql = new Sql(dataSource)
    def req = """
              SELECT tuser.id as user_id, tuser.first_name, tuser.email, tuser.language, tcontext.id as context_id, tcontext.context_name, count(tnote.id) as count_notes, tkey.unsubscribe_key as ukey
              FROM note as tnote
              INNER JOIN context_follower as tcontextfo ON tnote.context_id = tcontextfo.context_id
              INNER JOIN context as tcontext ON tcontextfo.context_id = tcontext.id
              INNER JOIN user as tuser ON tcontextfo.follower_id = tuser.id
              INNER JOIN settings as tsettings ON tsettings.user_id = tuser.id
              INNER JOIN unsubscribe_key as tkey ON tkey.user_id = tuser.id
              where tnote.date_created < NOW() and tnote.date_created > concat(date_sub(curdate(),interval 1 day),' ',curtime()) and tuser.enabled = TRUE
                and tsettings.daily_notifications = 1
              group by context_id, user_id, tkey.id
              UNION
              SELECT tuser.id as user_id, tuser.first_name, tuser.email, tuser.language, tcontext.id as context_id, tcontext.context_name, count(tnote.id), tkey.unsubscribe_key as ukey
              FROM note as tnote
              INNER JOIN context as tcontext ON tnote.context_id = tcontext.id
              INNER JOIN user as tuser ON tcontext.owner_id = tuser.id
              INNER JOIN settings as tsettings ON tsettings.user_id = tuser.id
              INNER JOIN unsubscribe_key as tkey ON tkey.user_id = tuser.id
              where tnote.date_created < NOW() and tnote.date_created > concat(date_sub(curdate(),interval 1 day),' ',curtime()) and tuser.enabled = TRUE
                and tsettings.daily_notifications = 1
              group by context_id, user_id, tkey.id
              order by user_id,context_name """
    def rows = sql.rows(req)
    def notifications = [:]
    rows.each {
      def key = [user_id: it.user_id, first_name: it.first_name, email: it.email, language: it.language]
      if (notifications[key] == null) {
        notifications[key] = []
      }
      notifications[key] << [context_id: it.context_id, context_name: it.context_name, count_notes: it.count_notes, key: it.ukey]
    }
    sql.close()
    notifications
  }

}





