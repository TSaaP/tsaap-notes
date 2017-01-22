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
            <g:link class="navbar-brand" uri="/">TsaaP-Notes</g:link>
        </div>

        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="https://github.com/TSaaP/tsaap-notes"
                       target="_blank">${message(code: "layout.home.github")}</a></li>
                <li><a href="https://github.com/TSaaP/tsaap-notes/issues?state=open"
                       target="_blank">${message(code: "layout.home.bug")}</a></li>
                <li><a href="http://tsaap.github.io/tsaap-notes/"
                       target="_blank">${message(code: "layout.home.documentation")}</a></li>
                <li><a href="mailto:franck.silvestre@irit.fr">Contact</a></li>
                %{--<li class="dropdown">--}%
                %{--<a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b--}%
                %{--class="caret"></b></a>--}%
                %{--<ul class="dropdown-menu">--}%
                %{--<li><a href="#">Action</a></li>--}%
                %{--<li><a href="#">Another action</a></li>--}%
                %{--<li><a href="#">Something else here</a></li>--}%
                %{--<li class="divider"></li>--}%
                %{--<li class="nav-header">Nav header</li>--}%
                %{--<li><a href="#">Separated link</a></li>--}%
                %{--<li><a href="#">One more separated link</a></li>--}%
                %{--</ul>--}%
                %{--</li>--}%
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <tsaap:ifNotLoggedIn>
                    <li><g:link controller="login"
                                action="auth">${message(code: "layout.home.signIn")} &raquo;</g:link></li>
                </tsaap:ifNotLoggedIn>
                <tsaap:ifLoggedIn>
                    <sec:ifAnyGranted
                            roles="${org.tsaap.directory.RoleEnum.ADMIN_ROLE.label},${org.tsaap.directory.RoleEnum.TEACHER_ROLE.label}">
                        <li id="mainLinkAssignments"><g:link
                                controller="assignment">${message(code: "layout.home.welcome")} @<sec:username/> !</g:link></li>
                    </sec:ifAnyGranted>
                    <sec:ifAnyGranted
                            roles="${org.tsaap.directory.RoleEnum.ADMIN_ROLE.label},${org.tsaap.directory.RoleEnum.STUDENT_ROLE.label}">
                        <li id="mainLinkPlayer"><g:link
                                controller="player">${message(code: "layout.home.welcome")} @<sec:username/> !</g:link></li>
                    </sec:ifAnyGranted>
                </tsaap:ifLoggedIn>
            </ul>
        </div>
    </div>
</div>
<r:layoutResources/>

<footer class="container">
    <p>&copy; Tsaap Development Group 2013 - Tsaap-Notes version <g:meta name="app.version"/> - <a
            href="${grailsApplication.config.grails.serverURL}/terms">Mentions</a></p>
</footer>
</body>
</html>