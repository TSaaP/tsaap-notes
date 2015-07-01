package org.tsaap.lti

import grails.test.mixin.TestFor
import groovy.sql.Sql
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(LmsContextService)
class LmsContextServiceSpec extends Specification {

    LmsContextService lmsContextService
    LmsContextHelper lmsContextHelper
    LmsUserHelper lmsUserHelper
    Sql sql

    def setup() {
        lmsContextService = new LmsContextService()
    }

    void "test to create a tsaap note context for given lti context and consumer "() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsContextHelper = Mock(LmsContextHelper)
        lmsUserHelper = Mock(LmsUserHelper)
        lmsContextService.lmsContextHelper = lmsContextHelper
        lmsContextService.lmsUserHelper = lmsUserHelper

        when: "I try to create a tsaap note context and user is a teacher"
        def res = lmsContextService.findOrCreateContext(sql,'key','3','4','Moodle Tsaap','Tsaap Teach: Tsaap',"jdoe",false)

        then: "a context is created and I get his id and his name"
        1*lmsContextHelper.selectLmsContext(sql,'key','3','4') >> null
        1*lmsUserHelper.selectUserId(sql,"jdoe") >> 88
        1*lmsContextHelper.insertContext(sql,'Tsaap Teach: Tsaap',null,88,true,null,'Moodle Tsaap')
        1*lmsContextHelper.selectContextId(sql,'Tsaap Teach: Tsaap','Moodle Tsaap') >> 55
        1*lmsContextHelper.insertLmsContext(sql,55,'3','4','key','Moodle Tsaap')
        res.get(0) == 'Tsaap Teach: Tsaap'
        res.get(1) == 55

        when: "I try to create a tsaap note context and user is a learner"
        lmsContextService.findOrCreateContext(sql,'key','3','4','Moodle Tsaap','Tsaap Teach: Tsaap',"jdoe",true)

        then: "I get an exception"
        1*lmsContextHelper.selectLmsContext(sql,'key','3','4') >> null
        thrown(LtiContextInitialisationException)
    }

    void "test to find a tsaap note context for given lti context and consumer"() {

        given: "the collaborators"
        sql = Mock(Sql)
        lmsContextHelper = Mock(LmsContextHelper)
        lmsContextService.lmsContextHelper = lmsContextHelper

        when: "I try to find a tsaap note context"
        def res = lmsContextService.findOrCreateContext(sql,'key','3','4','Moodle Tsaap','Tsaap Teach: Tsaap',"jdoe",false)

        then: "a context is found and I get his id and his name"
        1*lmsContextHelper.selectLmsContext(sql,'key','3','4') >> 55
        1*lmsContextHelper.selectContextName(sql,55) >> 'Tsaap Teach: Tsaap'
        res.get(0) == 'Tsaap Teach: Tsaap'
        res.get(1) == 55
    }
}
