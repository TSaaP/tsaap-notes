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
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>elaastic-questions</title>
  <meta name="layout" content="anonymous-elaastic">
  <r:require modules="semantic_ui,elaastic_ui,jquery"/>
</head>

<body>
<div class="ui icon error message">
  <i class="warning sign icon"></i>
  <div class="content">
    <g:if test="${flash.message}">
      <p>${flash.message}</p>
    </g:if>
    <g:else>
      <p>
        ${message(code: "error.apologies.text")}
      </p>
    </g:else>
    <p>
      ${message(code: "error.continueLink.message")} <g:link
          uri="/login/auth">${message(code: "error.continueLink.link")}</g:link>.
    </p>
  </div>


</div>

<g:if env="development">
  <p>
    <a href="#"
       onclick="showException()">${message(code: "error.development.errorDetail.link")}</a>
  </p>

  <div id="exception" style="display:none">
    <g:renderException exception="${exception}"/>
  </div>
</g:if>

<r:script>
  function showException() {
    $('#exception').show()
  }

</r:script>
</body>
</html>