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
  <title>${message(code: "passwordReset.page.title")}</title>
  <meta name="layout" content="anonymous-elaastic">
  <r:require modules="semantic_ui,elaastic_ui,jquery"/>
</head>

<body>
<div class="ui container">

  <g:form class="ui form"
          controller="passwordResetKey"
          action='resetPassword'
          params="[passwordResetKey: params.passwordResetKey]">
    <h3>${message(code: "passwordReset.page.reset.titre")}</h3>
    <g:if test='${flash.message}'>
      <div class='ui warning message'>${flash.message}</div>
    </g:if>

    <div class="field">
      <input type="password"
             class="input-block-level"
             placeholder="${message(code: 'springSecurity.login.password.placeholder')}"
             name='password'
             id='password'>
    </div>

    <div class="field">
      <input type="password"
             class="input-block-level"
             placeholder="${message(code: 'springSecurity.login.password.confirm.placeholder')}"
             name='passwordConfirm'
             id='passwordConfirm'>
    </div>


    <button class="ui primary button"
            type="submit"
            id="submit">${message(code: "passwordReset.form.button")}</button>
  </g:form>

</div>

</body>
</html>
