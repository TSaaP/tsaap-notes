package org.tsaap.assignments

import grails.transaction.Transactional

@Transactional
class AssignmentService {

    /**
     * Save an assignment
     * @param assignment the assignment to save
     * @return the assignment saved or with errors
     */
    Assignment saveAssignment(Assignment assignment) {
        assignment.save(flush: true)
        assignment
    }
}
