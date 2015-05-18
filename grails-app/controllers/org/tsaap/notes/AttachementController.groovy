package org.tsaap.notes


import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AttachementController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Attachement.list(params), model: [attachementInstanceCount: Attachement.count()]
    }

    def show(Attachement attachementInstance) {
        respond attachementInstance
    }

    def create() {
        respond new Attachement(params)
    }

    @Transactional
    def save(Attachement attachementInstance) {
        if (attachementInstance == null) {
            notFound()
            return
        }

        if (attachementInstance.hasErrors()) {
            respond attachementInstance.errors, view: 'create'
            return
        }

        attachementInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'attachementInstance.label', default: 'Attachement'), attachementInstance.id])
                redirect attachementInstance
            }
            '*' { respond attachementInstance, [status: CREATED] }
        }
    }

    def edit(Attachement attachementInstance) {
        respond attachementInstance
    }

    @Transactional
    def update(Attachement attachementInstance) {
        if (attachementInstance == null) {
            notFound()
            return
        }

        if (attachementInstance.hasErrors()) {
            respond attachementInstance.errors, view: 'edit'
            return
        }

        attachementInstance.save flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Attachement.label', default: 'Attachement'), attachementInstance.id])
                redirect attachementInstance
            }
            '*' { respond attachementInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Attachement attachementInstance) {

        if (attachementInstance == null) {
            notFound()
            return
        }

        attachementInstance.delete flush: true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Attachement.label', default: 'Attachement'), attachementInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'attachementInstance.label', default: 'Attachement'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
