package org.tsaap.assignments

import grails.transaction.Transactional
import org.tsaap.contracts.Contract
import org.tsaap.directory.User

@Transactional
class AssignmentService {

    /**
     * Save an assignment
     * @param assignment the assignment to save
     *
     * @return the assignment saved or with errors
     */
    Assignment saveAssignment(Assignment assignment, Schedule schedule = null) {
        if (schedule) {
            schedule.save()
            schedule.assignment = assignment
        }
        assignment.save(flush: true)
        assignment
    }

    /**
     * Delete assignment
     * @param assignment the assignment to delete
     * @param user the user performing deletion
     * @param flush force flush
     */
    def deleteAssignment(Assignment assignment, User user, boolean flush = false) {
        Contract.requires(assignment.owner == user, USER__MUST__BE__ASSIGNMENT__OWNER)
        assignment.schedule?.delete(flush: flush)
        assignment.delete(flush: flush)
    }

    public static final String USER__MUST__BE__ASSIGNMENT__OWNER = "USER_MUST_BE_ASSIGNMENT_OWNER"
}
