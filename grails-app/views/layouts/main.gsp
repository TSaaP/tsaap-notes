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
    <script src="https://unpkg.com/vue"></script>
    <script src="https://cdn.jsdelivr.net/vue.resource/1.3.1/vue-resource.min.js"></script>
</head>

<body>
<g:layoutBody/>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse"
                    data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="${grailsApplication.config.grails.serverURL}">TsaaP-Notes</a>
        </div>

        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <sec:ifAnyGranted
                        roles="${org.tsaap.directory.RoleEnum.ADMIN_ROLE.label},${org.tsaap.directory.RoleEnum.TEACHER_ROLE.label}">
                    <li id="mainLinkAssignments"><g:link
                            controller="assignment">${message(code: "assignment.list.label")}</g:link></li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted
                        roles="${org.tsaap.directory.RoleEnum.ADMIN_ROLE.label},${org.tsaap.directory.RoleEnum.STUDENT_ROLE.label}">
                    <li id="mainLinkPlayer"><g:link
                            controller="player">${message(code: "player.assignment.list.label")}</g:link></li>
                </sec:ifAnyGranted>
                <li><a href="http://tsaap.github.io/tsaap-notes/"
                       target="_blank">${message(code: "layout.home.documentation")}</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
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
        </div><!--/.nav-collapse -->
    </div>
</div>
<footer class="container">
    <hr/>

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
</footer>
<r:layoutResources/>
</body>
</html>