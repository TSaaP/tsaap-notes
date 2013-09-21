package org.tsaap.notes

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.directory.User

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ContextController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  SpringSecurityService springSecurityService

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def index(Integer max, String filter) {
    params.max = Math.min(max ?: 10, 100)
    User user = springSecurityService.currentUser
    def contextList
    if (!filter) {
      contextList = Context.list(params)
    } else {
      contextList = Context.findAllByContextNameIlike("${filter}%", params)
    }

    respond contextList, model: [contextList: contextList,contextCount: Context.count(), user:user]
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def show(Context context) {
    respond context, model: [context:context,user:springSecurityService.currentUser]
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def create() {
    respond new Context(params), model: [user:springSecurityService.currentUser]
  }

  @Transactional
  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def save(Context context) {
    def user = springSecurityService.currentUser
    if (context == null) {
      notFound()
      return
    }

    if (context.hasErrors()) {
      respond context.errors, view: 'create', model: [user:user]
      return
    }

    context.save flush: true

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
    respond context, model: [context: context, user:springSecurityService.currentUser]
  }

  @Transactional
  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def update(Context context) {
    if (context == null) {
      notFound()
      return
    }

    if (context.hasErrors()) {
      respond context.errors, model: [user:springSecurityService.currentUser], view: 'edit'
      return
    }

    context.save flush: true

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

    context.delete flush: true

    request.withFormat {
      form {
        flash.message = message(code: 'default.deleted.message', args: [message(code: 'Context.label', default: 'Context'), context.id])
        redirect action: "index", method: "GET"
      }
      '*' { render status: NO_CONTENT }
    }
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
