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
  Date: 24/05/16
  Time: 11:41
--%>

<!DOCTYPE html>
<html lang="en">
<head>
  <title>${message(code: "settings.unsubscribedDaily.page.title")}</title>
  <meta name="layout" content="anonymous-elaastic">
  <r:require modules="semantic_ui,elaastic_ui,jquery"/>
</head>

<body>
<div class="ui container">

  <div class="ui message">
    <p>${message(code: "settings.unsubscribedMention.message")}</p>
  </div>

</div>

</body>
</html>