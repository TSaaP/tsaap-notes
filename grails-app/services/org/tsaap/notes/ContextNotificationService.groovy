package org.tsaap.notes

import grails.plugin.mail.MailService
import groovy.sql.Sql
import javax.sql.DataSource

class ContextNotificationService {

  static transactional = false

  MailService mailService
  ContextService contextService
  DataSource dataSource

  /**
   * Notify users about the closure of a scope
   * @return
   */

  def sendScopeClosingEmailMessages() {
    Map notifications = findNotifications()
    notifications.each { context_name, user ->

      try {
        def sub = "scope closed"
        mailService.sendMail {
          to user.email
          subject sub
          html view: "/email/scopeClosing", model: [user: user,
                                                     context_name: context_name]
        }
      } catch (Exception e) {
        log.error("Error with ${user.email} : ${e.message}")
      }
    }

  }

  /**
   * Find users who follow a closed scope in the last 5 minutes
   * @return a map with the context_name as key, and  informations about the user as value
   */
  private Map findNotifications() {
    def sql = new Sql(dataSource)
    def req = """
                select tuser.id, tuser.email, tuser.first_name, tcontext.context_name, tsettings.language
                from context as tcontext, context_follower as tfollower, user as tuser, settings as tsettings
                where tcontext.closed = TRUE and tcontext.id = tfollower.context_id and
                tfollower.follower_id = tuser.id and tcontext.last_updated > date_sub(now(),interval 5 minute) and
                tsettings.user_id = tuser.id
              """
    def rows = sql.rows(req)
    def notifications = [:]
    rows.each {
      def key = it.context_name
      notifications[key] = [user_id: it.id, first_name: it.first_name, email: it.email, language: it.language]
    }
    sql.close()
    notifications
  }

}





