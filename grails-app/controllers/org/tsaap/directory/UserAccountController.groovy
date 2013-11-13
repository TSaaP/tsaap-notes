/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tsaap.directory

import grails.plugins.springsecurity.Secured
import grails.plugins.springsecurity.SpringSecurityService
import org.gcontracts.PreconditionViolation

class UserAccountController {

  UserAccountService userAccountService
  SpringSecurityService springSecurityService

  /**
   * Action allowing a user to subscribe
   * @param firsName the first name given by the user
   * @param lastName the last name given by the user
   * @param username the username given by the user
   * @param email the email given by the user
   * @param password the password given by the user
   * @param role the role given by the user
   */
  def doSubscribe() {
    Role mainRole = RoleEnum.valueOf(RoleEnum, params.role).role
    User user = new User(params)
    if (params.password == params.password2) {
      user = userAccountService.addUser(user, mainRole, true)
    } else {
      user.errors.rejectValue('password', 'user.password.confirm.fail', 'The two passwords must be the same.')
    }

    if (user.hasErrors()) {
      render(view: '/index', model: [user: user])
    } else {
      flash.message = message(code: 'subscription.success')
      redirect(uri: '/login/auth')
    }
  }

  /**
   * Action allowing a update a user
   * @param firsName the first name given by the user
   * @param lastName the last name given by the user
   * @param username the username given by the user
   * @param email the email given by the user
   * @param password the password given by the user
   * @param role the role given by the user
   */
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def doUpdate() {
    User user = springSecurityService.currentUser
    if ((params.password || params.password2) && (params.password != params.password2)) {
      user.errors.rejectValue('password', 'user.password.confirm.fail', 'The two passwords must be the same.')
    } else {
      if (!params.password) {
        params.remove('password')
      }
      Role mainRole = RoleEnum.valueOf(RoleEnum, params.role).role
      user.properties = params
      user = userAccountService.updateUser(user, mainRole)
    }
    if (user.hasErrors()) {
      render(view: '/userAccount/edit', model: [user: user])
    } else {
      flash.message = message(code: 'useraccount.update.success')
      redirect(uri: '/userAccount/doEdit')
    }
  }

  /**
   *
   * Go to the edit page
   */
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def doEdit() {
    render(view: '/userAccount/edit', model: [user: springSecurityService.currentUser])
  }

  /**
   * Unsubscribe the current user
   * @return
   */
  @Secured(['IS_AUTHENTICATED_FULLY'])
  def doUnsubscribe() {
    User user = springSecurityService.currentUser
    userAccountService.disableUser(user)
    redirect(uri: '/logout')
  }

  /**
   * Enable a user
   * @param actKey the activation key
   * @param id the user id
   */
  def doEnableUser() {
    def strKey = params.actKey
    def id = params.id
    User user = User.get(params.id)
    ActivationKey actKey = ActivationKey.findByUserAndActivationKey(user,strKey)
    try {
      userAccountService.enableUserWithActivationKey(user,actKey)
    } catch (PreconditionViolation e) {
      flash.message = message(code:'useraccount.activation.failure' )
      throw e
    }
    flash.message = message(code: 'useraccount.update.success')
    redirect (uri:'/login/auth')
  }

}
