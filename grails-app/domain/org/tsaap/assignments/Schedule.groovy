package org.tsaap.assignments

import org.tsaap.contracts.Contract

class Schedule {

    Date dateCreated

    Boolean isManual = true
    Date startDate
    Date endDate
    Long durationInMinutes

    Boolean isStarted = false
    Boolean isEnded = false

    Assignment assignment
    Interaction interaction

    static constraints = {
        isManual nullable: false
        startDate nullable: false
        endDate nullable: true, validator: { val, obj ->
            if (val != null && val <= obj.startDate) return ['endDateBeforeStartDate']
        }
        durationInMinutes nullable: true
        assignment nullable: true
        interaction nullable: true
    }

    /**
     * Check if the scheduled activity is open
     * @return true if open
     */
    Boolean isOpen() {
        isStarted && !isEnded
    }

    /**
     * Check if the scheduled activity is closed
     * @return true if closed
     */
    Boolean isClosed() {
        !isOpen()
    }

    /**
     * Start the scheduled activity
     * @return
     */
    def start() {
        Contract.requires(!isOpen(), IS__OPEN)
        isStarted = true
        isEnded = false
        save()
    }

    /**
     * Close tje scheduled activity
     * @return
     */
    def end() {
        Contract.requires(!isClosed(), IS__CLOSED)
        isEnded = true
        save()
    }


    public static final String IS__OPEN = "IS_ALREADY_OPEN"
    public static final String IS__CLOSED = "IS_ALREADY_CLOSED"
}
