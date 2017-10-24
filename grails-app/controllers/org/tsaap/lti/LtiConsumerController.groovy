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

import grails.plugins.springsecurity.Secured
import grails.transaction.Transactional
import org.tsaap.skin.SkinUtil

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
@Secured(['IS_AUTHENTICATED_FULLY', 'ROLE_ADMIN_ROLE'])
class LtiConsumerController {

  static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

  def index(Integer max) {
    params.max = Math.min(max ?: 10, 100)
    respond LtiConsumer.list(params),
        model: [ltiConsumerInstanceCount: LtiConsumer.count()],
        view: SkinUtil.getView(params, session, 'index')
  }

  def show(LtiConsumer ltiConsumerInstance) {
    respond ltiConsumerInstance, view: SkinUtil.getView(params, session, 'show')
  }

  def create() {
    respond new LtiConsumer(params), view: SkinUtil.getView(params, session, 'create')
  }

  @Transactional
  def save() {
    LtiConsumer ltiConsumerInstance = new LtiConsumer(params)
    if (ltiConsumerInstance == null) {
      notFound()
      return
    }


    if (ltiConsumerInstance.hasErrors()) {
      respond ltiConsumerInstance.errors, view: SkinUtil.getView(params, session, 'create')
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
    respond ltiConsumerInstance, view: SkinUtil.getView(params, session, 'edit')
  }

  @Transactional
  def update() {
    LtiConsumer ltiConsumerInstance = new LtiConsumer(params)
    if (ltiConsumerInstance == null) {
      notFound()
      return
    }

    if (ltiConsumerInstance.hasErrors()) {
      respond ltiConsumerInstance.errors, view: SkinUtil.getView(params, session, 'edit')
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
