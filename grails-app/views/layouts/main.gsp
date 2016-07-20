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

<%@ page import="org.tsaap.notes.FilterReservedValue" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description"
          content="Tsaap Notes - microblogging for learners and teachers">
    <meta name="author" content="Tsaap Development Group">
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
    <title><g:layoutTitle default="TsaaP-Notes"/></title>
    <r:layoutResources/>
    <ckeditor:resources/>
    <script type="text/javascript"
            src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
    </script>
</head>

<body>
<g:layoutBody/>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="${grailsApplication.config.grails.serverURL}">TsaaP-Notes</a>

        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li id="mainLinkContexts"><g:link controller="context"
                                                  params="[filter: FilterReservedValue.__MINE__.name()]">${message(code: "layout.main.scope")}</g:link></li>
            </ul>

            <div class="navbar-collapse collapse pull-right">
                <ul class="nav navbar-nav">
                    <tsaap:ifLoggedIn>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown"><sec:username/>
                                <span class="glyphicon glyphicon-cog"></span> <b class="caret"></b></a>
                            <ul class="dropdown-menu">
                                <li><g:link controller="userAccount"
                                            action="doEdit">${message(code: "layout.main.account")}</g:link></li>
                                <li class="divider"></li>
                                <li><g:link controller="settings"
                                            action="doSettings">${message(code: "layout.main.settings")}</g:link></li>
                                <li class="divider"></li>
                                <li><g:link controller="logout">${message(code: "layout.main.disconnect")}</g:link></li>
                            </ul>
                        </li>
                    </tsaap:ifLoggedIn>
                </ul>
            </div>
        </div><!--/.nav-collapse -->
    </div>
</div>
<r:layoutResources/>
<footer class="container">
    <p>&copy; Tsaap Development Group 2013 - Tsaap-Notes version <g:meta name="app.version"/> - <a
            href="${grailsApplication.config.grails.serverURL}/terms">Mentions</a></p>
    <g:if env="development">
        <sec:ifNotSwitched>
            <form action='${request.contextPath}/j_spring_security_switch_user' method='POST'>
                Switch to user: <input type='text' name='j_username'/>
                <input type='submit' value='Switch'/>
            </form>
        </sec:ifNotSwitched>
        <sec:ifSwitched>
            <a href='${request.contextPath}/j_spring_security_exit_user'>
                Resume as <sec:switchedUserOriginalUsername/>
            </a>
        </sec:ifSwitched>
    </g:if>
    <p></p>
</footer>
</body>
</html>