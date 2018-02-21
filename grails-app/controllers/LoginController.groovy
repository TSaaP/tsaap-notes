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


import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.tsaap.skin.SkinUtil

import javax.servlet.http.HttpServletResponse

class LoginController {

  /**
   * Dependency injection for the authenticationTrustResolver.
   */
  def authenticationTrustResolver

  /**
   * Dependency injection for the springSecurityService.
   */
  def springSecurityService

  /**
   * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
   */
  def index = {
    if (springSecurityService.isLoggedIn()) {
      redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
    } else {
      redirect action: 'auth', params: params
    }
  }

  /**
   * Show the login page.
   */
  def auth = {

    def config = SpringSecurityUtils.securityConfig

    if (springSecurityService.isLoggedIn()) {
      redirect uri: config.successHandler.defaultTargetUrl
      return
    }

    String view = SkinUtil.getView(params, session, 'auth')
    String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"

//    if(params.skin) {
//      postUrl += "?skin=${params.skin}"
//    }

    render view: view,
        model: [
            postUrl            : postUrl,
            rememberMeParameter: config.rememberMe.parameter
        ]
  }

  /**
   * The redirect action for Ajax requests.
   */
  def authAjax = {
    response.setHeader 'Location', SpringSecurityUtils.securityConfig.auth.ajaxLoginFormUrl
    response.sendError HttpServletResponse.SC_UNAUTHORIZED
  }

  /**
   * Show denied page.
   */
  def denied = {
    if (springSecurityService.isLoggedIn() &&
        authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
      // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
      redirect action: 'full', params: params
    }
  }

  /**
   * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
   */
  def full = {
    def config = SpringSecurityUtils.securityConfig
    render view: 'auth', params: params,
        model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
                postUrl  : "${request.contextPath}${config.apf.filterProcessesUrl}"]
  }

  /**
   * Callback after a failed login. Redirects to the auth page with a warning message.
   */
  def authfail = {

    def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
    String msg = ''
    def exception = session[WebAttributes.AUTHENTICATION_EXCEPTION]
    if (exception) {
      if (exception instanceof AccountExpiredException) {
        msg = g.message(code: "springSecurity.errors.login.expired")
      } else if (exception instanceof CredentialsExpiredException) {
        msg = g.message(code: "springSecurity.errors.login.passwordExpired")
      } else if (exception instanceof DisabledException) {
        msg = g.message(code: "springSecurity.errors.login.disabled")
      } else if (exception instanceof LockedException) {
        msg = g.message(code: "springSecurity.errors.login.locked")
      } else {
        msg = g.message(code: "springSecurity.errors.login.fail")
      }
    }

    if(session.skin) {
      params.skin = session.skin
    }

    if (springSecurityService.isAjax(request)) {
      render([error: msg] as JSON)
    } else {
      flash.message = msg
      redirect action: 'auth', params: params
    }
  }

  /**
   * The Ajax success redirect url.
   */
  def ajaxSuccess = {
    render([success: true, username: springSecurityService.authentication.name] as JSON)
  }

  /**
   * The Ajax denied redirect url.
   */
  def ajaxDenied = {
    render([error: 'access denied'] as JSON)
  }

}
