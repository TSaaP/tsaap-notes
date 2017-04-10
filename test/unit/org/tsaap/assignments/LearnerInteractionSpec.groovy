package org.tsaap.assignments

import grails.test.mixin.TestFor
import org.tsaap.directory.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(LearnerInteraction)
class LearnerInteractionSpec extends Specification {

    User learner
    Interaction interaction

    def setup() {
        learner = Mock(User)
        interaction = Mock(Interaction)
    }

    def cleanup() {
    }

    void "test creation of a learner interaction"() {

        given: "a learner"
        learner

        and: "an interaction"
        interaction

        when: "the learner interaction is created"
        def learnerInteraction = new LearnerInteraction(learner: learner, interaction:interaction).save()

        then: "the learner interaction is not active"
        !learnerInteraction.isActive

        and: "the learner interaction state is 'show'"
        learnerInteraction.state == StateType.show.name()

    }

    void "test activation of a learner interaction"() {

        given: "a learner"
        learner

        and: "an interaction"
        interaction

        and: "the corresponding learner interaction"
        def learnerInteraction = new LearnerInteraction(learner: learner, interaction:interaction).save()

        when: "the learner interaction is activated"
        learnerInteraction.activate()

        then: "the learner interaction become active"
        learnerInteraction.isActive

    }
}
