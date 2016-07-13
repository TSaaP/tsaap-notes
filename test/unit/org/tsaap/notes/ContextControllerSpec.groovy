/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.notes

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.sql.Sql
import org.tsaap.directory.User
import org.tsaap.lti.LmsContextHelper
import org.tsaap.lti.LmsContextService
import org.tsaap.questions.LiveSession
import org.tsaap.questions.LiveSessionService
import spock.lang.Specification

import javax.sql.DataSource

@TestFor(ContextController)
@Mock([Context, User, Note, ContextService, ContextFollower, LiveSessionService, NoteService, LiveSession])
class ContextControllerSpec extends Specification {

    User user
    SpringSecurityService springSecurityService = Mock(SpringSecurityService)
    DataSource dataSource = Mock(DataSource)
    Sql sql = Mock(Sql)
    LmsContextService lmsContextService = Mock(LmsContextService)
    LmsContextHelper lmsContextHelper = Mock(LmsContextHelper)

    def setup() {
        controller.springSecurityService = springSecurityService
        user = new User(firstName: "franck", lastName: "s", username: "fsil", email: "mail@mail.com", password: "password")
        user.springSecurityService = springSecurityService
        controller.contextService.dataSource = dataSource
        controller.contextService.sql = sql
        controller.contextService.lmsContextService = lmsContextService
        controller.contextService.lmsContextHelper = lmsContextHelper
        springSecurityService.encodePassword(user.password) >> user.password
        user.save()
    }


    def populateValidParams(params) {
        assert params != null
        params["contextName"] = 'science'
        params["url"] = 'http://www.w3.org'
        params["owner"] = user
        params["descriptionAsNote"] = "a description"

    }

    void "Test the index action returns the correct model"() {


        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        model.contextList == []
        model.contextCount == 0
    }

    void "Test the create action returns the correct model"() {

        when: "The create action is executed"
        springSecurityService.currentUser >> user
        controller.create()

        then: "The model is correctly created"
        model.contextInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        def context = new Context()
        context.validate()
        controller.save(context)

        then: "The create view is rendered again with the correct model"
        model.contextInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        context = new Context(params)
        if (context.hasErrors()) {
            println context.errors
        }

        controller.save(context)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/context/show/1'
        controller.flash.message != null
        Context.count() == 1
    }

    void "Test that the show action returns the correct model"() {

        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A redirect error is returned"
        status == 302

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def context = new Context(params)
        controller.show(context)

        then: "A model is populated containing the domain instance"
        model.context == context
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def context = new Context(params)
        controller.edit(context)

        then: "A model is populated containing the domain instance"
        model.context == context
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A redirect error is returned"
        status == 302

        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def context = new Context()
        context.validate()
        controller.update(context)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.contextInstance == context

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        context = new Context(params).save(flush: true)
        springSecurityService.currentUser >> user
        controller.update(context)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/context/show/$context.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A redirect is returned"
        status == 302

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def context = new Context(params).save(flush: true)

        then: "It exists"
        Context.count() == 1

        when: "The domain instance is passed to the delete action"
        springSecurityService.currentUser >> context.owner
        controller.delete(context)

        then: "The instance is deleted"
        Context.count() == 0
        response.redirectedUrl == '/context/index'
        flash.message != null

    }

    void "Test about removing context that contains notes"() {
        when: "Create a new context"
        populateValidParams(params)
        def context = new Context(params).save(flush: true)

        then: "The context exist"
        Context.count() == 1

        when: "Create note in this context"
        Note note = new Note(author: user, content: "standard note", context: context, kind: NoteKind.STANDARD.ordinal())
        note.save(flush: true)

        then: "Check if context has notes"
        Note.count() == 1
        context.hasNotes() == true
        context.hasStandardNotes() == true
        context.newNotesCountSinceYesterday() == 1

        when: "Trying to delete context2"
        context.isRemoved() == false
        springSecurityService.currentUser >> context.owner
        controller.delete(context)

        then: "context is marked as removed but not deleted from database"
        context.isRemoved() == true
        Context.count() == 1

    }

    void "Test about closing scope"() {
        when: "Create a new context"
        populateValidParams(params)
        def context = new Context(params).save(flush: true)

        and: ""
        springSecurityService.currentUser >> context.owner
        params.filter = filter
        params.show = show
        params.id = context.getId()
        controller.close(10)

        then: ""
        response.redirectedUrl == resp

        where: ""
        filter                              | show | resp
        FilterReservedValue.__MINE__.name() | 0    | '/scope/index?filter=__MINE__'
        FilterReservedValue.__ALL__.name()  | 0    | '/scope/index'
        ""                                  | 1    | '/scope/show/1'
        ""                                  | 0    | '/scope/index'


    }

    void "Test about opening scope"() {
        when: "Create a new claused context"
        populateValidParams(params)
        def context = new Context(params).save(flush: true)
        context.closed = true

        and: ""
        springSecurityService.currentUser >> context.owner
        params.filter = filter
        params.show = show
        params.id = context.getId()
        controller.open(10)

        then: ""
        response.redirectedUrl == resp

        where: ""
        filter                              | show | resp
        FilterReservedValue.__MINE__.name() | 0    | '/scope/index?filter=__MINE__'
        FilterReservedValue.__ALL__.name()  | 0    | '/scope/index'
        ""                                  | 1    | '/scope/show/1'
        ""                                  | 0    | '/scope/index'


    }
}
