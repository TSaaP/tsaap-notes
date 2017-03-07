package org.tsaap.assignments

import org.tsaap.BootstrapTestService
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.directory.User
import spock.lang.*


class AssignmentServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    AssignmentService assignmentService
    SequenceService sequenceService

    User teacher
    User anOtherUser


    def setup() {
        bootstrapTestService.initializeTests()
        teacher = bootstrapTestService.teacherJeanne
        anOtherUser = bootstrapTestService.learnerMary
    }


    void "test save valid assignments"() {
        given: "A valid assignment"
        Assignment assignment = new Assignment(title: "an assignment", owner: teacher)

        when: "saving the assignment"
        Assignment savedAssignment = assignmentService.saveAssignment(assignment)

        then: "the save assignement has no errors"
        !savedAssignment.hasErrors()

        and: "its properties are correctly set"
        savedAssignment.title == assignment.title
        savedAssignment.owner == teacher
        savedAssignment.id != null
        savedAssignment.dateCreated != null
        savedAssignment.globalId != null
    }

    void "test save fail with invalid assignments"() {
        given: "An assignment without owner"
        Assignment assignment = new Assignment(title: "an assignment", owner: null)

        when: "saving the assignment"
        Assignment savedAssignment = assignmentService.saveAssignment(assignment)

        then: "the  assignement has  errors and no id"
        savedAssignment.hasErrors()
        savedAssignment.id == null

        when: "assignment has owner but blank title and trying to save"
        assignment = new Assignment(title: "", owner: teacher)
        savedAssignment = assignmentService.saveAssignment(assignment)

        then: "the  assignement has  errors and no id"
        savedAssignment.hasErrors()
        savedAssignment.id == null
    }


    void "test delete assignment without schedule"() {
        given: "an assignment without schedule"
        Assignment assignment = assignmentService.saveAssignment(new Assignment(title: "an assignment", owner: teacher))

        when: "deleting assignment is not performed by the owner"
        assignmentService.deleteAssignment(assignment, anOtherUser)

        then: "an exception is thrown"
        def exception = thrown(ConditionViolationException)
        exception.message == AssignmentService.USER__MUST__BE__ASSIGNMENT__OWNER

        and: "the assignment is not deleted"
        Assignment.findById(assignment.id)

        when: "deleting assignment is  performed by the owner"
        assignmentService.deleteAssignment(assignment, teacher)

        then: "the assignment is deleted"
        Assignment.findById(assignment.id) == null
    }


    void "test delete assignment with sequences"() {
        given: "an ssignment with sequences"
        Assignment assignment = bootstrapTestService.assignment2With2Sequences
        def sequence1 = assignment.sequences[0]
        def sequence2 = assignment.sequences[1]

        when: "deleting assignment is  performed by the owner"
        assignmentService.deleteAssignment(assignment, teacher)

        then: "the assignment and the sequences are deleted"
        Assignment.findById(assignment.id) == null
        Sequence.findById(sequence1.id) == null
        Sequence.findById(sequence2.id) == null

        and: "the statements has been deleted from the database"
        Statement.findById(sequence1.statementId) == null
        Statement.findById(sequence2.statementId) == null

    }

    void "test delete assignment with sequences and interactions"() {
        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement1 = bootstrapTestService.statement1
        Statement statement2 = bootstrapTestService.statement2

        and: "a sequence added to the assignment with interactions"
        def interactions = [bootstrapTestService.responseSubmissionInteraction, bootstrapTestService.evaluationInteraction]
        Sequence sequence1 = sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement1, interactions)
        Sequence sequence2 = sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement2)
        def schedule1 = bootstrapTestService.responseSubmissionInteraction.schedule
        def schedule2 = bootstrapTestService.evaluationInteraction.schedule
        schedule1 && schedule2

        when: "deleting assignment is  performed by the owner"
        assignmentService.deleteAssignment(assignment, teacher)

        then: "the assignment and the sequences are deleted"
        Assignment.findById(assignment.id) == null
        Sequence.findById(sequence1.id) == null
        Sequence.findById(sequence2.id) == null

        and: "the statements has been deleted from the database"
        Statement.findById(sequence1.statementId) == null
        Statement.findById(sequence2.statementId) == null

        and: "the interactions have been deleted from the datable"
        Interaction.findById(bootstrapTestService.responseSubmissionInteraction.id) == null
        Interaction.findById(bootstrapTestService.evaluationInteraction.id) == null

        when: "fetching schedule with new session"
        def fetchSchedule1, fetchSchedule2
        Schedule.withNewSession {
            fetchSchedule1 = Schedule.findById(schedule1.id)
            fetchSchedule2 = Schedule.findById(schedule2.id)
        }

        then: "the fetch schedules are null"
        fetchSchedule2 == null
        fetchSchedule1 == null

    }


    void "test remove sequence from an assignment with interactions"() {
        given: "an assignment with a sequence"
        Assignment assignment = bootstrapTestService.assignment1
        Statement statement1 = bootstrapTestService.statement1
        Statement statement2 = bootstrapTestService.statement2

        and: "a sequence added to the assignment with interactions"
        def interactions = [bootstrapTestService.responseSubmissionInteraction, bootstrapTestService.evaluationInteraction]
        Sequence sequence1 = sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement1, interactions)
        Sequence sequence2 = sequenceService.addSequenceToAssignment(assignment, assignment.owner, statement2)


        when: "removing the first sequence"
        assignmentService.removeSequenceFromAssignment(sequence1, assignment, assignment.owner)

        then: "the assignment has only one sequence"
        Sequence.findById(sequence1.id) == null
        assignment.sequences.size() == 1

        and: "the rank of the last sequence is always 2"
        assignment.lastSequence.rank == 2

        and: "the sequence is deleted from the database"
        !Sequence.findById(sequence1.id)

        and: "the interactions are deleted from the database"
        Interaction.findById(bootstrapTestService.responseSubmissionInteraction.id) == null
        Interaction.findById(bootstrapTestService.evaluationInteraction.id) == null

        and: "the statement been deleted from the database"
        Statement.findById(statement1.id) == null
    }

    void "test remove sequence from an assignment"() {
        given: "an assignment with 2 sequences"
        Assignment assignment = bootstrapTestService.assignment2With2Sequences

        and: "the first sequence"
        Sequence sequence = assignment.sequences[0]

        when: "removing the first sequence"
        assignmentService.removeSequenceFromAssignment(sequence, assignment, assignment.owner)

        then: "the assignment has only one sequence"
        assignment.sequences.size() == 1

        and: "the rank of the last sequence is always 2"
        assignment.lastSequence.rank == 2

        and: "the sequence is deleted from the database"
        !Sequence.findById(sequence.id)

        and: "the statement has  been deleted from the database"
        Statement.findById(sequence.statementId) == null
    }


    void "test swap 2 sequences in an assignment"() {
        given: "an assigment with 2 sequences"
        Assignment assignment = bootstrapTestService.assignment2With2Sequences
        Sequence sequence1 = assignment.sequences[0]
        Integer rank1 = sequence1.rank
        Sequence sequence2 = assignment.sequences[1]
        Integer rank2 = sequence2.rank

        when: "swapping 2 sequences"
        assignmentService.swapSequences(assignment, assignment.owner, sequence1, sequence2)

        then: "ranks are swapped"
        sequence1.rank == rank2
        sequence2.rank == rank1

        and: "order are swapped"
        List<Sequence> listSequence = assignment.sequences
        assignment.sequences[0] == sequence2
        assignment.sequences[1] == sequence1
    }

    void "test the no registration of a teacher on an assignment"() {
        given: "an assignment"
        Assignment assignment = bootstrapTestService.assignment1

        expect:"registering the owner of the assignment return no registration"
        assignmentService.registerUserOnAssignment(assignment.owner,assignment) == null

    }

    void "test the registration of a user"() {
        given: "two assignments"
        Assignment assignment1 = bootstrapTestService.assignment1
        Assignment assignment2 = bootstrapTestService.assignment2With2Sequences

        and: "learner "
        User paul = bootstrapTestService.learnerPaul

        when:"registering the learner on the assignments"
        LearnerAssignment la1 = assignmentService.registerUserOnAssignment(paul,assignment1)
        LearnerAssignment la2 = assignmentService.registerUserOnAssignment(paul,assignment2)

        then: "the resulting learner assignments are OK"
        la1.id  && la1.assignment == assignment1  && la1.learner == paul
        la2.id  && la2.assignment == assignment2  && la2.learner == paul

        when: "searching for learner assignments"
        List<Assignment> assignmentList = assignmentService.findAllAssignmentsForLearner(paul, [offset:0, max:5])

        then:"the list contains the two expected assignments"
        assignmentList.size() == 2
        assignmentService.countAllAssignmentsForLearner(paul) == 2 as Long
        assignmentList.contains(assignment1)
        assignmentList.contains(assignment2)

    }

    void "test two registration attempts gives only one registration"() {
        given: "an assignment"
        Assignment assignment = bootstrapTestService.assignment1

        and: "learner "
        User paul = bootstrapTestService.learnerPaul

        when:"registering twice the learner on the assignment"
        LearnerAssignment la1 = assignmentService.registerUserOnAssignment(paul,assignment)
        LearnerAssignment la2 = assignmentService.registerUserOnAssignment(paul,assignment)

        then: "only one registration is performed"
        la1
        la2 == la1
        assignmentService.countAllAssignmentsForLearner(paul) == 1 as Long

    }

}
