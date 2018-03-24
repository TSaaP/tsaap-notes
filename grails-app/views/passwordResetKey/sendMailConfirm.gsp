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
  Date: 16/06/16
  Time: 15:28
--%>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>${message(code: "settings.unsubscribedMention.page.title")}</title>
  <meta name="layout" content="anonymous-elaastic">
  <r:require modules="semantic_ui,elaastic_ui,jquery"/>
</head>

<body>
<div class="ui container">

  <g:if test='${flash.message}'>

    <div class="ui message">
      <i class="exclamation icon"></i>
      ${flash.message}
    </div>
  </g:if>
  <g:link class="ui primary button"
          controller="passwordResetKey"
          action="goIndex">${message(code: "password.redirect.home.page")}</g:link>

</div>

</body>
</html>