package org.tsaap.directory

import groovy.sql.Sql
import org.tsaap.lti.LmsUserHelper
import spock.lang.Specification

import javax.sql.DataSource

/**
 * Created by dorian on 24/06/15.
 */
class UserProvisionAccountServiceIntegrationSpec extends Specification{

    UserProvisionAccountService userProvisionAccountService
    DataSource dataSource
    LmsUserHelper lmsUserHelper
    Sql sql

    def setup() {
        sql = new Sql(dataSource)
        lmsUserHelper = new LmsUserHelper()
        userProvisionAccountService.lmsUserHelper = lmsUserHelper
    }

    def "test generate username"(){

        when: "I want to generate a new username for two user with same quadrigram"
        def username = userProvisionAccountService.generateUsername(sql,"john","doe")
        lmsUserHelper.insertUserInDatabase(sql,'jdoe@mail.com','john','doe','jdoe','pass')
        def username2 = userProvisionAccountService.generateUsername(sql,'jane','doe')
        lmsUserHelper.insertUserInDatabase(sql,'jane@mail.com','jane','doe','jdoe2','passw')
        def username3 = userProvisionAccountService.generateUsername(sql,'jean','doel')


        then: "I got a different username for the two users"
        username == "jdoe"
        username2 == "jdoe2"
        username3  == "jdoe3"

    }
}
