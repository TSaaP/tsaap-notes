%{--
  - Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
  -
  -     This program is free software: you can redistribute it and/or modify
  -     it under the terms of the GNU Affero General Public License as published by
  -     the Free Software Foundation, either version 3 of the License, or
  -     (at your option) any later version.
  -
  -     This program is distributed in the hope that it will be useful,
  -     but WITHOUT ANY WARRANTY; without even the implied warranty of
  -     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -     GNU Affero General Public License for more details.
  -
  -     You should have received a copy of the GNU Affero General Public License
  -     along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<!DOCTYPE html>
<html lang="en">
<head>
    <title>${message(code: "springSecurity.login.page.title")}</title>
    <meta name='layout' content='home'/>
    <r:require module="tsaap_ui_signin"/>
</head>

<body>
<div class="container">

    <form class="form-signin" action='${postUrl}' id='loginForm' method="post">
        <h3>${message(code: "springSecurity.login.connexion.title")}</h3>
        <g:if test='${flash.message}'>
            <div class='alert'>${flash.message}</div>
        </g:if>
        <div class="form-group">
            <input type="text" class="input-block-level"
                   placeholder=${message(code: "springSecurity.login.username.placeholder")}
                   autofocus name='j_username' id='username'>
        </div>

        <div class="form-group">
            <input type="password" class="input-block-level"
                   placeholder=${message(code: "springSecurity.login.password.placeholder")}
                   name='j_password' id='password'>
        </div>

        <div class="form-group">
            <label class="checkbox">
                <g:if test="${hasCookie}">
                    <input type="checkbox" value="remember-me"
                           name='${rememberMeParameter}'
                           id='remember_me'
                           checked='checked'/> ${message(code: "springSecurity.login.rememberMe.checkbox")}
                </g:if>
                <g:else>
                    <input type="checkbox" value="remember-me"
                           name='${rememberMeParameter}'
                           id='remember_me'/> ${message(code: "springSecurity.login.rememberMe.checkbox")}
                </g:else>
            </label>
        </div>
        <button class="btn btn-large btn-primary btn-block"
                type="submit"
                id="submit">${message(code: "springSecurity.login.button")}</button>
    </form>
    <center>
        <g:link action="statistics" controller="login">
            ${message(code: 'login.lost.password.message')}
        </g:link>
    </center>


</div>

</body>
</html>
