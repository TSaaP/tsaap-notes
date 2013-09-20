package org.tsaap.notes



import grails.test.mixin.*
import spock.lang.*

@TestFor(ContextController)
@Mock(Context)
class ContextControllerSpec extends Specification {

  def populateValidParams(params) {
    assert params != null
    // TODO: Populate valid properties like...
    //params["name"] = 'someValidName'
  }

  void "Test the index action returns the correct model"() {

    when: "The index action is executed"
    controller.index()

    then: "The model is correct"
    !model.contextList
    model.contextCount == 0
  }

  void "Test the create action returns the correct model"() {
    when: "The create action is executed"
    controller.create()

    then: "The model is correctly created"
    model.context != null
  }

  void "Test the save action correctly persists an instance"() {

    when: "The save action is executed with an invalid instance"
    def context = new Context()
    context.validate()
    controller.save(context)

    then: "The create view is rendered again with the correct model"
    model.context != null
    view == 'create'

    when: "The save action is executed with a valid instance"
    response.reset()
    populateValidParams(params)
    context = new Context(params)

    controller.save(context)

    then: "A redirect is issued to the show action"
    response.redirectedUrl == '/context/show/1'
    controller.flash.message != null
    Context.count() == 1
  }

  void "Test that the show action returns the correct model"() {
    when: "The show action is executed with a null domain"
    controller.show(null)

    then: "A 404 error is returned"
    response.status == 404

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

    then: "A 404 error is returned"
    status == 404

    when: "An invalid domain instance is passed to the update action"
    response.reset()
    def context = new Context()
    context.validate()
    controller.update(context)

    then: "The edit view is rendered again with the invalid instance"
    view == 'edit'
    model.context == context

    when: "A valid domain instance is passed to the update action"
    response.reset()
    populateValidParams(params)
    context = new Context(params).save(flush: true)
    controller.update(context)

    then: "A redirect is issues to the show action"
    response.redirectedUrl == "/context/show/$context.id"
    flash.message != null
  }

  void "Test that the delete action deletes an instance if it exists"() {
    when: "The delete action is called for a null instance"
    controller.delete(null)

    then: "A 404 is returned"
    status == 404

    when: "A domain instance is created"
    response.reset()
    populateValidParams(params)
    def context = new Context(params).save(flush: true)

    then: "It exists"
    Context.count() == 1

    when: "The domain instance is passed to the delete action"
    controller.delete(context)

    then: "The instance is deleted"
    Context.count() == 0
    response.redirectedUrl == '/context/index'
    flash.message != null
  }
}
