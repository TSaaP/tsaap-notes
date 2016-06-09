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
  Date: 18/05/16
  Time: 08:40
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>
<div class="container context-nav">

    <ol class="breadcrumb">
        <li class="active">${message(code: "settings.title")}</li>
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

        <span class="glyphicon glyphicon-globe" aria-hidden="true"></span><label>&nbsp;Notifications</label>
        <hr>

        <!-- Example row of columns -->
        <div class="row">

            <div class="col-lg-6">
                <g:form controller="settings" action="doUpdate">
                    <fieldset>
                        <div class="checkbox">
                            <label>
                                <!-- <input type="checkbox" checked="checked" value="dailyNotification"> -->
                                <g:checkBox name="dailyNotification" value="${user.settings.dailyNotifications}"/>
                                <span data-toggle="tooltip" data-html="true"
                                      title="${message(code: "settings.checkbox.dailyNotification.message")}"
                                      data-placement="top">
                                    ${message(code: "settings.checkbox.dailyNotification.label")}
                                </span>
                            </label>
                        </div>

                        <div class="checkbox">
                            <label>
                                <!--<input type="checkbox" checked="checked" value="mentionNotification"> -->
                                <g:checkBox name="mentionNotification" value="${user.settings.mentionNotifications}"/>
                                <span data-toggle="tooltip" data-html="true"
                                      title="${message(code: "settings.checkbox.mentionNotification.message")}"
                                      data-placement="top">
                                    ${message(code: "settings.checkbox.mentionNotification.label")}
                                </span>
                            </label>
                        </div>

                        <div class="form-group">
                            <label>${message(code: "useraccount.form.language.label")} : <g:select name="language"
                                                                                                   from="${org.tsaap.directory.UserAccountService.LANGUAGES_SUPPORTED.values()}"
                                                                                                   value="${fieldValue(bean: user.settings, field: 'language')}"></g:select></label>
                        </div>

                        <button type="submit"
                                class="btn btn-primary">${message(code: "useraccount.save.button")} &raquo;</button><br/>

                    </fieldset>
                </g:form>

            </div>

        </div>

    </div>
    <hr>
</div> <!-- /container -->
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>

</body>
</html>