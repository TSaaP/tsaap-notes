package org.tsaap.notes

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import org.tsaap.directory.User
import org.tsaap.questions.export.ExportAsGiftService

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ContextController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    SpringSecurityService springSecurityService
    ContextService contextService
    ExportAsGiftService exportAsGiftService

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max, String filter) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'contextName'
        params.order = params.order ?: 'asc'
        User user = springSecurityService.currentUser
        def contextList
        def contextCount = 0
        if (!filter) {
            contextList = Context.list(params)
            contextCount = Context.count()
        } else {
            contextList = Context.findAllByContextNameIlike("${filter}%", params)
            contextCount = Context.countByContextNameIlike("${filter}")
        }

        respond contextList, model: [contextList: contextList, contextCount: contextCount, user: user]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Context context) {
        respond context, model: [context: context, user: springSecurityService.currentUser]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def create() {
        params['owner'] = springSecurityService.currentUser.id
        respond new Context(params), model: [user: springSecurityService.currentUser]
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def save(Context context) {
        def user = springSecurityService.currentUser
        if (context == null) {
            notFound()
            return
        }

        context.validate()
        if (context.hasErrors()) {
            respond context.errors, view: 'create', model: [context: context, user: user]
            return
        }

        contextService.saveContext(context, true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.created.message', args: [message(code: 'context.label', default: 'Context'), context.id])
                redirect context
            }
            '*' { respond context, [status: CREATED] }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def edit(Context context) {
        respond context, model: [context: context, user: springSecurityService.currentUser]
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def update(Context context) {
        if (context == null) {
            notFound()
            return
        }

        User user = springSecurityService.currentUser
        context.validate()
        if (context.hasErrors()) {
            respond context.errors, model: [context: context, user: user], view: 'edit'
            return
        }

        contextService.updateContext(context, user, true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Context.label', default: 'Context'), context.id])
                redirect context
            }
            '*' { respond context, [status: OK] }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def delete(Context context) {

        if (context == null) {
            notFound()
            return
        }

        contextService.deleteContext(context, springSecurityService.currentUser, true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Context.label', default: 'Context'), context.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def followContext(Context context) {
        if (context == null) {
            notFound()
            return
        }

        contextService.subscribeUserOnContext(springSecurityService.currentUser, context)
        request.withFormat {
            html {
                redirect action: "index", method: "GET"
            }
            '*' { respond context, [status: OK] }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unfollowContext(Context context) {
        if (context == null) {
            notFound()
            return
        }

        contextService.unsuscribeUserOnContext(springSecurityService.currentUser, context)
        request.withFormat {
            html {
                redirect action: "index", method: "GET"
            }
            '*' { respond context, [status: OK] }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def exportQuestionsAsGift(Context context) {
        if (context == null) {
            notFound()
            return
        }
        def questions = exportAsGiftService.findAllGiftQuestionsWithNotesAsFeedbackForContext(springSecurityService.currentUser, context,message(code:"questions.export.feedback.prefix"))
        response.setHeader "Content-disposition", "attachment; filename=ExportMoodleGift.txt"
        render(template:"/questions/export/QuestionsAsGift", contentType: "text/plain", encoding: "UTF-8", model: [questions:questions])
    }


    protected void notFound() {
        request.withFormat {
            form {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'context.label', default: 'Context'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
