package org.tsaap.assignments

import grails.transaction.Transactional
import groovy.sql.Sql
import org.tsaap.attachement.Attachement
import org.tsaap.contracts.Contract
import org.tsaap.directory.User
import org.tsaap.lti.LmsAssignmentHelper

import javax.sql.DataSource

@Transactional
class AssignmentService {

    DataSource dataSource
    SequenceService sequenceService

    /**
     * Find all assignment owned by owner
     * @param owner the owner
     * @return the list of assignments
     */
    List<Assignment> findAllAssignmentsForOwner(User owner, def params) {
        Assignment.findAllByOwner(owner,params)
    }

    /**
     * Count assignments owned by owner
     * @param owner the owner
     * @return assignment count
     */
    Integer countAllAssignmentsForOwner(User owner) {
        Assignment.countByOwner(owner)
    }

    /**
     * Save an assignment
     * @param assignment the assignment to save
     *
     * @return the assignment saved or with errors
     */
    Assignment saveAssignment(Assignment assignment) {
        if (assignment.globalId == null) {
            assignment.globalId = UUID.randomUUID().toString()
        }
        assignment.lastUpdated = new Date();
        assignment.save()
        assignment
    }

    /**
     * Duplicate a assignment (create a copy of it, generating a new title, and assigning a
     * new owner to the copy)
     * @param user the user duplicating the assignment
     * @param assignment the course to duplicate
     * @return the duplicated course
     */
    Assignment duplicate (Assignment assignment, User user) {
        Contract.requires(assignment?.owner == user, USER__MUST__BE__ASSIGNMENT__OWNER)
        Assignment newAssignment = new Assignment();
        newAssignment.globalId = null
        newAssignment.owner = assignment.owner
        newAssignment.title = assignment.title + '-copy'
        Assignment duplicateAssignment = saveAssignment(newAssignment)

        assignment.sequences.each {sequence ->
            sequenceService.duplicateSequenceInAssignment(sequence, duplicateAssignment, user)
        }

        duplicateAssignment
    }


    /**
     * Delete assignment
     * @param assignment the assignment to delete
     * @param user the user performing deletion
     * @param flush force flush
     */
    def deleteAssignment(Assignment assignment, User user, boolean flush = true) {
        Contract.requires(assignment.owner == user, USER__MUST__BE__ASSIGNMENT__OWNER)
        deleteLmsAssignment(assignment)
        LearnerAssignment.executeUpdate("delete LearnerAssignment la where la.assignment = ?",[assignment])
        PeerGrading.executeUpdate("delete PeerGrading pg where pg.response in (from InteractionResponse resp where resp.interaction in (from Interaction i where i.sequence in (from Sequence s where s.assignment = ?)))",[assignment])
        InteractionResponse.executeUpdate("delete InteractionResponse resp where resp.interaction in (from Interaction i where i.sequence in (from Sequence s where s.assignment = ?))",[assignment])
        LearnerSequence.executeUpdate("delete LearnerSequence ls where ls.sequence in (from Sequence s where s.assignment = ?)",[assignment])
        Interaction.executeUpdate("delete Interaction i where i.sequence in (from Sequence s where s.assignment = ?)",[assignment])
        Attachement.executeUpdate("update Attachement attach set toDelete=true, statement=null where attach.statement in (select s.statement from Sequence s where s.assignment = ?)",[assignment])
        FakeExplanation.executeUpdate("delete FakeExplanation fe where fe.statement in (select s.statement from Sequence s where s.assignment = ?)",[assignment])
        Statement.executeUpdate("delete Statement st  where st in (select s.statement from Sequence s where s.assignment = ?)",[assignment])
        //sequences are deleted by cascade
        assignment.delete(flush: flush)
    }

    private void deleteLmsAssignment(Assignment assignment) {
        Sql sql = new Sql(dataSource)
        LmsAssignmentHelper lmsAssignmentHelper = new LmsAssignmentHelper()
        lmsAssignmentHelper.deleteLmsAssignment(sql, assignment.id)
        sql.close()
    }

    /**
     * Remove sequence from assignment
     * @param sequence the sequence to remove
     * @param assignment the assignment containing the sequence
     * @param user the user performing the operation
     * @return the modified assignment
     */
    Assignment removeSequenceFromAssignment(Sequence sequence, Assignment assignment, User user) {
        Contract.requires(assignment.owner == user, USER__MUST__BE__ASSIGNMENT__OWNER)
        Contract.requires(assignment.sequences?.contains(sequence), SEQUENCE__DOESN__T__BELONG__TO__ASSIGNMENT)
        setupAttachementToDeleteFlag(sequence)
        PeerGrading.executeUpdate("delete PeerGrading pg where pg.response in (from InteractionResponse resp where resp.interaction in (from Interaction i where i.sequence = ?))",[sequence])
        InteractionResponse.executeUpdate("delete InteractionResponse resp where resp.interaction in (from Interaction i where i.sequence  = ?)",[sequence])
        def query2 = LearnerSequence.where {
            sequence == sequence
        }
        query2.deleteAll()
        def query = Interaction.where {
            sequence == sequence
        }
        query.deleteAll()
        def statement = sequence.statement
        sequenceService.removeAllFakeExplanationFromStatement(statement)
        statement.delete(flush: true)
        // sequence is deleted by cascade
        assignment.lastUpdated = new Date()
        assignment.save()
        assignment
    }

    /**
     * Find all assignments for learner
     * @param user the learner
     * @param params sort and order params
     * @return the list of assignments the user is registered in
     */
    List<Assignment> findAllAssignmentsForLearner(User user, def params) {
        Assignment.executeQuery("select la.assignment from LearnerAssignment as la where la.learner = ? order by la.assignment.lastUpdated desc",[user], params)
    }

    /**
     * Count of assignments the user is registered in
     * @param user the user
     * @return the count of assignments the user is registered in
     */
    Long countAllAssignmentsForLearner(User user) {
        LearnerAssignment.countByLearner(user)
    }

    /**
     * Find assignment by global id
     * @param globalId the global id
     * @return the assignment if any
     */
    Assignment findAssignmentByGlobalId(String globalId) {
        Assignment.findByGlobalId(globalId)
    }

    /**
     * Register a user on an assignment
     * @param user the user to register
     * @param assignment the assignment
     * @return the learner assignment object or null if the user is the owner of the assignment
     */
    LearnerAssignment registerUserOnAssignment(User user, Assignment assignment) {
        LearnerAssignment learnerAssignment = null
        if (assignment.owner != user) {
            learnerAssignment = LearnerAssignment.findByLearnerAndAssignment(user, assignment)
            if (learnerAssignment == null) {
                learnerAssignment = new LearnerAssignment(learner: user, assignment: assignment)
                learnerAssignment.save()
            }
        }
        learnerAssignment
    }

    /**
     * Swap two sequences in an assignment
     * @param assignment the assignment
     * @param user the user performing the oeration
     * @param sequence1 the first sequence
     * @param sequence2 the second sequence
     * @return the assignement after swapping
     */
    Assignment swapSequences(Assignment assignment, User user, Sequence sequence1, Sequence sequence2) {
        Contract.requires(assignment.owner == user, USER__MUST__BE__ASSIGNMENT__OWNER)
        Contract.requires(assignment.sequences?.contains(sequence1), SEQUENCE__DOESN__T__BELONG__TO__ASSIGNMENT)
        Contract.requires(assignment.sequences?.contains(sequence2), SEQUENCE__DOESN__T__BELONG__TO__ASSIGNMENT)
        def rank1 = sequence1.rank
        sequence1.rank = sequence2.rank
        sequence2.rank = rank1
        sequence1.save()
        sequence2.save()
        assignment.lastUpdated = new Date()
        assignment.save()
        assignment
    }

    public static final String USER__MUST__BE__ASSIGNMENT__OWNER = "USER_MUST_BE_ASSIGNMENT_OWNER"
    public static final String SEQUENCE__DOESN__T__BELONG__TO__ASSIGNMENT = "SEQUENCE_DOESN_T_BELONG_TO_ASSIGNMENT"

    private void setupAttachementToDeleteFlag(Sequence sequence) {
        Attachement attachement = Attachement.findByStatement(sequence.statement)
        if (attachement) {
            attachement.toDelete = true
            attachement.statement = null
            attachement.save()
        }
    }


}
