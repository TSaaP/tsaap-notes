package org.tsaap.notes

class NotificationJob {

  NotificationService notificationService

  static triggers = {
    cron name: 'notificationCronTrigger', startDelay: 10000, cronExpression: '0 0 5 * * ?'
  }

  def execute() {
    log.info("Start notification job...")
    notificationService.notifyUsersOnTodayNotes()
    log.info("End notification job.")
  }
}
