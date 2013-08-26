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

package org.tsaap.taglibs

import grails.plugins.springsecurity.SpringSecurityService
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class SimpleSecurityTagLib {

  //static defaultEncodeAs = 'html'
  //static encodeAsForTags = [tagName: 'raw']
  static namespace = "tsaap"

  SpringSecurityService springSecurityService

  /**
   * Renders the body if the user is authenticated.  */
  def ifLoggedIn = { attrs, body ->
    if (springSecurityService.isLoggedIn()) {
      out << body()
    }
  }

  /**
   * Renders the body if the user is not authenticated.  */
  def ifNotLoggedIn = { attrs, body ->
    if (!springSecurityService.isLoggedIn()) {
      out << body()
    }
  }

}
