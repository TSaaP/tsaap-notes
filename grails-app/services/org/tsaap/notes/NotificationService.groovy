package org.tsaap.notes

import grails.plugin.mail.MailService
import groovy.sql.Sql

import javax.sql.DataSource

class NotificationService {

  static transactional = false

  MailService mailService
  ContextService contextService
  DataSource dataSource

  /**
   * Notify the given user on  notes of the day coming from the context the user
   * follows
   * @param user the user
   */
  def notifyUsersOnTodayNotes() {
    Map notifications = findAllNotifications()
    notifications.each { user, contextList ->
      try {
        mailService.sendMail {
          to user.email
          subject "[tsaap-notes] notes of the day"
          html view: "/email/notesNotification", model: [user: user,
                  contextList: contextList]
        }
      } catch (Exception e) {
        log.error("Error with ${it.email} : ${e.message}")
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
              SELECT tuser.id as user_id, tuser.first_name, tuser.email, tcontext.id as context_id, tcontext.context_name, count(tnote.id) as count_notes
              FROM `tsaap-notes`.note as tnote
              INNER JOIN `tsaap-notes`.context_follower as tcontextfo ON tnote.context_id = tcontextfo.context_id
              INNER JOIN `tsaap-notes`.context as tcontext ON tcontextfo.context_id = tcontext.id
              INNER JOIN `tsaap-notes`.user as tuser ON tcontextfo.follower_id = tuser.id
              where tnote.date_created < NOW() and tnote.date_created > concat(date_sub(curdate(),interval 1 day),' ',curtime())
              group by context_id, user_id
              UNION
              SELECT tuser.id as user_id, tuser.first_name, tuser.email, tcontext.id as context_id, tcontext.context_name, count(tnote.id)
              FROM `tsaap-notes`.note as tnote
              INNER JOIN `tsaap-notes`.context as tcontext ON tnote.context_id = tcontext.id
              INNER JOIN `tsaap-notes`.user as tuser ON tcontext.owner_id = tuser.id
              where tnote.date_created < NOW() and tnote.date_created > concat(date_sub(curdate(),interval 1 day),' ',curtime())
              group by context_id, user_id
              order by user_id,context_name """
    def rows = sql.rows(req)
    def notifications = [:]
    rows.each {
      def key = [user_id: it.user_id, first_name: it.first_name, email: it.email]
      if (notifications[key] == null) {
        notifications[key] = []
      }
      notifications[key] << [context_id: it.context_id, context_name: it.context_name, count_notes: it.count_notes]
    }
    notifications
  }

}





