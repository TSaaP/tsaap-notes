package org.tsaap.attachement

/**
 * Created by dylan on 03/06/15.
 */
class GarbageCollectorJob {

    AttachementService attachementService

    static triggers = {
        cron name: 'garbageCronTrigger', startDelay: 10000, cronExpression: '0 0 4 * * ?'
    }

    def execute() {
        log.debug("Start garbage collector job...")
        attachementService.deleteAttachementAndFileInSystem()
        log.debug("End garbage collector job.")
    }
}
