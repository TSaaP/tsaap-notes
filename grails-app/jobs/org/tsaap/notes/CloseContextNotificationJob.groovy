package org.tsaap.notes



class CloseContextNotificationJob {

    ContextNotificationService contextNotificationService

    static triggers = {
        // every 5 minutes
        simple name: 'closeContextNotificationTrigger', startDelay: 10000, repeatInterval: 300000
    }

    def execute() {
        log.debug("Start closing scope notification job...")
        contextNotificationService.sendScopeClosingEmailMessages()
        log.debug("End closing scope notification job.")
    }
}
