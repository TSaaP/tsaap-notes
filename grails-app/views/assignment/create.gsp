<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}"/>
    <title><g:message code="assignment.creation.label" args="[entityName]"/></title>
</head>

<body>
<div class="container context-nav" role="navigation">
    <ol class="breadcrumb">
        <li><g:link class="list" action="index"><g:message code="assignment.list.label"
                                                           args="[entityName]"/></g:link></li>
        <li class="active">${message(code: message(code: "assignment.edition.label"))}</li>
    </ol>
</div>


<div id="create-assignment" class="container" role="main">
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${assignmentInstance}">
        <div class="alert alert-danger">
            <ul class="errors" role="alert">
                <g:eachError bean="${assignmentInstance}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
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
    <g:form controller="assignment" action="save">
        <fieldset class="form">
            <g:render template="assignment_form" bean="${assignmentInstance}"/>
            <g:render template="schedule_form" bean="${scheduleInstance}"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="btn btn-info"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
