package org.tsaap.lti

import grails.plugins.springsecurity.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
@Secured(['IS_AUTHENTICATED_FULLY', 'ROLE_ADMIN_ROLE'])
class LtiConsumerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond LtiConsumer.list(params), model: [ltiConsumerInstanceCount: LtiConsumer.count()]
    }

    def show(LtiConsumer ltiConsumerInstance) {
        respond ltiConsumerInstance
    }

    def create() {
        respond new LtiConsumer(params)
    }

    @Transactional
    def save() {
        LtiConsumer ltiConsumerInstance = new LtiConsumer(params)
        if (ltiConsumerInstance == null) {
            notFound()
            return
        }


        if (ltiConsumerInstance.hasErrors()) {
            respond ltiConsumerInstance.errors, view: 'create'
            return
        }

        ltiConsumerInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ltiConsumerInstance.label', default: 'LtiConsumer'), ltiConsumerInstance.id])
                redirect ltiConsumerInstance
            }
            '*' { respond ltiConsumerInstance, [status: CREATED] }
        }
    }

    def edit(LtiConsumer ltiConsumerInstance) {
        respond ltiConsumerInstance
    }

    @Transactional
    def update() {
        LtiConsumer ltiConsumerInstance = new LtiConsumer(params)
        if (ltiConsumerInstance == null) {
            notFound()
            return
        }

        if (ltiConsumerInstance.hasErrors()) {
            respond ltiConsumerInstance.errors, view: 'edit'
            return
        }

        ltiConsumerInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'LtiConsumer.label', default: 'LtiConsumer'), ltiConsumerInstance.id])
                redirect ltiConsumerInstance
            }
            '*' { respond ltiConsumerInstance, [status: OK] }
        }
    }

    @Transactional
    def delete() {
        LtiConsumer ltiConsumerInstance = new LtiConsumer(params)
        if (ltiConsumerInstance == null) {
            notFound()
            return
        }

        ltiConsumerInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'LtiConsumer.label', default: 'LtiConsumer'), ltiConsumerInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ltiConsumerInstance.label', default: 'LtiConsumer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
