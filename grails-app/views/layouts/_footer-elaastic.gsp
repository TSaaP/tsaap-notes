<div class="ui divider"></div>
<footer style="margin-bottom: 2em;">

  <p>&copy; Tsaap Development Group 2013 - Ticetime 2017 – Tsaap-Notes version %{--
  -
  -  Copyright (C) 2017 Ticetime
  -
  -      This program is free software: you can redistribute it and/or modify
  -      it under the terms of the GNU Affero General Public License as published by
  -      the Free Software Foundation, either version 3 of the License, or
  -      (at your option) any later version.
  -
  -      This program is distributed in the hope that it will be useful,
  -      but WITHOUT ANY WARRANTY; without even the implied warranty of
  -      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -      GNU Affero General Public License for more details.
  -
  -      You should have received a copy of the GNU Affero General Public License
  -      along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -
  --}%

<g:meta name="app.version"/> -
    <a href="${grailsApplication.config.grails.serverURL}/terms">Mentions</a></p>
  <g:if env="development">
    <sec:ifNotSwitched>
      <form class="ui form" action='${request.contextPath}/j_spring_security_switch_user' method='POST'>
        <div class="field">
          <label>Switch to user:</label>
          <div class="two fields">
            <div class="field">
              <input type='text' name='j_username'/>
            </div>
            <div class="field">
              <input class="ui button" type='submit' value='Switch'/>
            </div>
          </div>
        </div>
      </form>
    </sec:ifNotSwitched>
    <sec:ifSwitched>
      <a href='${request.contextPath}/j_spring_security_exit_user'>
        Resume as <sec:switchedUserOriginalUsername/>
      </a>
    </sec:ifSwitched>
  </g:if>

</footer>
<div class="ui hidden divider"></div>