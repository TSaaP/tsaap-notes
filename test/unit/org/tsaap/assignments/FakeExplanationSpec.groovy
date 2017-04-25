package org.tsaap.assignments

import grails.test.mixin.TestFor
import org.tsaap.directory.User
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(FakeExplanation)
class FakeExplanationSpec extends Specification {

    void "test validation of valid fake explanation"() {

        given: "a statement and a user"
        def statement = Mock(Statement)
        def user = Mock(User)

        when: "create a valid fake explanation"
        def vFakeExpl = new FakeExplanation(
                statement: statement,
                author: user,
                content: "a fake"
        )

        and: "trigger a validation"
        vFakeExpl.validate()

        then: "the valid fake explanation has no errors"
        !vFakeExpl.hasErrors()

    }

    void "test validation of invalid fake explanation"() {
        given: "a statement and a user"
        def statement = Mock(Statement)
        def user = Mock(User)

        when: "create a fake explanation without statement"
        def vFakeExpl = new FakeExplanation(
                author: user,
                content: "a fake"
        )

        and: "trigger a validation"
        vFakeExpl.validate()

        then: "the fake explanation has errors"
        vFakeExpl.hasErrors()

        when: "the fake explanation has no author"
        vFakeExpl.author = null
        vFakeExpl.statement = statement

        and: "trigger a validation"
        vFakeExpl.validate()

        then: "the fake explanation has errors"
        vFakeExpl.hasErrors()

        when: "the fake explanation has no content"
        vFakeExpl.author = user
        vFakeExpl.statement = statement
        vFakeExpl.content = ""

        and: "trigger a validation"
        vFakeExpl.validate()

        then: "the fake explanation has errors"
        vFakeExpl.hasErrors()

    }

}
