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
    <title>${message(code: "password.reset.page.title")}</title>
    <meta name='layout' content='anonymous'/>
    <r:require module="tsaap_ui_signin"/>
</head>

<body>
<div class="container">

    <g:form class="form-signin" controller="passwordResetKey" action='resetPassword' params="[passwordResetKey: params.passwordResetKey]">
        <h3>${message(code: "password.reset.page.reset.titre")}</h3>
        <g:if test='${flash.message}'>
            <div class='alert'>${flash.message}</div>
        </g:if>

        <div class="form-group">
            <input type="password" class="input-block-level"
                   placeholder=${message(code: "springSecurity.login.password.placeholder")}
                   name='password' id='password'>
        </div>
        <div class="form-group">
            <input type="password" class="input-block-level"
                   placeholder=${message(code: "springSecurity.login.password.confirm.placeholder")}
                   name='passwordConfirm' id='passwordConfirm'>
        </div>


        <button class="btn btn-large btn-primary btn-block"
                type="submit"
                id="submit">${message(code: "password.reset.form.button")}</button>
    </g:form>

</div>

</body>
</html>
