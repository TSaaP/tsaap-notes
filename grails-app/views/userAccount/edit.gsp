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
                                           placeholder="${message(code: "useraccount.form.firstName.placeholder")}">
                                </div>

                                <div class="col-lg-5">
                                    <input type="text" class="form-control" id="lastName"
                                           name="lastName"
                                           value="${fieldValue(bean: user, field: 'lastName')}"
                                           placeholder="${message(code: "useraccount.form.lastName.placeholder")}">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <input type="text" class="form-control" id="email" name="email"
                                   placeholder="${message(code: "useraccount.form.email.placeholder")}"
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
                                   id="username" placeholder="${message(code: "useraccount.form.username.placeholder")}"
                                   name="username"
                                   value="${fieldValue(bean: user, field: 'username')}">
                        </div>


                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password" placeholder="${message(code: "useraccount.form.password.placeholder")}"
                                   name="password">
                        </div>

                        <div class="form-group">
                            <input type="password" class="form-control"
                                   id="password2"
                                   placeholder="${message(code: "useraccount.form.confirmPassword.placeholder")}"
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