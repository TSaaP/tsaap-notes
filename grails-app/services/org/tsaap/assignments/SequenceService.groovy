package org.tsaap.assignments

import grails.transaction.Transactional
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
     * Save an interaction
     * @param assignment the assignment to save
     *
     * @return the assignment saved or with errors
     */
    Assignment saveInteraction(Interaction interaction, User owner, Sequence sequence, Schedule schedule = null) {
        if (schedule) {
            schedule.save()
            schedule.interaction = interaction
        }
        interaction.owner = owner
        interaction.sequence = sequence
        interaction.lastUpdated = new Date();
        interaction.save()
        interaction
    }
}
