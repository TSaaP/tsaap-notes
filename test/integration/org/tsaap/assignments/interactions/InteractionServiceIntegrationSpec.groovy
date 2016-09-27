package org.tsaap.assignments.interactions


import org.tsaap.BootstrapTestService
import org.tsaap.assignments.Assignment
import org.tsaap.assignments.Interaction
import org.tsaap.assignments.Sequence
import org.tsaap.assignments.StateType
import org.tsaap.contracts.ConditionViolationException
import org.tsaap.directory.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
class InteractionServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    InteractionService interactionService

    def setup() {
        bootstrapTestService.initializeTests()
    }

    void "test start interaction"() {

        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.lastSequence
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        expect: "the state is consistent"
        sequence.state == StateType.beforeStart.name()
        interaction.state == StateType.beforeStart.name()

        when: "the interaction is started"
        interactionService.startInteraction(interaction,interaction.owner)

        then: "the interaction enter in the new state"
        interaction.state == StateType.show.name()
        sequence.state == StateType.show.name()

    }

    void "test stop interaction"() {

        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Sequence sequence = assignment.lastSequence
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        expect: "the active interaction is the first one"
        sequence.activeInteraction == interaction

        when: "trying to stop not started interaction"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "an exception is thrown"
        thrown(ConditionViolationException)

        when:"the interaction is started"
        interactionService.startInteraction(interaction, interaction.owner)

        and: "the interaction is stopped"
        interactionService.stopInteraction(interaction, interaction.owner)

        then: "the interaction is in consistent state"
        interaction.state == StateType.afterStop.name()

        and: "the current active interaction is the second one"
        sequence.activeInteraction == sequence.evaluationInteraction

    }

    void "test start and stop interaction by another user than the owner"() {
        given: "an assignment with one sequence and 2 interactions"
        Assignment assignment = bootstrapTestService.assignment3WithInteractions
        Interaction interaction = assignment.lastSequence.responseSubmissionInteraction

        and: "a learner"
        User paul = bootstrapTestService.learnerPaul

        when: "trying a student try to start interaction"
        interactionService.startInteraction(interaction, paul)

        then:"an expection is thrown"
        thrown(ConditionViolationException)

        when:"the interaction is started"
        interactionService.startInteraction(interaction, interaction.owner)

        and: "the interaction is stopped by a student"
        interactionService.stopInteraction(interaction, paul)

        then:"an expection is thrown"
        thrown(ConditionViolationException)
    }
}
