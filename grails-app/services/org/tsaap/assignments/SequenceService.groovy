package org.tsaap.assignments

import grails.transaction.Transactional
import org.springframework.validation.FieldError
import org.tsaap.contracts.Contract
import org.tsaap.directory.User

@Transactional
class SequenceService {

    /**
     * Save a sequence
     * @param sequence a sequence
     * @param owner the owner of the sequence
     * @param assignment the assignment
     * @param statement the statement
     * @return the saved sequence or the sequence with errors
     */
    Sequence saveSequence(Sequence sequence, User owner, Assignment assignment, Statement statement) {
        sequence.owner = owner
        sequence.assignment = assignment
        sequence.statement = statement
        sequence.save()
        sequence
    }

    /**
     * Save a statement
     * @param statement the statement to save
     * @return the saved statement or the statement with errors
     */
    Statement saveStatement(Statement statement, User owner) {
        statement.owner = owner
        statement.save()
        statement
    }


    /**
     * Add a sequence to an assignment
     * @param assignment the assignment to add the sequence
     * @param statement the statement the sequence is based on
     * @param user the user adding the sequence
     * @return the sequence
     */
    Sequence addSequenceToAssignment(Assignment assignment, User user, Statement statement, List<Interaction> interactions = null) {
        Contract.requires(assignment.owner == user, AssignmentService.USER__MUST__BE__ASSIGNMENT__OWNER)
        def rank = assignment.lastSequence ? assignment.lastSequence.rank + 1 : 1
        Sequence sequence = new Sequence(rank: rank)
        statement.save()
        if (!statement.hasErrors()) {
            saveSequence(sequence,user,assignment,statement)
            interactions.each {
                it.owner = user
                it.sequence = sequence
                it.save(failOnError: true)
            }
            assignment.lastUpdated = new Date()
            assignment.save()
        } else {
            statement.errors.allErrors.each { FieldError error ->
                sequence.errors.rejectValue(error.field, error.code)
            }
        }
        sequence
    }

    /**
     * Update statement and interactions of a sequence
     * @param sequence the sequence
     * @param user the user performing the operation
     * @return the sequence
     */
    Sequence updateStatementAndInteractionsOfSequence(Sequence sequence, User user, List<Interaction> interactionsToAdd = null) {
        Statement statement = sequence.statement
        Contract.requires(statement.owner == user, AssignmentService.USER__MUST__BE__ASSIGNMENT__OWNER)
        statement.save()
        if (!statement.hasErrors()) {
            interactionsToAdd.each {
                it.owner = user
                it.sequence = sequence
                it.save(failOnError: true)
            }
            sequence.interactions.each { it.save(failOnError: true)}
            Assignment assignment = sequence.assignment
            assignment.lastUpdated = new Date()
            assignment.save()
        } else {
            statement.errors.allErrors.each { FieldError error ->
                sequence.errors.rejectValue(error.field, error.code)
            }
        }
        sequence
    }
}
