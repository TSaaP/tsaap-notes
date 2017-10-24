%{--
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

<!DOCTYPE html>
<html lang="en">
<head>
  <title>${message(code: "springSecurity.login.page.title")}</title>
  <meta name='layout' content='elaastic-minimal'/>
  <r:require modules="semantic_ui,elaastic_ui,jquery"/>
</head>

<body>
<div class="ui middle aligned center aligned grid" style="padding-top: 8em;">
  <div class="column left aligned">
    <form action='${postUrl}' id='loginForm' method="post" class="ui large form">

      <div class="ui segments">

        <g:if test="${params.justRegistered && params.checkEmail != 'true'}">
          <div class="ui positive small message">
            <g:message code="subscription.success"/>
          </div>
        </g:if>
        <g:if test="${params.justRegistered && params.checkEmail == 'true'}">
          <div class="ui positive small center message">
            <g:message code="subscription.success.email_to_be_checked"/>
          </div>
        </g:if>

        <div class="ui segment">
          <img src="../images/elaastic/logos/elaastic_logoRVB.png" class="ui small image"/>
        </div>

        <div class="ui segment">

          <div class="field">
            <div class="ui left icon input">
              <i class="user icon"></i>
              <input type="text"
                     placeholder="${message(code: 'springSecurity.login.username.placeholder')}"
                     autofocus
                     name='j_username'
                     id='username'/>
            </div>
          </div>

          <div class="field">
            <div class="ui left icon input">
              <i class="lock icon"></i>
              <input type="password"
                     placeholder="${message(code: 'springSecurity.login.password.placeholder')}"
                     name='j_password'
                     id='password'/>
            </div>
          </div>

          <button type="submit" id="submit" class="ui fluid large primary submit button">
            ${message(code: "springSecurity.login.button")}
          </button>
        </div>

        <div class="ui secondary segment center aligned">
          <div class="ui list">
            <div class="item">
              Vous n'avez pas de compte? <g:link controller="userAccount" action="showSuscribeForm">
                <g:message code="index.newUser.signUp.action" />
              </g:link>
            </div>

            <div class="item">
              <g:link controller="passwordResetKey" action="doForget">
                ${message(code: 'springSecurity.login.lostPassword.message')}
              </g:link>
            </div>
          </div>

        </div>
      </div>

      <g:if test='${flash.message}'>
        <div class="ui negative message">
          ${flash.message}
        </div>
      </g:if>

    </form>
  </div>
</div>

%{-- Specific login style --}%
<style type="text/css">

body {
  background-image: url('../images/elaastic/elaastic_background.jpg');
  background-repeat: no-repeat;
  background-size: cover;
}

body > .ui.grid {
  height: 100%;
  background-color: rgba(0, 0, 0, .45);
  margin-top: 0;
  margin-bottom: 0;
}

.column {
  max-width: 450px;
}

.ui.segments > .segment {
  border-top: none;
}

</style>

</body>
</html>
