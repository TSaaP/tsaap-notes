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
        if (!sequence.hasErrors()) {
            sequence.save()
        }
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
    Sequence addSequenceToAssignment(Assignment assignment, User user, Statement statement, List<Interaction> interactions = null, boolean phasesAreScheduled = false) {
        Contract.requires(assignment.owner == user, AssignmentService.USER__MUST__BE__ASSIGNMENT__OWNER)
        def rank = assignment.lastSequence ? assignment.lastSequence.rank + 1 : 1
        Sequence sequence = new Sequence(rank: rank, phasesAreScheduled: phasesAreScheduled)
        saveOrUpdateStatement(statement, sequence)
        validateInteractions(interactions, user, sequence)
        saveSequence(sequence, user, assignment, statement)
        saveInteractions(interactions, sequence)
        updateAssignmentLastUpdated(sequence, assignment)
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
        saveOrUpdateStatement(statement, sequence)
        validateInteractions(interactionsToAdd, user, sequence)
        saveInteractions(interactionsToAdd, sequence)
        updateInteractions(sequence)
        updateAssignmentLastUpdated(sequence, sequence.assignment)
        sequence
    }

    private void updateAssignmentLastUpdated(Sequence sequence, Assignment assignment) {
        if (!sequence.hasErrors()) {
            if (sequence.interactions) {
                sequence.activeInteraction = sequence.interactions[0]
            }
            assignment.lastUpdated = new Date()
            assignment.save()
        }
    }

    private void saveOrUpdateStatement(Statement statement, Sequence sequence) {
        if (!sequence.hasErrors()) {
            statement.save()
            if (statement.hasErrors()) {
                statement.errors.allErrors.each { FieldError error ->
                    def code = error.code
                    if (error.field.contains("content")) {
                        code = "sequence.content.blank"
                    } else if (error.field.contains("title")) {
                        code = "sequence.title.blank"
                    }
                    sequence.errors.rejectValue(error.field, code)
                }
            }
        }
    }

    private def updateInteractions(Sequence sequence) {
        sequence.interactions.eachWithIndex { def interaction, int i ->
            interaction.save(failOnError: true)
            interaction.schedule.save()
            processInteractionScheduleError(interaction,i,sequence)
        }
    }

    private def validateInteractions(List<Interaction> interactions, User user, Sequence sequence) {
        interactions.eachWithIndex { def interaction, int i ->
            interaction.owner = user
            interaction.sequence = sequence
            interaction.schedule.interaction = interaction
            interaction.schedule.validate()
            processInteractionScheduleError(interaction, i, sequence)
        }
    }

    private void processInteractionScheduleError(Interaction interaction, int i, Sequence sequence) {
        if (interaction.schedule.hasErrors()) {
            interaction.schedule.errors.allErrors.each { FieldError error ->
                def code = error.code
                if (error.code.contains("endDateBeforeStartDate")) {
                    code = "sequence.endDatePhase${i + 1}.endDateBeforeStartDate"
                } else if (error.code.contains("nullable")) {
                    code = "sequence.startDatePhase${i + 1}.nullable"
                }
                sequence.errors.rejectValue("${error.field}Phase${i + 1}", code)
            }
        }
    }

    private def saveInteractions(List<Interaction> interactions, Sequence sequence) {
        if (!sequence.hasErrors()) {
            interactions.each { def interaction ->
                interaction.save(failOnError: true)
                interaction.schedule.save()
            }
        }
    }
}
