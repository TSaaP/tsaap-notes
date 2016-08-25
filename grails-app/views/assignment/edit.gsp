<%@ page import="org.tsaap.assignments.Schedule" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require modules="tsaap_ui_notes,tsaap_icons"/>
		<g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>
	<body>
    <div class="container context-nav" role="navigation">
        <ol class="breadcrumb">
            <li><g:link class="list" action="index"><g:message code="assignment.list.label"
                                                               args="[entityName]"/></g:link></li>
            <li class="active">${message(code: "assignment.edition.label")} ${assignment?.title}</li>
        </ol>
    </div>
		<div id="edit-assignment" class="content scaffold-edit" role="main">
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
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
                </div>
			</g:hasErrors>
			<g:form <g:form controller="assignment" action="update"> method="PUT" >
                <g:hiddenField name="version" value="${assignmentInstance?.version}" />
				<g:hiddenField name="version" value="${scheduleInstance?.version}" />
				<fieldset class="form">
                    <g:render template="assignment_form"/>
					<g:render template="schedule_form"/>
				</fieldset>
				<fieldset class="buttons">
					<g:actionSubmit class="btn btn-info" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
