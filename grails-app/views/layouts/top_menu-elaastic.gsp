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
<%@ page import="org.tsaap.directory.RoleEnum" %>

%{--
  Layout elaastic-skin with a top fixed menu
  --}%
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible"/>
  <meta content="width=device-width, initial-scale=1, maximum-scale=2, user-scalable=no" name="viewport"/>
  <meta name="description"
        content="${g.message(code: 'elaastic.description')}">
  <meta name="author" content="Ticetime">
  <meta content="#ffffff" name="theme-color"/>
  <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}"
        type="image/x-icon">
  <title>elaastic: <g:layoutTitle  /></title>
  <style type="text/css">[v-cloak] {
    display: none;
  }</style>
  <r:layoutResources/>
  <script type="text/javascript"
          src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>

  %{-- TODO Find out how to load CK resources only when required... --}%
  <ckeditor:resources/>
</head>

<body>

<div id="page-content" class="ui container">
  <div class="ui large top fixed inverted menu" style="background-color: #4f7691;">
    <div class="header item">
      <g:link url="${grailsApplication.config.grails.plugins.springsecurity.logout.afterLogoutUrl}">
        <img class="ui avatar bordered circular image logo"
             src="${resource(dir: 'images/elaastic/logos', file: 'Elaastic_pictoRVB.png')}"/>
      </g:link>
    </div>

    <sec:ifAnyGranted
        roles="${RoleEnum.ADMIN_ROLE.label},${RoleEnum.TEACHER_ROLE.label}">

      <div class="item">
        <g:link controller="assignment">
          <i class="book icon"></i> <g:message code="assignment.my.list.label"/>
        </g:link>
      </div>
    </sec:ifAnyGranted>

    <sec:ifAnyGranted
        roles="${RoleEnum.ADMIN_ROLE.label},${RoleEnum.STUDENT_ROLE.label}">
      <div class="item">
        <g:link controller="player">
          <i class="travel icon"></i> <g:message code="player.my.assignment.list.label"/>
        </g:link>
      </div>

    </sec:ifAnyGranted>

    <div class="right menu">
      <tsaap:ifNotLoggedIn>
        <div class="item">
          <g:link controller="login"
                  action="auth"
                  params="[skin: 'elaastic']"
                  class="ui button">
            ${message(code: "index.newUser.logIn.action")}
          </g:link>

        </div>

        <div class="item">
          <g:link class="ui primary button" controller="userAccount" action="showSuscribeForm">
            <g:message code="index.newUser.signUp.action"/>
          </g:link>
        </div>
      </tsaap:ifNotLoggedIn>
      <tsaap:ifLoggedIn>
        <g:render template="/layouts/userMenu" model="[user: user]" />
      </tsaap:ifLoggedIn>
    </div>
  </div>

  <div class="ui container" style="padding-top: 6em;">
    <g:layoutBody/>
    <g:render template="/layouts/footer-elaastic" />
  </div>
</div>

<r:layoutResources/>
</body>
</html>