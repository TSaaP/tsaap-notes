%{--
  - Copyright 2013 Tsaap Development Group
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%




<!DOCTYPE html>
<html lang="en">
<head>
  <meta name="layout" content="main"/>
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>

<div class="container context-nav">

  <ol class="breadcrumb">
    <li class="active">${message(code: "useraccount.title")}</li>
  </ol>

  <div class="body-content">
    <g:if test="${flash.message}">
      <div class="alert alert-info" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${user?.hasErrors()}">
      <div class="alert">
        <g:eachError bean="${user}">
          <li><g:message error="${it}"/></li>
        </g:eachError>
      </div>
    </g:if>
  <!-- Example row of columns -->
    <div class="row">

      <div class="col-lg-6">
        <g:form controller="userAccount" action="doUpdate">
          <fieldset>

            <div class="form-group">
              <div class="row">
                <div class="col-lg-5">
                  <input type="text" class="form-control" id="firstName"
                         name="firstName"
                         value="${fieldValue(bean: user, field: 'firstName')}"
                         placeholder=${message(code: "useraccount.form.firstName.placeholder")}>
                </div>

                <div class="col-lg-5">
                  <input type="text" class="form-control" id="lastName"
                         name="lastName"
                         value="${fieldValue(bean: user, field: 'lastName')}"
                         placeholder=${message(code: "useraccount.form.lastName.placeholder")}>
                </div>
              </div>
            </div>

            <div class="form-group">
              <input type="text" class="form-control" id="email" name="email"
                     placeholder=${message(code: "useraccount.form.email.placeholder")}
                     value="${fieldValue(bean: user, field: 'email')}">
            </div>

            <div class="form-group">
              <label class="radio-inline">
                <g:radio name="role" value="STUDENT_ROLE" id="STUDENT_ROLE"
                         checked="${user.isLearner()}"/>
                ${message(code: "useraccount.form.learner.radio")}
              </label>
              <label class="radio-inline">
                <g:radio name="role" value="TEACHER_ROLE" id="TEACHER_ROLE"
                         checked="${user.isTeacher()}"/> ${message(code: "useraccount.form.teacher.radio")}
              </label>

            </div>

            <div class="form-group">
              <input type="text" class="form-control"
                     id="username" placeholder=${message(code: "useraccount.form.username.placeholder")}
                     name="username"
                     value="${fieldValue(bean: user, field: 'username')}">
            </div>


            <div class="form-group">
              <input type="password" class="form-control"
                     id="password" placeholder=${message(code: "useraccount.form.password.placeholder")} name="password">
            </div>

            <div class="form-group">
              <input type="password" class="form-control"
                     id="password2" placeholder=${message(code: "useraccount.form.confirmPassword.placeholder")}
                     name="password2">
            </div>


            <button type="submit"
                    class="btn btn-primary">${message(code: "useraccount.save.button")} &raquo;</button><br/>

          </fieldset>
        </g:form>

      </div>

    </div>

  </div>

</div> <!-- /container -->
<div class="container">
  <hr>
  <g:link action="doUnsubscribe" controller="userAccount"
          class="btn btn-danger">${message(code: "useraccount.unsubscribe.button")}</g:link>
  <hr>
</div>

</body>
</html>