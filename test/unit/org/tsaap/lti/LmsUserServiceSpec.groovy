package org.tsaap.lti

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import groovy.sql.Sql
import org.tsaap.directory.UserProvisionAccountService
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LmsUserService)
class LmsUserServiceSpec extends Specification {

    LmsUserService lmsUserService
    SpringSecurityService springSecurityService
    LmsUserHelper lmsUserHelper
    UserProvisionAccountService userProvisionAccountService
    Sql sql

    def setup() {
        lmsUserService = new LmsUserService()
    }

    void "test to find or create a tsaap note account for a given lti user without a tsaap account"() {

        given:"the collaborators"
        sql = Mock(Sql)
        lmsUserHelper = Mock(LmsUserHelper)
        springSecurityService = Mock(SpringSecurityService)
        userProvisionAccountService = Mock(UserProvisionAccountService)
        lmsUserService.lmsUserHelper = lmsUserHelper
        lmsUserService.userProvisionAccountService = userProvisionAccountService
        lmsUserService.springSecurityService = springSecurityService

        when: "I try to found or create a tsaap account for this lti user"
        lmsUserService.findOrCreateUser(sql,'10',"john","doe","doe@nomail.com",'key',true)

        then: "The user is log with the created account"
        1*lmsUserHelper.selectLmsUser(sql,'10') >> null
        1*userProvisionAccountService.generateUsername(sql,"john","doe") >> "jdoe"
        1*userProvisionAccountService.generatePassword() >> "pass"
        1*lmsUserHelper.insertUserInDatabase(sql,"doe@nomail.com","john","doe","jdoe","pass")
        1*lmsUserHelper.selectUserId(sql,"jdoe") >> 88
        1*lmsUserHelper.insertUserRoleInDatabase(sql,2,88)
        1*lmsUserHelper.insertLmsUserInDatabase(sql,88,'key','10')
        1*springSecurityService.reauthenticate("jdoe","pass")
    }

    void "test to find or create a tsaap note account for a given lti user who have a tsaap account"() {

        given:"the collaborators"
        sql = Mock(Sql)
        lmsUserHelper = Mock(LmsUserHelper)
        springSecurityService = Mock(SpringSecurityService)
        userProvisionAccountService = Mock(UserProvisionAccountService)
        lmsUserService.lmsUserHelper = lmsUserHelper
        lmsUserService.userProvisionAccountService = userProvisionAccountService
        lmsUserService.springSecurityService = springSecurityService

        when: "I try to found or create a tsaap account for this lti user"
        lmsUserService.findOrCreateUser(sql,'10',"john","doe","doe@nomail.com",'key',true)

        then: "The user is log with his account"
        1*lmsUserHelper.selectLmsUser(sql,'10') >> 88
        1*lmsUserHelper.selectUsernameAndPassword(sql,'10') >> [username: "jdoe", password: "pass"]
        1*springSecurityService.reauthenticate("jdoe","pass")
    }
}
