package org.tsaap.assignments.interactions

import grails.transaction.Transactional
import org.tsaap.BootstrapService
import org.tsaap.assignments.*
import org.tsaap.contracts.Contract
import org.tsaap.directory.User

@Transactional
class InteractionService {

    BootstrapService bootstrapService

    /**
     * Start an interaction
     * @param interaction the interaction to start
     * @return the started interaction
     */
    Interaction startInteraction(Interaction interaction, User user) {
        Contract.requires(interaction.owner == user, ONLY_OWNER_CAN_START_INTERACTION)
        Sequence sequence = interaction.sequence
        interaction.state = StateType.show.name()
        sequence.state = StateType.show.name()
        interaction.save()
        sequence.save()
        interaction
    }

    /**
     * Stop an interaction
     * @param interaction the interaction to stop
     * @return the stopped interaction
     */
    Interaction stopInteraction(Interaction interaction, User user) {
        Contract.requires(interaction.owner == user, ONLY_OWNER_CAN_STOP_INTERACTION)
        Contract.requires(interaction.state == StateType.show.name(), INTERACTION_IS_NOT_STARTED)
        interaction.doAfterStop()
        updateActiveInteractionInSequence(interaction)
        interaction
    }

    /**
     * Save an interaction response
     * @param response the response to save
     * @return the saved response with the updated score
     */
    InteractionResponse saveInteractionResponse(InteractionResponse response) {
        Contract.requires(response.firstAttemptIsSubmitable(response.learner) || response.secondAttemptIsSubmitable(response.learner), INTERACTION_CANNOT_RECEIVE_RESPONSE)
        Contract.requires(response.learner.isRegisteredInAssignment(response.assignment()),
                LEARNER_NOT_REGISTERED_IN_ASSIGNMENT)
        if (response.interaction.sequence.statement.hasChoices()) {
            response.updateScore()
        }
        response.save()
        response
    }

    /**
     * find or create peer grading from user on response
     * @param grader the grader
     * @param response the response
     * @return the peer grading object
     */
    PeerGrading peerGradingFromUserOnResponse(User grader, InteractionResponse response, Float grade) {
        Contract.requires(grader.isRegisteredInAssignment(response.assignment()), "$LEARNER_NOT_REGISTERED_IN_ASSIGNMENT : ${grader.id} - ${response.assignment().id}")
        PeerGrading peerGrading = PeerGrading.findByResponseAndGrader(response, grader)
        if (!peerGrading) {
            peerGrading = new PeerGrading(grader: grader, response: response, grade: grade)
        } else {
            peerGrading.grade = grade
        }
        peerGrading.save()
        peerGrading
    }

    /**
     * Update the mean grade of a given response
     * @param response the response
     * @return the response with mean grade updated
     */
    InteractionResponse updateMeanGradeOfResponse(InteractionResponse response) {
        def query = PeerGrading.where {
            response == response && grade != -1
        }.projections {
            avg('grade')
        }
        def meanGrade = query.find() as Float
        response.meanGrade = meanGrade
        response.save()
        response
    }

    /**
     * Build Interaction responses from teacher explanations
     * @param teacher the teacher
     * @param sequence the sequence
     */
    def buildInteractionResponsesFromTeacherExplanationsForASequence(User teacher, Sequence sequence, ConfidenceDegreeEnum confidenceDegree = ConfidenceDegreeEnum.CONFIDENT) {

        buildResponseBasedOnTeacherExpectedExplanationForASequence(sequence, teacher, confidenceDegree)
        buildResponsesBasedOnTeacherFakeExplanationsForASequence(sequence, confidenceDegree)

    }

    /**
     * Build Interaction response from teacher expected explanation
     * @param teacher the teacher
     * @param sequence the sequence
     */
    InteractionResponse buildResponseBasedOnTeacherExpectedExplanationForASequence(Sequence sequence, User teacher, ConfidenceDegreeEnum confidenceDegree = ConfidenceDegreeEnum.CONFIDENT) {
        def statement = sequence.statement
        if (!statement.expectedExplanation) {
            return null
        }
        def attempt = sequence.executionIsFaceToFace() ? 1 : 2
        def statementHasChoices = statement.hasChoices()
        def interaction = sequence.responseSubmissionInteraction

        InteractionResponse resp = new InteractionResponse(learner: teacher, explanation: statement.expectedExplanation,
                confidenceDegree: confidenceDegree.ordinal(), attempt: attempt, interaction: interaction)
        if (statementHasChoices) {
            resp.updateChoiceListSpecification(statement.choiceSpecificationObject?.expectedChoiceList?.collect {
                it.index
            })
            resp.updateScore()
        }
        resp.save()
        resp
    }

    /**
     * Build Interaction response from teacher expected explanation
     * @param teacher the teacher
     * @param sequence the sequence
     */
    List<InteractionResponse> buildResponsesBasedOnTeacherFakeExplanationsForASequence(Sequence sequence, ConfidenceDegreeEnum confidenceDegree = ConfidenceDegreeEnum.CONFIDENT) {
        def statement = sequence.statement
        def explanations = statement.fakeExplanations
        if (!explanations) {
            return []
        }
        def attempt = sequence.executionIsFaceToFace() ? 1 : 2

        def statementHasChoices = statement.hasChoices()
        def interaction = sequence.responseSubmissionInteraction

        def res = []
        explanations.eachWithIndex { fakeExplanation, index ->
            def learner = User.load(bootstrapService.fakeUserList[index].id)
            InteractionResponse fakeResp = new InteractionResponse(learner: learner, explanation: fakeExplanation.content,
                    confidenceDegree: confidenceDegree.ordinal(), attempt:attempt, interaction: interaction)
            if (statementHasChoices && fakeExplanation.correspondingItem) {
                fakeResp.updateChoiceListSpecification([fakeExplanation.correspondingItem])
                fakeResp.updateScore()
            }
            fakeResp.save()
            res << fakeResp
        }
        res
    }


    private void updateActiveInteractionInSequence(Interaction interaction) {
        Sequence sequence = interaction.sequence
        int newRank = interaction.rank+1
        if (newRank <= sequence.interactions.size()) {
            sequence.activeInteraction = sequence.interactions[newRank-1]
            sequence.save()
        }
    }

    private static final String ONLY_OWNER_CAN_START_INTERACTION = 'Only owner can start an interaction'
    private static final String ONLY_OWNER_CAN_STOP_INTERACTION = 'Only owner can stop an interaction'
    private static final String INTERACTION_IS_NOT_STARTED = 'The interaction is not started'
    private static final String INTERACTION_CANNOT_RECEIVE_RESPONSE = 'The interaction cannot receive response'
    private static
    final String LEARNER_NOT_REGISTERED_IN_ASSIGNMENT = 'Learner is not registered in the relative assignment'


}
