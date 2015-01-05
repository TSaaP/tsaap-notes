package org.tsaap.questions

import org.gcontracts.annotations.Requires

class SessionPhase {

    Date dateCreated
    String status = LiveSessionStatus.NotStarted.name()
    Date startDate
    Date endDate
    String resultMatrixAsJson
    Integer rank

    LiveSession liveSession

    static constraints = {
        status inList: LiveSessionStatus.values()*.name()
        startDate nullable: true
        endDate nullable: true
        resultMatrixAsJson nullable: true
    }


    /**
     * Flag to indicate if the phase is not started
     * @return true if the phase is not started,false otherwise
     */
    boolean isNotStarted() {
        status == LiveSessionStatus.NotStarted.name()
    }

    /**
     * Flag to indicate if the phase is started
     * @return true if the phase is started,false otherwise
     */
    boolean isStarted() {
        status == LiveSessionStatus.Started.name()
    }

    /**
     * Flag to indicate if the phase is stopped
     * @return true if the phase is stopped,false otherwise
     */
    boolean isStopped() {
        status == LiveSessionStatus.Ended.name()
    }

    /**
     * Start the current phase
     */
    @Requires({ isNotStarted() })
    def start() {
        status = LiveSessionStatus.Started.name()
        startDate = new Date()
        save(flush: true)
    }

    /**
     * Stop the current phase
     */
    @Requires({ isStarted() })
    def stop(boolean shouldBuildResultMatrix = true) {
        status = LiveSessionStatus.Ended.name()
        endDate = new Date()
        if (shouldBuildResultMatrix) {
           // updateResultMatrixAsJson()
        }
        save(flush: true)
    }
}
