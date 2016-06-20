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
    <meta name='layout' content='home'/>
    <r:require module="tsaap_ui_signin"/>
    <link rel="stylesheet" type="text/css" href="css/bootstrap-glyphicons.css"/>
</head>

<body>
<div class="container">
    <div class="col-xs-12" style="height:70px;"></div>

    <div class="row">
        <div class="col-lg-6 col-lg-offset-3">
            <g:if test='${flash.message}'>
                <div class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    ${flash.message}
                </div>
            </g:if>
            <div class="panel panel-primary" style="background-color:transparent">
                <div class="panel-heading">${message(code: "springSecurity.login.lostPassword.title")}</div>

                <div class="panel-body">
                    <g:form class="form-horizontal" action="doReset">
                        <div class="input-group">
                            <span class="input-group-addon" id="sizing-addon2">@</span>
                            <input type="text" class="form-control" placeholder="Email" name="email" id="email"
                                   aria-describedby="sizing-addon2">
                        </div>
                        <br>
                        <button class="btn btn-primary"
                                type="submit"
                                id="submit">${message(code: "springSecurity.login.lostPassword.button")}</button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>