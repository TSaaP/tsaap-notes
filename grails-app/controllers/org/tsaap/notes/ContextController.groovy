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

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import grails.transaction.NotTransactional
import grails.transaction.Transactional
import org.tsaap.directory.User
import org.tsaap.questions.StatisticsService
import org.tsaap.questions.export.ExportAsGiftService

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ContextController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE", duplicate: "POST"]

    SpringSecurityService springSecurityService
    ContextService contextService
    ExportAsGiftService exportAsGiftService
    StatisticsService statisticsService

    // for export stats
    def exportService
    def grailsApplication

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def index(Integer max, String filter) {
        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'dateCreated'
        params.order = params.order ?: 'desc'

        User user = springSecurityService.currentUser
        def contextList
        def contextCount = 0
        if (!filter || filter == FilterReservedValue.__ALL__.name()) {
            contextList = Context.findAllByRemoved(false, params)
            contextCount = Context.countByRemoved(false)
        } else if (filter == FilterReservedValue.__MINE__.name()) {
            contextList = contextService.contextsForOwner(user, params)
            contextCount = Context.countByOwnerAndRemoved(user, false)
        } else if (filter == FilterReservedValue.__FOLLOWED__.name()) {
            contextList = contextService.contextsFollowedByUser(user, params)
            contextCount = contextList.totalCount
        } else {
            contextList = Context.findAllByContextNameIlikeAndRemoved("%${filter}%", false, params)
            contextCount = Context.countByContextNameIlikeAndRemoved("%${filter}%", false)
        }

        respond contextList, model: [contextList: contextList, contextCount: contextCount, user: user]
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def show(Context context) {
        if (!contextService.contextExists(context)) {
            notFound()
        } else {
            respond context, model: [context: context, user: springSecurityService.currentUser]
        }
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
        if (!contextService.contextExists(context)) {
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
        if (!contextService.contextExists(context)) {
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
                flash.message = message(code: 'default.updated.message', args: [message(code: 'context.label', default: 'Context'), context.id])
                redirect context
            }
            '*' { respond context, [status: OK] }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def delete(Context context) {

        if (!contextService.contextExists(context)) {
            notFound()
            return
        }
        contextService.deleteContext(context, springSecurityService.currentUser, true)

        request.withFormat {
            form {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'context.label', default: 'Context'), context.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def followContext(Context context) {
        if (!contextService.contextExists(context)) {
            notFound()
            return
        }

        contextService.subscribeUserOnContext(springSecurityService.currentUser, context)
        request.withFormat {
            html {
                redirect action: "index", method: "GET", params: params
            }
            '*' { respond context, [status: OK] }
        }
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def unfollowContext(Context context) {
        if (!contextService.contextExists(context)) {
            notFound()
            return
        }

        contextService.unsuscribeUserOnContext(springSecurityService.currentUser, context)
        request.withFormat {
            html {
                redirect action: "index", method: "GET", params: params
            }
            '*' { respond context, [status: OK] }
        }
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def exportQuestionsAsGiftWithFeedbacks(Context context) {
        if (!contextService.contextExists(context)) {
            notFound()
            return
        }
        def questions = exportAsGiftService.findAllGiftQuestionsWithNotesAsFeedbackForContext(springSecurityService.currentUser, context, message(code: "questions.export.feedback.prefix"))
        response.setHeader "Content-disposition", "attachment; filename=ExportMoodleGift.txt"
        render(template: "/questions/export/QuestionsAsGift", contentType: "text/plain", encoding: "UTF-8", model: [questions: questions])
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def exportQuestionsAsGift(Context context) {
        if (!contextService.contextExists(context)) {
            notFound()
            return
        }
        def questions = exportAsGiftService.findAllGiftQuestionsForContext(springSecurityService.currentUser, context)
        response.setHeader "Content-disposition", "attachment; filename=ExportMoodleGift.txt"
        render(template: "/questions/export/QuestionsAsGift", contentType: "text/plain", encoding: "UTF-8", model: [questions: questions])
    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def duplicate(Context context) {
        if (!contextService.contextExists(context)) {
            notFound()
            return
        }
        def newContext = contextService.duplicateContext(context, springSecurityService.currentUser)
        redirect action: "edit", controller: "context", id: newContext.id
    }

    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def statistics() {
        User user = springSecurityService.currentUser
        Context context = Context.get(params.id)
        List liveSessionIds = contextService.findAllNphaseLiveSessionIdsForContext(context)
        def statsList = []
        liveSessionIds.each {
            statsList << statisticsService.getNPhasesLiveSessionStatisticsForLiveSessionId(it as Long)
        }
        Map labels = statisticsService.nPhaseSessionStatsLabels()
        if (params?.format && params.format != "html") {
            response.contentType = grailsApplication.config.grails.mime.types[params.format]
            response.setHeader("Content-disposition", "attachment; filename=tsaapNotesStats.${params.extension}")
            exportService.export(
                    params.format,
                    response.outputStream,
                    statsList,
                    labels.keySet() as List,
                    labels,
                    [:],
                    [:]
            )
        }
        render(view: '/context/contextNPhaseSessionsStats', model: [stats: statsList, labels: labels, user: user, context: context])
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

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def close(Integer max) {

        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'dateCreated'
        params.order = params.order ?: 'desc'
        User user = springSecurityService.currentUser
        contextService.closeScope(Context.findById(params.id), user)
        def contextList
        def contextCount = 0
        if (params.show) {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'context.label', default: 'Context'), params.id])
            redirect(uri: '/scope/show/' + params.id)
        }
        if ((!params.filter || params.filter == FilterReservedValue.__ALL__.name()) && (!params.show)) {
            contextList = Context.list(params)
            contextCount = Context.count()
            redirect(uri: '/scope/index', params: [contextList: contextList, contextCount: contextCount, user: user])
        } else if (params.filter == FilterReservedValue.__MINE__.name()) {
            contextList = contextService.contextsForOwner(user, params)
            contextCount = Context.countByOwner(user)
            redirect(uri: '/scope/index?filter=__MINE__', params: [contextList: contextList, contextCount: contextCount, user: user])
        }


    }

    @Transactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def open(Integer max) {

        params.max = Math.min(max ?: 10, 100)
        params.sort = params.sort ?: 'dateCreated'
        params.order = params.order ?: 'desc'
        User user = springSecurityService.currentUser
        contextService.openScope(Context.findById(params.id), user)
        def contextList
        def contextCount = 0
        if (params.show) {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'context.label', default: 'Context'), params.id])
            redirect(uri: '/scope/show/' + params.id)
        }
        if ((!params.filter || params.filter == FilterReservedValue.__ALL__.name()) && (!params.show)) {
            contextList = Context.list(params)
            contextCount = Context.count()
            redirect(uri: '/scope/index', params: [contextList: contextList, contextCount: contextCount, user: user])
        } else if (params.filter == FilterReservedValue.__MINE__.name()) {
            contextList = contextService.contextsForOwner(user, params)
            contextCount = Context.countByOwner(user)
            redirect(uri: '/scope/index?filter=__MINE__', params: [contextList: contextList, contextCount: contextCount, user: user])
        }


    }

    @NotTransactional
    @Secured(['IS_AUTHENTICATED_REMEMBERED'])
    def syncGrades() {
        try {
            contextService.sendAllUserGradeToLms(params.id)
            flash.message = message(code: 'context.synchronized.sucess.message')
        } catch (Throwable e) {
            log.error(e.message)
            flash.message = message(code: 'context.synchronized.failure.message')
        }

        redirect(uri: '/scope/show/' + params.id)
    }
}

enum FilterReservedValue {
    __ALL__,
    __FOLLOWED__,
    __MINE__
}