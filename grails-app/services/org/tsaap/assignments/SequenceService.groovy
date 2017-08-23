package org.tsaap.assignments

import grails.transaction.Transactional
import org.springframework.validation.FieldError
import org.tsaap.assignments.interactions.EvaluationSpecification
import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import org.tsaap.attachement.Attachement
import org.tsaap.attachement.AttachementService
import org.tsaap.contracts.Contract
import org.tsaap.directory.User

@Transactional
class SequenceService {

    AttachementService attachementService

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
    Sequence createAndAddSequenceToAssignment(Assignment assignment, User user, Statement statement) {
        Contract.requires(assignment.owner == user, AssignmentService.USER__MUST__BE__ASSIGNMENT__OWNER)
        def rank = assignment.lastSequence ? assignment.lastSequence.rank + 1 : 1
        Sequence sequence = new Sequence(rank: rank)

        addSequenceToAssignment(user, assignment, sequence, statement)
    }

    /**
     * Add a sequence to an assignment.
     *
     * @param user the user adding the sequence
     * @param assignment the assignment to add the sequence
     * @param sequence the sequence
     * @param statement the statement the sequence is based on
     *
     * @return the sequence
     */
    private Sequence addSequenceToAssignment(User user, Assignment assignment, Sequence sequence, Statement statement) {
        saveOrUpdateStatement(statement, sequence)
        saveSequence(sequence, user, assignment, statement)
        updateAssignmentLastUpdated(sequence, assignment)

        sequence
    }

    /**
     * Add interactions to sequence
     * @param sequence the sequence
     * @param user the user performing the action
     * @param interactionsToAdd the interactions to add
     * @return
     */
    Sequence addSequenceInteractions(Sequence sequence, User user, List<Interaction> interactionsToAdd = null) {
        Contract.requires(sequence.owner == user, AssignmentService.USER__MUST__BE__ASSIGNMENT__OWNER)
        validateInteractions(interactionsToAdd, user, sequence)
        saveInteractions(interactionsToAdd, sequence)
        updateAssignmentLastUpdated(sequence, sequence.assignment)

        sequence
    }

    /**
     * Create interactions for sequence
     * @param sequence the sequence
     * @param studentsProvideExplanation flag to indicate if students shoud provide explanations
     * @param responseToEvatuateCount flag to indicate how many response students have to grade
     * @return the list of created interactions
     */
    List<Interaction> createInteractionsForSequence(Sequence sequence,
                                                        boolean studentsProvideExplanation = true,
                                                        int responseToEvaluateCount = EvaluationSpecification.MAX_RESPONSE_TO_EVALUATE_COUNT) {
        List<Interaction> interactions

        ResponseSubmissionSpecification responseSpec = new ResponseSubmissionSpecification()
        responseSpec.studentsProvideExplanation = studentsProvideExplanation
        responseSpec.studentsProvideConfidenceDegree = studentsProvideExplanation

        EvaluationSpecification evalSpec = new EvaluationSpecification()
        evalSpec.responseToEvaluateCount = responseToEvaluateCount

        if (sequence.executionIsFaceToFace() && !studentsProvideExplanation) {
            interactions = getInteractionsToShortProcess(
                    responseSpec
            )
        } else {
            interactions = getInteractionsToDefaultProcess(
                    responseSpec,
                    evalSpec
            )
        }
        interactions
    }


    private List<Interaction> getInteractionsToDefaultProcess(ResponseSubmissionSpecification responseSpec, EvaluationSpecification evalSpec) {
        List<Interaction> interactions = []

        Interaction interaction1 = new Interaction(
                interactionType: InteractionType.ResponseSubmission.name(),
                rank: 1,
                specification: responseSpec.jsonString
        )
        interactions.add(interaction1)


        Interaction interaction2 = new Interaction(
                interactionType: InteractionType.Evaluation.name(),
                rank: 2,
                specification: evalSpec.jsonString
        )
        interactions.add(interaction2)


        Interaction interaction3 = new Interaction(
                interactionType: InteractionType.Read.name(),
                rank: 3,
                specification: Interaction.EMPTY_SPECIFICATION
        )
        interactions.add(interaction3)


        interactions
    }

    private List<Interaction> getInteractionsToShortProcess(ResponseSubmissionSpecification responseSpec) {
        List<Interaction> interactions = []

        Interaction interaction1 = new Interaction(
                interactionType: InteractionType.ResponseSubmission.name(),
                rank: 1,
                specification: responseSpec.jsonString
        )
        interactions.add(interaction1)

        Interaction interaction3 = new Interaction(
                interactionType: InteractionType.Read.name(),
                rank: 2,
                specification: Interaction.EMPTY_SPECIFICATION
        )
        interactions.add(interaction3)


        interactions
    }


    /**
     * Duplicate a sequence in an assignment (without interactions)
     * @param sequence the sequence to duplicate
     * @param duplicatedAssignment the target assignment
     * @param user the user performing the operation
     * @return the duplicated sequence
     */
    Sequence duplicateSequenceInAssignment(Sequence sequence, Assignment duplicatedAssignment, User user) {
        Sequence duplicatedSequence = new Sequence(
                rank: sequence.rank,
                owner: sequence.owner,
                assignment: duplicatedAssignment,
                state: sequence.state
        )

        Statement duplicatedStatement = new Statement(
                title: sequence.statement.title,
                content: sequence.statement.content,
                choiceSpecification: sequence.statement.choiceSpecification,
                questionType: sequence.statement.questionType,
                owner: sequence.statement.owner,
                parentStatement: sequence.statement
        )

        addSequenceToAssignment(user, duplicatedAssignment, duplicatedSequence, duplicatedStatement)

        Attachement attachement = sequence.statement.attachment
        if (attachement) {
            Attachement duplicatedAttachement = attachementService.duplicateAttachment(attachement)
            attachementService.addStatementToAttachment(duplicatedStatement,duplicatedAttachement)
        }

        duplicatedSequence
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

    /**
     * Save or update a statement
     * @param statement
     * @param sequence
     */
    def saveOrUpdateStatement(Statement statement, Sequence sequence) {
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

    /**
     * Add a fake explanation to a statement
     * @param content the content of the fake explanation
     * @param statement the given statement the explanation is added to
     * @param user the user performing the operation
     * @return fakeExplanation the created fake explanation
     */
    FakeExplanation addFakeExplanationToStatement(String content, Statement statement, User user, Integer correspondingItem = null) {
        Contract.requires(statement.owner == user, USER_MUST_BE_STATEMENT_OWNER)
        FakeExplanation fakeExplanation = new FakeExplanation(
                author: user,
                statement: statement,
                content: content,
                correspondingItem: correspondingItem
        )
        fakeExplanation.save()
        fakeExplanation
    }

    /**
     * Update fake explanations of the given statement
     * @param fakeExplanations the list of fake explanations to update
     * @param statement the given statement
     * @param user the user performing the operation
     */
    def updateFakeExplanationListToStatement(List<FakeExplanationDto> fakeExplanationDtos, Statement statement, User user) {
        Contract.requires(statement.owner == user, USER_MUST_BE_STATEMENT_OWNER)
        removeAllFakeExplanationFromStatement(statement)
        fakeExplanationDtos.each {
            def fe = addFakeExplanationToStatement(it.content, statement,user, it.correspondingItem)
            if (fe.hasErrors()) {
                log.error(fe.errors)
            }
        }
    }

    /**
     * Remove all fake explanations from a given statement
     * @param statement the statement
     */
    void removeAllFakeExplanationFromStatement(Statement statement) {
        def query = FakeExplanation.where {
            statement == statement
        }
        query.deleteAll()
    }

    /**
     * Find all fake explanations for the given statement
     * @param statement the statement
     * @param user the user performing the operation
     * @return the list of fake explanations
     */
    List<FakeExplanation> findAllFakeExplanationsForStatement(Statement statement, User user) {
        Contract.requires(statement.owner == user, USER_MUST_BE_STATEMENT_OWNER)
        FakeExplanation.findAllByStatement(statement)
    }



    private def updateInteractions(Sequence sequence) {
        sequence.interactions.eachWithIndex { def interaction, int i ->
            interaction.save(failOnError: true)
        }
    }

    private def validateInteractions(List<Interaction> interactions, User user, Sequence sequence) {
        interactions.eachWithIndex { def interaction, int i ->
            interaction.owner = user
            interaction.sequence = sequence
        }
    }


    private def saveInteractions(List<Interaction> interactions, Sequence sequence) {
        if (!sequence.hasErrors()) {
            interactions.each { def interaction ->
                interaction.save(failOnError: true)
            }
        }
    }

    private static final String USER_MUST_BE_STATEMENT_OWNER = "user must be the statement owner"


}
