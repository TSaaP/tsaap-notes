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



}
