package org.tsaap.assignments

import org.tsaap.BootstrapTestService
import org.tsaap.directory.User
import spock.lang.*

/**
 *
 */
class SequenceServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    SequenceService sequenceService

    def setup() {
        bootstrapTestService.initializeTests()
    }


    void "test save valid statement"() {
        given:"a valid statement and a user"
        Statement statement = new Statement(title: "a title", content: "a content")
        User owner = bootstrapTestService.teacherJeanne

        when: "saving the statement"
        sequenceService.saveStatement(statement,owner)

        then: "the statement has no errors and properties correctly set"
        !statement.hasErrors()
        statement.id
        statement.owner == bootstrapTestService.teacherJeanne
        statement.lastUpdated
        statement.dateCreated

    }

    void "test save valid sequence"() {
        given: "a valid sequence, a user, an assignment and a statement"
        Sequence sequence = new Sequence(rank: 1)
        User owner = bootstrapTestService.teacherJeanne
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1

        when: "saving the sequence"
        sequenceService.saveSequence(sequence, owner,assignment,statement)

        then: "the sequence has no errors and properties set correctly"
        !sequence.hasErrors()
        sequence.id
        sequence.owner == owner
        sequence.assignment == assignment
        sequence.statement == statement
    }

    void "test add a valid sequence to an assignment"() {
        given: "an assignment and a statement"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1

        when: "when adding a first sequence"
        Sequence sequence = sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement)

        then: "the sequence has no errors"
        !sequence.hasErrors()

        and: "the assignment has one sequence"
        assignment.sequences.size() == 1

        and: "the rank of the last sequence is 1"
        assignment.lastSequence.rank == 1

        when: "adding a new sequence"
        Statement statement2 = bootstrapTestService.statement2
        sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement2)

        then:"the sequence has no errors"
        !sequence.hasErrors()

        and: "the assignment has two sequences"
        assignment.sequences.size() == 2

        and: "the rank of the last sequence is 2"
        assignment.lastSequence.rank == 2

    }

    void "test add an invalid sequence to an assignment"() {
        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1
        sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement)

        and: "an invalid statement"
        Statement statement2 =new Statement(title: null, content: "a content", owner: null)

        when: "adding a sequence with the invalid statement"
        Sequence sequence = sequenceService.addSequenceToAssignment(assignment,assignment.owner,statement2)

        then: "assignment has only one sequence"
        assignment.sequences.size() == 1
        assignment.lastSequence.rank == 1

        and: "sequence has errors coming from the statement"
        sequence.hasErrors()
        println ">>>>>>>>>> ${sequence.errors.allErrors}"

    }

}
