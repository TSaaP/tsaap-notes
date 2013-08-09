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

class UserAccountController {

  UserAccountService userAccountService

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
    Role mainRole = RoleEnum.valueOf(RoleEnum,params.role).role
    User user = new User(params)
    user = userAccountService.addUser(user,mainRole, true)
    if (user.hasErrors()) {
      render(view: '/index', model: [user:user])
    } else {
      flash.message = message(code: 'subscription.success')
      redirect(uri: '/login/auth')
    }

  }
}
