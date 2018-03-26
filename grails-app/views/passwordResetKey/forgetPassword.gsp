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

<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 13/06/16
  Time: 15:24
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>${message(code: "passwordReset.request.page.title")}</title>
  <meta name="layout" content="anonymous-elaastic">
  <r:require modules="semantic_ui,elaastic_ui,jquery"/>
</head>

<body>
<div class="ui container" style="padding-top: 5em;">

  <g:if test='${flash.message}'>
    <div class="ui error message">
      <i class="exclamation icon"></i> ${flash.message}
    </div>
  </g:if>

  <h2 class="ui block top attached header">${message(code: "springSecurity.login.lostPassword.title")}</h2>

  <div class="ui bottom attached segment">

    <g:form class="ui horizontal form" action="doReset">
      <div class="field">
        <div class="ui left icon input">
          <input type="text" placeholder="Email" name="email" id="email" aria-describedby="sizing-addon2">
          <i class="at icon"></i>
        </div>

      </div>
      <br>
      <button class="ui primary button"
              type="submit"
              id="submit">${message(code: "springSecurity.login.lostPassword.button")}</button>
    </g:form>

  </div>

</div>
</body>
</html>