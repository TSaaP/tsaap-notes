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

<%@ page import="org.tsaap.notes.Context" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName"
           value="${message(code: 'context.label', default: 'Context')}"/>
    <title>Tsaap Notes - <g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div class="container context-nav" role="navigation">
    <ol class="breadcrumb">
        <li><g:link class="list" action="index"><g:message code="default.list.label"
                                                           args="[entityName]"/></g:link></li>
        <li class="active">${message(code: "context.create.li.active")}</li>
    </ol>
</div>

<div id="create-context" class="container" role="main">
    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${context}">
        <div class="alert alert-danger">
            <ul class="errors" role="alert">
                <g:eachError bean="${context}" var="error">
                    <li
                        <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                            error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>
    <g:form url="[resource: context, action: 'save']">
        <fieldset class="form">
            <g:render template="form" model="[context: context ?: new Context(owner: user)]"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="btn btn-default"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
<r:script>
    $(".nav li").removeClass('active');
    $("#mainLinkContexts").addClass('active');
</r:script>
</body>
</html>
