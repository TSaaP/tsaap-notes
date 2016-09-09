package org.tsaap.contracts

/**
 * Simplified contract util class
 */
class Contract {

    static requires(Boolean condition, String message) {
        if (!condition) {
            throw new ConditionViolationException(message)
        }
    }
}
