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

package org.tsaap.lti

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(LtiConsumerController)
@Mock(LtiConsumer)
class LtiConsumerControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        params["id"] = 'unId'
        params["consumerName"] = 'un name'
        params["secret"] = 'un secret'
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.ltiConsumerInstanceList
        model.ltiConsumerInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.ltiConsumerInstance != null
    }

//    void "Test the save action correctly persists an instance"() {
//
//        when: "The save action is executed with an invalid instance"
//        def ltiConsumer = new LtiConsumer()
//        ltiConsumer.validate()
//        controller.save()
//
//        then: "The create view is rendered again with the correct model"
//        model.ltiConsumerInstance != null
//        view == 'create'
//
//        when: "The save action is executed with a valid instance"
//        response.reset()
//        populateValidParams(params)
//        ltiConsumer = new LtiConsumer(params)
//
//        controller.save()
//
//        then: "A redirect is issued to the show action"
//        response.redirectedUrl == '/ltiConsumer/show/1'
//        controller.flash.message != null
//        LtiConsumer.count() == 1
//    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def ltiConsumer = new LtiConsumer(params)
        controller.show(ltiConsumer)

        then: "A model is populated containing the domain instance"
        model.ltiConsumerInstance == ltiConsumer
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def ltiConsumer = new LtiConsumer(params)
        controller.edit(ltiConsumer)

        then: "A model is populated containing the domain instance"
        model.ltiConsumerInstance == ltiConsumer
    }

//    void "Test the update action performs an update on a valid domain instance"() {
//        when: "Update is called for a domain instance that doesn't exist"
//        controller.update(null)
//
//        then: "A 404 error is returned"
//        response.redirectedUrl == '/ltiConsumer/index'
//        flash.message != null
//
//
//        when: "An invalid domain instance is passed to the update action"
//        response.reset()
//        def ltiConsumer = new LtiConsumer()
//        ltiConsumer.validate()
//        controller.update(ltiConsumer)
//
//        then: "The edit view is rendered again with the invalid instance"
//        view == 'edit'
//        model.ltiConsumerInstance == ltiConsumer
//
//        when: "A valid domain instance is passed to the update action"
//        response.reset()
//        populateValidParams(params)
//        ltiConsumer = new LtiConsumer(params).save(flush: true)
//        controller.update(ltiConsumer)
//
//        then: "A redirect is issues to the show action"
//        response.redirectedUrl == "/ltiConsumer/show/$ltiConsumer.id"
//        flash.message != null
//    }

//    void "Test that the delete action deletes an instance if it exists"() {
//        when: "The delete action is called for a null instance"
//        controller.delete(null)
//
//        then: "A 404 is returned"
//        response.redirectedUrl == '/ltiConsumer/index'
//        flash.message != null
//
//        when: "A domain instance is created"
//        response.reset()
//        populateValidParams(params)
//        def ltiConsumer = new LtiConsumer(params).save(flush: true)
//
//        then: "It exists"
//        LtiConsumer.count() == 1
//
//        when: "The domain instance is passed to the delete action"
//        controller.delete(ltiConsumer)
//
//        then: "The instance is deleted"
//        LtiConsumer.count() == 0
//        response.redirectedUrl == '/ltiConsumer/index'
//        flash.message != null
//    }
}
