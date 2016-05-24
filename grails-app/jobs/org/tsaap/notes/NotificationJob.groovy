package org.tsaap.notes

class NotificationJob {

  NotificationService notificationService

  static triggers = {
    // everyday at 5am
    cron name: 'notificationCronTrigger', startDelay: 10000, cronExpression: '0 0 5 * * ?'
  }

  def execute() {
    log.debug("Start notification job...")
    notificationService.notifyUsersOnTodayNotes()
    log.debug("End notification job.")
  }
}
