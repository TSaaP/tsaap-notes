package org.tsaap.directory

import grails.plugin.mail.MailService
import groovy.sql.Sql

import javax.sql.DataSource

class MailCheckingService {

  static transactional = false

  MailService mailService
  DataSource dataSource

  /**
   * Send email to check user emails and then activate the corresponding user
   * accounts
   * @param user the user to check email in
   */
  def sendCheckingEmailMessages() {
    Map notifications = findAllNotifications()
    List<String> actKeysWithEmailSent = []
    notifications.each { actKey, user ->
      actKeysWithEmailSent << actKey
      try {
        mailService.sendMail {
          to user.email
          subject "[tsaap-notes] Email checking"
          html view: "/email/emailChecking", model: [user: user,
                  actKey: actKey]
        }
      } catch (Exception e) {
        log.error("Error with ${it.email} : ${e.message}")
      }
    }
    updateEmailSentStatusForAllNotifications(actKeysWithEmailSent)
  }

  /**
   * The notifications is a map :<br>
   * <ul>
   *   <li> key : the activation key
   *   <li> value : a map representing the corresponding user [user_id:..., first_name:...,email:...]
   * </ul>
   * @return the notifications as a map
   */
  private Map findAllNotifications() {
    def sql = new Sql(dataSource)
    def req = """
              SELECT tuser.id as user_id, tuser.first_name, tuser.email, tact_key.activation_key
              FROM `tsaap-notes`.user as tuser
              INNER JOIN  `tsaap-notes`.activation_key as tact_key ON tact_key.user_id = tuser.id
              where tact_key.activation_email_sent = false"""
    def rows = sql.rows(req)
    def notifications = [:]
    rows.each {
      def key = it.activation_key
      notifications[key] = [user_id: it.user_id, first_name: it.first_name, email: it.email]
    }
    sql.close()
    notifications
  }

  /**
   *
   * @param actKeys list of activation keys which email was sent for
   */
  private updateEmailSentStatusForAllNotifications(List<String> actKeys) {
    try {
    def keys = actKeys.collect { "'$it'" }.join( ',' )
    def sql = new Sql(dataSource)
    def req = """
                update `tsaap-notes`.activation_key as tact_key
                set tact_key.activation_email_sent = 1
                where tact_key.activation_key in ($keys)
              """
    def nbUpdates = sql.executeUpdate(req)
    log.debug("the update request : $req")
    log.debug("Nb of rows updated : $nbUpdates")
    sql.close()
    } catch (Exception e) {
      log.error(e.message,e)
    }
  }
}
