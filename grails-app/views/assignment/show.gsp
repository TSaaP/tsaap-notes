
<%@ page import="org.tsaap.assignments.Schedule" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<r:require modules="tsaap_ui_notes,tsaap_icons"/>
		<g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}" />
		<title><g:message code="assignment.label" args="[entityName]" /></title>
	</head>
	<body>

	<div class="container context-nav" role="navigation">
		<ol class="breadcrumb">
			<li><g:link class="list" action="index"><g:message code="assignment.list.label"
															   args="[entityName]"/></g:link></li>
			<li class="active">${message(code: 'assignment.label')} "${assignmentInstance?.title}"</li>
		</ol>
	</div>

		<div id="show-assignment" class="container" role="main">
			<g:if test="${flash.message}">
                <div class="alert alert-info" role="status">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    ${flash.message}
                </div>
			</g:if>
            <g:set var="scheduleInstance" value="${assignmentInstance.schedule}"/>
			<ol class="property-list assignment">

                <g:if test="${assignmentInstance?.title}">
                    <li class="fieldcontain">
                        <span id="assignment-label" class="property-label"><g:message code="assignment.title.label" default="Title" /></span>

                        <span class="property-value" aria-labelledby="assignment-label">${assignmentInstance.title}</span>

                    </li>
                </g:if>
			
				<g:if test="${scheduleInstance?.startDate}">
				<li class="fieldcontain">
					<span id="startDate-label" class="property-label"><g:message code="schedule.startdate.label" default="Start Date" /></span>
					
						<span class="property-value" aria-labelledby="startDate-label"><g:formatDate date="${scheduleInstance?.startDate}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${scheduleInstance?.endDate}">
				<li class="fieldcontain">
					<span id="endDate-label" class="property-label"><g:message code="schedule.enddate.label" default="End Date" /></span>
					
						<span class="property-value" aria-labelledby="endDate-label"><g:formatDate date="${scheduleInstance?.endDate}" /></span>
					
				</li>
				</g:if>


			</ol>

			<g:form action="delete" controller="assignment" id="${assignmentInstance.id}" method="DELETE">
				<fieldset class="buttons">
					<g:link class="btn btn-primary" action="edit" resource="${assignmentInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="btn btn-default" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
