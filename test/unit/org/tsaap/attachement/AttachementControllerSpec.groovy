package org.tsaap.attachement


import grails.test.mixin.*
import org.tsaap.attachement.Attachement
import org.tsaap.attachement.AttachementController
import org.tsaap.notes.Context
import org.tsaap.notes.Note
import spock.lang.*

@TestFor(AttachementController)
@Mock(Attachement)
class AttachementControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
        params["path"] = "/home/dorian"
        params["name"] = "grails.png"
        params["originalName"] = "grails.png"
        params["size"] = 1
        params["dimension"] = Mock(Dimension)
        params["typeMime"] = "image/png"
        params["note"] = Mock(Note)
        params["context"] = Mock(Context)
    }

    void "Test the index action returns the correct model"() {

        when: "The index action is executed"
        controller.index()

        then: "The model is correct"
        !model.attachementInstanceList
        model.attachementInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when: "The create action is executed"
        controller.create()

        then: "The model is correctly created"
        model.attachementInstance != null
    }

    void "Test the save action correctly persists an instance"() {

        when: "The save action is executed with an invalid instance"
        def attachement = new Attachement()
        attachement.validate()
        controller.save(attachement)

        then: "The create view is rendered again with the correct model"
        model.attachementInstance != null
        view == 'create'

        when: "The save action is executed with a valid instance"
        response.reset()
        populateValidParams(params)
        attachement = new Attachement(params)

        controller.save(attachement)

        then: "A redirect is issued to the show action"
        response.redirectedUrl == '/attachement/show/1'
        controller.flash.message != null
        Attachement.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when: "The show action is executed with a null domain"
        controller.show(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the show action"
        populateValidParams(params)
        def attachement = new Attachement(params)
        controller.show(attachement)

        then: "A model is populated containing the domain instance"
        model.attachementInstance == attachement
    }

    void "Test that the edit action returns the correct model"() {
        when: "The edit action is executed with a null domain"
        controller.edit(null)

        then: "A 404 error is returned"
        response.status == 404

        when: "A domain instance is passed to the edit action"
        populateValidParams(params)
        def attachement = new Attachement(params)
        controller.edit(attachement)

        then: "A model is populated containing the domain instance"
        model.attachementInstance == attachement
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when: "Update is called for a domain instance that doesn't exist"
        controller.update(null)

        then: "A 404 error is returned"
        response.redirectedUrl == '/attachement/index'
        flash.message != null


        when: "An invalid domain instance is passed to the update action"
        response.reset()
        def attachement = new Attachement()
        attachement.validate()
        controller.update(attachement)

        then: "The edit view is rendered again with the invalid instance"
        view == 'edit'
        model.attachementInstance == attachement

        when: "A valid domain instance is passed to the update action"
        response.reset()
        populateValidParams(params)
        attachement = new Attachement(params).save(flush: true)
        controller.update(attachement)

        then: "A redirect is issues to the show action"
        response.redirectedUrl == "/attachement/show/$attachement.id"
        flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when: "The delete action is called for a null instance"
        controller.delete(null)

        then: "A 404 is returned"
        response.redirectedUrl == '/attachement/index'
        flash.message != null

        when: "A domain instance is created"
        response.reset()
        populateValidParams(params)
        def attachement = new Attachement(params).save(flush: true)

        then: "It exists"
        Attachement.count() == 1

        when: "The domain instance is passed to the delete action"
        controller.delete(attachement)

        then: "The instance is deleted"
        Attachement.count() == 0
        response.redirectedUrl == '/attachement/index'
        flash.message != null
    }
}
