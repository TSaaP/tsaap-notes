package org.tsaap.assignments

import org.tsaap.BootstrapTestService
import org.tsaap.assignments.statement.ChoiceInteractionType
import org.tsaap.assignments.statement.ChoiceSpecification
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.directory.User
import spock.lang.*

/**
 *
 */
class SequenceServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    SequenceService sequenceService
    AssignmentService assignmentService

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

    void "test create interactions for a face to face sequence with short process"() {

        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)

        expect: "the sequence is by default in face to face execution context"
        sequence.executionIsFaceToFace()

        when: "create interactions with no provision of explanations"
        def interactions = sequenceService.createInteractionsForSequence(sequence, false)

        then: "there are 3 interactions created"
        interactions.size() == 3

        and: "first interaction is a response submission interaction"
        interactions[0].isResponseSubmission()

        and: "the first interaction specification is set properly"
        interactions[0].interactionSpecification.studentsProvideExplanation == false
        interactions[0].interactionSpecification.studentsProvideConfidenceDegree == false

    }

    void "test create interactions for a face to face sequence with default process"() {

        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)

        expect: "the sequence is by default in face to face execution context"
        sequence.executionIsFaceToFace()

        when: "create interactions with no provision of explanations"
        def interactions = sequenceService.createInteractionsForSequence(sequence, true,3)

        then: "there are  3 interactions created"
        interactions.size() == 3

        and: "first interaction is a response submission interaction"
        interactions[0].isResponseSubmission()

        and: "the first interaction specification is set properly"
        interactions[0].interactionSpecification.studentsProvideExplanation == true
        interactions[0].interactionSpecification.studentsProvideConfidenceDegree == true

        and: "the second interaction is an evaluation interaction and is set properly"
        interactions[1].isEvaluation()
        interactions[1].interactionSpecification.responseToEvaluateCount == 3

    }

    void "test create interactions for a blended sequence with default process"() {

        given: "an assignment with a blended sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)
        sequence.executionContext = ExecutionContextType.Blended.name()

        expect: "the sequence is in a blended execution context"
        sequence.executionIsBlended()

        when: "create interactions with no provision of explanations"
        def interactions = sequenceService.createInteractionsForSequence(sequence, true,3)

        then: "there are  3 interactions created"
        interactions.size() == 3

        and: "first interaction is a response submission interaction"
        interactions[0].isResponseSubmission()

        and: "the first interaction specification is set properly"
        interactions[0].interactionSpecification.studentsProvideExplanation == true
        interactions[0].interactionSpecification.studentsProvideConfidenceDegree == true

        and: "the second interaction is an evaluation interaction and is set properly"
        interactions[1].isEvaluation()
        interactions[1].interactionSpecification.responseToEvaluateCount == 3

    }

    void "test create interactions for a distance sequence with default process"() {

        given: "an assignment with a distance sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement = bootstrapTestService.statement1
        Sequence sequence = sequenceService.createAndAddSequenceToAssignment(assignment, assignment.owner, statement)
        sequence.executionContext = ExecutionContextType.Distance.name()

        expect: "the sequence is in a distance execution context"
        sequence.executionIsDistance()

        when: "create interactions with no provision of explanations"
        def interactions = sequenceService.createInteractionsForSequence(sequence, true,3)

        then: "there are  3 interactions created"
        interactions.size() == 3

        and: "first interaction is a response submission interaction"
        interactions[0].isResponseSubmission()

        and: "the first interaction specification is set properly"
        interactions[0].interactionSpecification.studentsProvideExplanation == true
        interactions[0].interactionSpecification.studentsProvideConfidenceDegree == true

        and: "the second interaction is an evaluation interaction and is set properly"
        interactions[1].isEvaluation()
        interactions[1].interactionSpecification.responseToEvaluateCount == 3

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

    void "test obtaining active interaction for a learner in face to face process"() {
        given: "a sequence with eval interaction as active interaction"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.activeInteraction = sequence.evaluationInteraction

        and: "a learner"
        def mary = bootstrapTestService.learnerMary

        expect:"sequence is face to face"
        sequence.executionIsFaceToFace()

        and: "the active interaction for the learner is the active interaction of the sequence"
        sequence.activeInteractionForLearner(mary) == sequence.activeInteraction

    }

    void "test obtaining active interaction for a learner in asynchronous process"() {
        given: "a sequence with no active interaction"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.activeInteraction = null
        sequence.executionContext = ExecutionContextType.Blended.name()

        and: "a learner"
        def mary = bootstrapTestService.learnerMary

        expect:"sequence is not face to face"
        !sequence.executionIsFaceToFace()

        and: "the active interaction for the learner is null"
        sequence.activeInteractionForLearner(mary) == null

        when: "the sequence has started"
        sequence.activeInteraction = sequence.responseSubmissionInteraction

        then: "the active interaction for the learner is response submission interaction"
        sequence.activeInteractionForLearner(mary) == sequence.responseSubmissionInteraction

    }

    void "test update active interaction for learner"() {
        given: "a blended sequence with response interaction as active interaction"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.activeInteraction = sequence.evaluationInteraction
        sequence.executionContext = ExecutionContextType.Blended.name()

        and: "a learner embedded in phase 1"
        def mary = bootstrapTestService.learnerMary
        def rankPhase = 1

        when: "updating the active interaction"
        sequence.updateActiveInteractionForLearner(mary, rankPhase)

        then: "the active interaction is eval interaction"
        sequence.activeInteractionForLearner(mary) == sequence.evaluationInteraction

        when: "rank phase is 2"
        rankPhase = 2

        and: "updating the active interaction"
        sequence.updateActiveInteractionForLearner(mary, rankPhase)

        then: "the active interaction is eval interaction"
        sequence.activeInteractionForLearner(mary) == sequence.readInteraction
    }

    void "test update all results is not granted for face to face sequence"() {
        given: "a sequence"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]

        expect: "the sequence is face to face"
        sequence.executionIsFaceToFace()

        when: "the owner of the sequence is triggering the update of all results"
        sequenceService.updateAllResults(sequence, sequence.owner)

        then: "an exception is thrown"
        thrown(ConditionViolationException)

        when: "a student of the sequence is triggering the update of all results"
        def mary = bootstrapTestService.learnerMary
        assignmentService.registerUserOnAssignment(mary, sequence.assignment)
        sequenceService.updateAllResults(sequence, mary )

        then: "an exception is thrown"
        thrown(ConditionViolationException)

    }

    void "test update all results is  granted to owner for blended sequence"() {
        given: "a sequence"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.executionContext = ExecutionContextType.Blended.name()

        expect: "the sequence is face to face"
        sequence.executionIsBlended()

        when: "the owner of the sequence is triggering the update of all results"
        sequenceService.updateAllResults(sequence, sequence.owner)

        then: "no exception is thrown"
        noExceptionThrown()

        when: "a student of the sequence is triggering the update of all results"
        def mary = bootstrapTestService.learnerMary
        assignmentService.registerUserOnAssignment(mary, sequence.assignment)
        sequenceService.updateAllResults(sequence, mary )

        then: "an exception is thrown"
        thrown(ConditionViolationException)

    }

    void "test update all results is  granted to owner and learner for distance sequence"() {
        given: "a sequence"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.executionContext = ExecutionContextType.Distance.name()

        expect: "the sequence is face to face"
        sequence.executionIsDistance()

        when: "the owner of the sequence is triggering the update of all results"
        sequenceService.updateAllResults(sequence, sequence.owner)

        then: "no exception is thrown"
        noExceptionThrown()

        when: "a student of the sequence is triggering the update of all results"
        def mary = bootstrapTestService.learnerMary
        assignmentService.registerUserOnAssignment(mary, sequence.assignment)
        sequenceService.updateAllResults(sequence, mary)

        then: "no exception is thrown"
        noExceptionThrown()

    }

    void "test adding a fake explanation"() {
        given: "a statement"
        Statement statement = bootstrapTestService.statement1

        when: "adding a fake explanation to the statement"
        FakeExplanation fakeExplanation = sequenceService.addFakeExplanationToStatement("a fake explanation", statement, statement.owner)

        then: "the fake explanation has no errors"
        !fakeExplanation.hasErrors()

        and:"the statement has one fake explanation"
        statement.fakeExplanations[0] == fakeExplanation

    }

    void "test start sequence in blended or distance context"() {
        given: "a sequence"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]

        when: "the sequence is face to face"
        sequence.executionContext = ExecutionContextType.FaceToFace.name()

        and: "the owner try to trigger start of a blended or distance sequence"
        sequenceService.startSequenceInBlendedOrDistanceContext(sequence, sequence.owner)

        then:"an exception is thrown"
        thrown(ConditionViolationException)

        when:"the sequence is blended or distance"
        sequence.executionContext = ExecutionContextType.Blended.name()

        and: "a user not owner of the sequence try to trigger start of a blended or distance sequence"
        sequenceService.startSequenceInBlendedOrDistanceContext(sequence, bootstrapTestService.learnerMary)

        then:"an exception is thrown"
        thrown(ConditionViolationException)

        when: "the owner of the sequence try to trigger start of a blended or distance sequence"
        sequenceService.startSequenceInBlendedOrDistanceContext(sequence,sequence.owner)

        then:"the sequence is started"
        sequence.state == StateType.show.name()
        sequence.activeInteraction == sequence.readInteraction

        and: "the owner try to trigger start of a blended or distance sequence"
        sequenceService.startSequenceInBlendedOrDistanceContext(sequence, sequence.owner)

        then:"the sequence is started"
        sequence.state == StateType.show.name()
        sequence.activeInteraction == sequence.readInteraction

    }

    void "test stop sequence"() {
        given: "a sequence"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]

        when: "a user not owner of the sequence try to trigger start of a blended or distance sequence"
        sequenceService.startSequenceInBlendedOrDistanceContext(sequence, bootstrapTestService.learnerMary)

        then:"an exception is thrown"
        thrown(ConditionViolationException)

        when: "the owner of the sequence try to stop the sequence"
        sequenceService.stopSequence(sequence,sequence.owner)

        then:"the sequence is started"
        sequence.state == StateType.afterStop.name()
        sequence.interactions.each {
            it.state == StateType.afterStop.name()
        }

    }

    void "test sequence duplication"() {
        given: "a sequence and its statement"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.sequences[0]
        sequence.state = StateType.show.name()
        sequence.executionContext = ExecutionContextType.Blended.name()
        Statement statement = sequence.statement

        and: "the sequence statement has explanations"
        statement.expectedExplanation = "expected explanation"
        FakeExplanation fe1 = sequenceService.addFakeExplanationToStatement("a fake explanation", statement, statement.owner)
        FakeExplanation fe2 = sequenceService.addFakeExplanationToStatement("a fake explanation 2", statement, statement.owner,2)

        and: "a new assignment"
        Assignment newAssignment = assignmentService.saveAssignment(new Assignment(owner: assignment.owner, title: "new assignment"))

        when: "duplicate the sequence"
        Sequence dupSeq = sequenceService.duplicateSequenceInAssignment(sequence, newAssignment, assignment.owner)

        then: "the duplicate sequence is set properly"
        dupSeq.id
        !dupSeq.hasErrors()
        dupSeq.assignment == newAssignment
        dupSeq.owner == sequence.owner
        dupSeq.state == StateType.beforeStart.name() // it's a new sequence
        dupSeq.executionContext == ExecutionContextType.FaceToFace.name() // default value for a new sequence
        dupSeq.statement.id != sequence.statement.id
        dupSeq.statement.title == statement.title
        dupSeq.statement.owner == statement.owner
        dupSeq.statement.expectedExplanation == statement.expectedExplanation
        dupSeq.statement.questionType == statement.questionType
        dupSeq.statement.content == statement.content
        dupSeq.statement.choiceSpecification == statement.choiceSpecification
        dupSeq.statement.parentStatement == statement

        when: "getting the duplicate sequence fake explanations"
        def explanations = dupSeq.statement.fakeExplanations

        then: "the fake explanations are OK"
        explanations.size() == 2
        explanations[0].statement == dupSeq.statement
        explanations[0].content == fe1.content
        explanations[0].author == fe1.author
        explanations[0].id != fe1.id
        explanations[0].correspondingItem == fe1.correspondingItem
        explanations[1].statement == dupSeq.statement
        explanations[1].content == fe2.content
        explanations[1].author == fe2.author
        explanations[1].id != fe2.id
        explanations[1].correspondingItem == fe2.correspondingItem




    }
}
