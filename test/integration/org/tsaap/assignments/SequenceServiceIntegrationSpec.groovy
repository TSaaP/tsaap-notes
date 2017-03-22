package org.tsaap.assignments

import org.tsaap.BootstrapTestService
import org.tsaap.assignments.statement.ChoiceInteractionType
import org.tsaap.assignments.statement.ChoiceSpecification
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
        Statement statement = new Statement(title: "a title", content: "a content", questionType: QuestionType.OpenEnded)
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
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)

        then: "the sequence has no errors"
        !sequence.hasErrors()

        and: "the assignment has one sequence"
        assignment.sequences.size() == 1

        and: "the rank of the last sequence is 1"
        assignment.lastSequence.rank == 1

        when: "adding a new sequence"
        Statement statement2 = bootstrapTestService.statement2
        sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement2)

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
        sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)

        and: "an invalid statement"
        Statement statement2 =
            new Statement(title: null, content: "a content", owner: null, questionType: QuestionType.OpenEnded)

        when: "adding a sequence with the invalid statement"
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment,assignment.owner,statement2)

        then: "assignment has only one sequence"
        assignment.sequences.size() == 1
        assignment.lastSequence.rank == 1

        and: "sequence has errors coming from the statement"
        sequence.hasErrors()
        println ">>>>>>>>>> ${sequence.errors.allErrors}"

    }

    void "test update statement of a sequence"() {
        given: "an assignment and a statement"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1

        and: "a sequence added to the assignment with interaction"
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)

        when: "statement is modified"
        statement.title = "new title"

        and: "update is triggered on the sequence"
        sequenceService.saveOrUpdateStatement(statement, sequence)

        then: "the statement title of the sequence is modified"
        Statement.findById(sequence.statementId).title == "new title"
    }

    void "test obtaining statement specification"() {
        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement5

        and: "a sequence added to the assignment with interactions"
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)

        when:"getting the choiceSpecification spec from the statement"
        ChoiceSpecification choiceSpecification = sequence.statement.choiceSpecificationObject

        then:"the specification is OK"
        choiceSpecification.choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()
        choiceSpecification.itemCount == 5
        choiceSpecification.expectedChoiceList.size() == 3
        choiceSpecification.totalScoreFromExpectedChoice == 100f
    }

    void "test obtaining active interaction "() {
        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1

        and: "a sequence added to the assignment with interactions"
        def interactions = [bootstrapTestService.responseSubmissionInteraction, bootstrapTestService.evaluationInteraction]
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)
        sequenceService.addSequenceInteractions(sequence,assignment.owner, interactions)

        when: "asking the active interaction"
        def interaction = sequence.activeInteraction

        then: "the first interaction is given"
        interaction == bootstrapTestService.responseSubmissionInteraction

    }
}
