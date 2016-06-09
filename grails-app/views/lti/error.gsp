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
  User: dorian
  Date: 01/07/15
  Time: 09:45
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Tsaap-Notes</title>
    <meta name="layout" content="error">
    <r:require module="tsaap_ui_home"/>
</head>

<body>
<div class="container">
    <br>

    <div class="alert">
        <p>${message(code: exception.message)}</p>
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