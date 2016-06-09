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
  User: fsil
  Date: 18/11/13
  Time: 11:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.checking.page.title", locale: new Locale(user.language))}</title>
</head>

<body>
<p>${message(code: "email.hi", locale: new Locale(user.language))} ${user.first_name}</p>

<p>
    ${message(code: "email.checking.message", locale: new Locale(user.language))}<br><br>
    <g:createLink absolute="true"
                  params='[actKey: "${actKey}", id: "${user.user_id}"]' action="doEnableUser"
                  controller="userAccount"/>
</p>
</body>
</html>