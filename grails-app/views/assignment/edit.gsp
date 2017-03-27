<%@ page import="org.tsaap.assignments.Schedule" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div id="edit-assignment" class="container" role="main">

    <ol class="breadcrumb">
        <li><g:link class="list" action="index"><g:message code="assignment.list.label"
                                                           args="[entityName]"/></g:link></li>
        <li><g:link class="list" action="show" controller="assignment"
                    id="${assignmentInstance.id}">${message(code: "assignment.label")} ${assignmentInstance?.title}</g:link></li>
        <li class="active">${message(code: "assignment.edition.label")}</li>
    </ol>

    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">${raw(flash.message)}</div>
    </g:if>
    <g:hasErrors bean="${assignmentInstance}">
        <div class="alert alert-danger">
            <ul class="errors" role="alert">
                <g:eachError bean="${assignmentInstance}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                            error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>
    <g:hasErrors bean="${scheduleInstance}">
        <div class="alert alert-danger">
            <ul class="errors" role="alert">
                <g:eachError bean="${scheduleInstance}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                            error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>
    <g:form controller="assignment" action="update" method="PUT" id="${assignmentInstance.id}">
        <g:hiddenField name="version" value="${assignmentInstance?.version}"/>
        <fieldset class="form">
            <g:render template="assignment_form" bean="${assignmentInstance}"/>
            <g:render template="schedule_form" bean="${scheduleInstance}"/>
        </fieldset>
        <fieldset class="buttons">
            <g:actionSubmit class="btn btn-info" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
