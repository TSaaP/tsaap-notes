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


<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "tsaap.terms.title")}</title>
    <meta name="layout" content="error">
    <r:require module="tsaap_ui_home"/>
</head>

<body>
<div class="container">
    <h1>${message(code: "tsaap.terms.title")}</h1>

    <p>
        ${message(code: "tsaap.terms")}
    </p>

    <p>${message(code: "tsaap.terms.question")}</p>
    <g:link class="btn btn-info" controller="userAccount" action="ltiConnection"
            params='[agree: "true", username: params.username, contextId: params.contextId, contextName: params.contextName, displaysAll: "on"]'>${message(code: "tsaap.terms.agree")}</g:link>
    <g:link class="btn btn-default" controller="userAccount" action="ltiConnection"
            params='[agree: "false"]'>${message(code: "tsaap.terms.disagree")}</g:link>
</div>
</body>
</html>