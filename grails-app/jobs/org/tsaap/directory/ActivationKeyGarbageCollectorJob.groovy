package org.tsaap.directory

class ActivationKeyGarbageCollectorJob {

    ActivationKeyService activationKeyService

    static triggers = {
        cron name: 'garbageActKeyCronTrigger', startDelay: 10000, cronExpression: '0 0 0/1 * * ?'
    }

    def execute() {
        log.debug("Start Activation Key garbage collector job ...")
        activationKeyService.removeOldActivationKeys()
        log.debug("End Activation Key garbage collector job.")
    }
}
