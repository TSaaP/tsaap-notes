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

package org.tsaap.directory


import grails.plugins.springsecurity.SpringSecurityService
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.tsaap.skin.SkinUtil


class UserAccountBatchCreationController {

  UserAccountService userAccountService
  SpringSecurityService springSecurityService

  /**
   * Displays form to upload csv file
   */
  def index() {
    render(
        view: "/directory/" + SkinUtil.getView(params, session, 'userAccountBatchCreation'),
        model: [user: springSecurityService.currentUser]
    )
  }

  /**
   * Processes the uploaded csv file
   */
  def doProcessCSVFile() {
    User user = springSecurityService.currentUser
    CommonsMultipartFile file = request.getFile('csvFile')
    List<User> userList
    if (file && !file.isEmpty()) {
      InputStreamReader reader = new InputStreamReader(file.inputStream)
      try {
        userList = userAccountService.addUserListFromFileByOwner(reader, user)
        response.setContentType("text/csv")
        response.setCharacterEncoding("UTF-8")
        response.setHeader("Content-Disposition", "Attachment;Filename=\"${message(code: 'useraccount.generatedFileName')}\"")
        render(view: "/directory/userAccountsCreated", model: [userList: userList])
      } catch (Exception e) {
        log.error(e.message)
        flash.message = "useraccount.batchCreation.errorMessage"
        redirect(action: "index", controller: "userAccountBatchCreation")
      } finally {
        try {
          reader.close()
        } catch (Exception e1) {
          log.error(e1.message)
        }
      }
    }
  }

}
