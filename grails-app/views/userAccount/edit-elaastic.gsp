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

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui"/>
</head>

<body>
<div style="max-width: 800px; margin: auto;">
  <h2 class="ui header">
    <i class="user icon"></i>

    <div class="content">
      ${message(code: "useraccount.title")}
    </div>
  </h2>

  <g:if test="${flash.message}">
    <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
  </g:if>

  <g:if test="${user?.hasErrors()}">
    <div class="ui error message">
      <ul class="list">
        <g:eachError bean="${user}">
          <li><g:message error="${it}"/></li>
        </g:eachError>
      </ul>
    </div>
  </g:if>

  <div class="ui segment">

    <g:form class="ui form" controller="userAccount" action="doUpdate">
      <div class="required field">
        <label>
          <g:message code="useraccount.form.name"/>
        </label>

        <div class="two fields">
          <div class="required field">
            <input type="text"
                   id="firstName"
                   name="firstName"
                   value="${fieldValue(bean: user, field: 'firstName')}"
                   placeholder="${message(code: "useraccount.form.firstName.placeholder")}">
          </div>

          <div class="required field">
            <input type="text"
                   id="lastName"
                   name="lastName"
                   value="${fieldValue(bean: user, field: 'lastName')}"
                   placeholder="${message(code: "useraccount.form.lastName.placeholder")}">
          </div>
        </div>
      </div>

      <div class="required field">
        <label>
          <g:message code="useraccount.form.email"/>
        </label>
        <input type="text"
               id="email"
               name="email"
               placeholder="${message(code: "useraccount.form.email.placeholder")}"
               value="${fieldValue(bean: user, field: 'email')}">
      </div>

      <div class="ui hidden divider"></div>

      <div class="inline fields">
        <label><g:message code="useraccount.profile.label"/></label>

        <div class="field">
          <div class="ui radio checkbox">
            <g:radio name="role"
                     value="STUDENT_ROLE"
                     id="STUDENT_ROLE"
                     checked="${user.isLearner()}"/>
            <label for="STUDENT_ROLE">${message(code: "useraccount.form.learner.radio")}</label>
          </div>
        </div>

        <div class="field">
          <div class="ui radio checkbox">
            <g:radio name="role" value="TEACHER_ROLE" id="TEACHER_ROLE"
                     checked="${user.isTeacher()}"/>
            <label for="TEACHER_ROLE">${message(code: "useraccount.form.teacher.radio")}</label>
          </div>
        </div>
      </div>

      <div class="required field">
        <label>
          <g:message code="useraccount.username.label"/>
        </label>
        <input type="text"
               id="username" placeholder="${message(code: "useraccount.form.username.placeholder")}"
               name="username"
               value="${fieldValue(bean: user, field: 'username')}">
      </div>

      <div class="ui hidden divider"></div>

      <div class="field">
        <label>
          <g:message code="useraccount.form.password"/>
        </label>
        <input type="password"
               id="password" placeholder="${message(code: "useraccount.form.password.placeholder")}"
               name="password">
      </div>

      <div class="field">
        <input type="password"
               id="password2"
               placeholder="${message(code: "useraccount.form.confirmPassword.placeholder")}"
               name="password2">
      </div>

      <button type="submit"
              class="ui primary button">
        ${message(code: "useraccount.save.button")} &raquo;
      </button>
    </g:form>

  </div>

  <div class="ui segment">
    <g:link action="doUnsubscribe"
            controller="userAccount"
            class="ui negative button">
      ${message(code: "useraccount.unsubscribe.button")}
    </g:link>
  </div>


  <r:script>
    $(document)
        .ready(function () {

      $('.message .close')
          .on('click', function () {
        $(this)
            .closest('.message')
            .transition('fade')
        ;
      });

    });

  </r:script>
</div>
</body>
</html>