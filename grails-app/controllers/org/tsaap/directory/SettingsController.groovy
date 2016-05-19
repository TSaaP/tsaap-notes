package org.tsaap.directory

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SettingsController {

    SpringSecurityService springSecurityService
    SettingsService settingsService

    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doSettings() {
        render(view: '/settings/settings',model: [user: springSecurityService.currentUser])
        //redirect(uri: '/settings/settings')
    }
    @Transactional
    @Secured(['IS_AUTHENTICATED_FULLY'])
    def doUpdate() {

        User user = springSecurityService.currentUser

        def settingsMap = [:]
        if (params.dailyNotification) {
            settingsMap.dailyNotifications = true
        }
        else {
            settingsMap.dailyNotifications = false
        }
        if (params.mentionNotification) {
            settingsMap.mentionNotifications = true
        }
        else {
            settingsMap.mentionNotifications = false
        }

        def settings = settingsService.updateSettingsForUser(user, settingsMap)

         if (settings.hasErrors()) {
            render(view: '/settings/settings')
        } else {
            flash.message = message(code: 'useraccount.update.success')
            redirect(uri: '/settings/doSettings')
        }
    }

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Settings.list(params), model:[settingsInstanceCount: Settings.count()]
    }

    def show(Settings settingsInstance) {
        respond settingsInstance
    }

    def create() {
        respond new Settings(params)
    }

    @Transactional
    def save(Settings settingsInstance) {
        if (settingsInstance == null) {
            notFound()
            return
        }

        if (settingsInstance.hasErrors()) {
            respond settingsInstance.errors, view:'create'
            return
        }

        settingsInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'settingsInstance.label', default: 'Settings'), settingsInstance.id])
                redirect settingsInstance
            }
            '*' { respond settingsInstance, [status: CREATED] }
        }
    }

    def edit(Settings settingsInstance) {
        respond settingsInstance
    }

    @Transactional
    def update(Settings settingsInstance) {
        if (settingsInstance == null) {
            notFound()
            return
        }

        if (settingsInstance.hasErrors()) {
            respond settingsInstance.errors, view:'edit'
            return
        }

        settingsInstance.save flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Settings.label', default: 'Settings'), settingsInstance.id])
                redirect settingsInstance
            }
            '*'{ respond settingsInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Settings settingsInstance) {

        if (settingsInstance == null) {
            notFound()
            return
        }

        settingsInstance.delete flush:true

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Settings.label', default: 'Settings'), settingsInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'settingsInstance.label', default: 'Settings'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
