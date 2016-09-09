package org.tsaap.contracts

/**
 * Condition violation exception
 */
class ConditionViolationException extends Exception {
    ConditionViolationException(String message) {
        super(message)
    }
}
