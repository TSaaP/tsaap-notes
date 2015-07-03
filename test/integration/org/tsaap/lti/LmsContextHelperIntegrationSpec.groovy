package org.tsaap.lti

import groovy.sql.Sql
import org.tsaap.BootstrapTestService
import spock.lang.Specification

import javax.sql.DataSource
import java.sql.SQLException

/**
 * Created by dorian on 30/06/15.
 */
class LmsContextHelperIntegrationSpec extends Specification{

    DataSource dataSource
    Sql sql
    LmsContextHelper lmsContextHelper
    LmsUserHelper lmsUserHelper

    def setup() {
        lmsContextHelper = new LmsContextHelper()
        lmsUserHelper = new LmsUserHelper()
        sql = new Sql(dataSource)
    }


    def "test select context id in database"() {

        when: "I want to select a context id from a context name and source"
        def userId = null
        def res = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,"Context",null,userId,true,null,"Source")
                res = lmsContextHelper.selectContextId(sql,"Context","Source")
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "I get the context id"
        res != null
    }

    def "test select context name in database"() {

        when: "I want to select a context name from a context id"
        def contextId = null
        def res = null
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,"Context",null,userId,true,null,"Source")
                contextId = lmsContextHelper.selectContextId(sql,"Context","Source")
                res = lmsContextHelper.selectContextName(sql,contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "I get the context name"
        res == "Context"
    }

    def "test insert context in database"() {

        when: "I want to insert a context in database"
        def res = null
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,'Test',null,userId,true,null,'Source')
                res = lmsContextHelper.selectContextId(sql,'Test','Source')
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "I get the context id"
        res != null
    }


    def "test select lms context in database"() {

        when: "I want to select an lms context id if the lms context is attach to the given lti context"
        def req = null
        def res = null
        def res2 = null
        def contextId
        def userId = null
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql,'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql,"key","3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,"Tsaap teach: Tsaap",null,userId,true,null,'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql,"Tsaap teach: Tsaap",'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql,contextId,"3","4",'key','Moodle-Tsaap')
                res = lmsContextHelper.selectLmsContext(sql,'key',"3","4")
                res2 = lmsContextHelper.selectLmsContext(sql,'key','8','12')
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "I get an id for an existing lms context and null for the other"
        res != null
        res == contextId
        res2 == null

    }

    def "test insert lms context in database"() {

        when: "I want to insert a lms context for a given lti_context and context"
        def req = null
        def res = null
        def userId = null
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql,'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql,"key","3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,"Tsaap teach: Tsaap",null,userId,true,null,'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql,"Tsaap teach: Tsaap",'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql,contextId,"3","4",'key','Moodle-Tsaap')
                res = lmsContextHelper.selectLmsContext(sql,'key',"3","4")
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "The lms context is correctly insert"
        res != null
        res == contextId

    }

    def "test select lms context for a context"() {

        when: "I want to select a lms context for a given tsaap context id"
        def req = null
        def res = null
        def userId = null
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql,'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql,"key","3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,"Tsaap teach: Tsaap",null,userId,true,null,'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql,"Tsaap teach: Tsaap",'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql,contextId,"3","4",'key','Moodle-Tsaap')
                res = lmsContextHelper.selectLmsContextForContextId(sql,contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "I get the lms context"
        res != null
        res.tsaap_context_id == contextId
    }

    def "test delete lms context for a context"() {

        when: "I want to delete a lms context for a given tsaap context id"
        def req = null
        def res = null
        def userId = null
        def contextId
        try {
            sql.withTransaction { ->
                lmsUserHelper.insertLtiConsumerInDatabase(sql,'key', 'Moodle', 'azer', 'LTI-1p0', 'Moodle-Tsaap', 'moodle-2015051100.06', '130.120.214.80', null, 0, 1, null, null)
                lmsUserHelper.insertLtiContextInDatabase(sql,"key","3", "4", "3", "Tsaap teach: Tsaap", "{\"lis_outcome_service_url\":\"http://130.120.214.80/moodle/mod/lti/service.php\",\"lis_result_sourcedid\":\"{\\\"data\\\":{\\\"instanceid\\\":\\\"3\\\",\\\"userid\\\":\\\"5\\\",\\\"typeid\\\":\\\"1\\\",\\\"launchid\\\":827256523},\\\"hash\\\":\\\"26cdb9af21a105ee3c7d9211ca7809d6a43f34489f32cfc715ff9718a7193da5\\\"}\"}")
                lmsUserHelper.insertUserInDatabase(sql,"jdoe@nomail.com","john","doe","jdoe","pass")
                userId = lmsUserHelper.selectUserId(sql,"jdoe")
                lmsContextHelper.insertContext(sql,"Tsaap teach: Tsaap",null,userId,true,null,'Moodle-Tsaap')
                req = lmsContextHelper.selectContextId(sql,"Tsaap teach: Tsaap",'Moodle-Tsaap')
                contextId = req
                lmsContextHelper.insertLmsContext(sql,contextId,"3","4",'key','Moodle-Tsaap')
                lmsContextHelper.deleteLmsContext(sql,contextId)
                res = lmsContextHelper.selectLmsContextForContextId(sql,contextId)
                throw new SQLException()
            }
        }
        catch (SQLException e){}

        then: "the lms context is delele"
        res == null
    }

}
