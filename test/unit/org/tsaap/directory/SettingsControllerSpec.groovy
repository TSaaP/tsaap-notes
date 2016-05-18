package org.tsaap.directory



import grails.test.mixin.*
import spock.lang.*

@TestFor(SettingsController)
@Mock(Settings)
class SettingsControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.settingsInstanceList
            model.settingsInstanceCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.settingsInstance!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            def settings = new Settings()
            settings.validate()
            controller.save(settings)

        then:"The create view is rendered again with the correct model"
            model.settingsInstance!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            settings = new Settings(params)

            controller.save(settings)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/settings/show/1'
            controller.flash.message != null
            Settings.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def settings = new Settings(params)
            controller.show(settings)

        then:"A model is populated containing the domain instance"
            model.settingsInstance == settings
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def settings = new Settings(params)
            controller.edit(settings)

        then:"A model is populated containing the domain instance"
            model.settingsInstance == settings
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/settings/index'
            flash.message != null


        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def settings = new Settings()
            settings.validate()
            controller.update(settings)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.settingsInstance == settings

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            settings = new Settings(params).save(flush: true)
            controller.update(settings)

        then:"A redirect is issues to the show action"
            response.redirectedUrl == "/settings/show/$settings.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/settings/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def settings = new Settings(params).save(flush: true)

        then:"It exists"
            Settings.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(settings)

        then:"The instance is deleted"
            Settings.count() == 0
            response.redirectedUrl == '/settings/index'
            flash.message != null
    }
}
