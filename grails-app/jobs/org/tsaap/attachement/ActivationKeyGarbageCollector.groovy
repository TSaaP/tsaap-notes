package org.tsaap.attachement


class ActivationKeyGarbageCollector {


    static triggers = {
        //
        cron name: 'garbageCronTrigger', startDelay: 10000, cronExpression: '0 0 0/1 * * ?'
    }

    def execute() {
        log.debug("Start Activation Key garbage collector job ...")

        log.debug("End Activation Key garbage collector job.")
    }
}
