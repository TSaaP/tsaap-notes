package org.tsaap.directory

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class RoleSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test getAuthoriry function"() {
        when:"create a role with name"
        Role role = new Role()
        role.roleName = "Test"

        then:"test getAutority return"
        role.getAuthority() == "ROLE_Test"
    }
}
