package org.tsaap.directory

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserProvisionAccountService)
class UserProvisionAccountServiceSpec extends Specification {

    UserProvisionAccountService userProvisionAccountService
    SpringSecurityService springSecurityService

    void "test generate password"() {

        userProvisionAccountService = new UserProvisionAccountService()
        springSecurityService = Mock(SpringSecurityService)
        userProvisionAccountService.springSecurityService = springSecurityService

        when: "I want to generate a password"

        def pass = userProvisionAccountService.generatePassword()

        then: "I got a password"
        1*springSecurityService.encodePassword(_) >> "pass"
        pass != null
    }
}
