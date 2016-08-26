<%@ page import="org.tsaap.assignments.Assignment; org.tsaap.assignments.Schedule" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: message(code:"assignment.label"), default: 'Assignment')}"/>
    <title><g:message code="assignment.list.label" args="[entityName]"/></title>
</head>

<body>
<div class="container">
    <ol class="breadcrumb">
        <li class="active"><g:message code="assignment.list.label" args="[entityName]"/></li>
    </ol>
    <g:link class="btn btn-primary btn-sm pull-right" action="create"><span
            class="glyphicon glyphicon-plus"></span>${message(code: 'assignment.index.addAssignment.button')}</g:link>
</div>

<div id="list-assignment" class="container">

    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">${flash.message}</div>
    </g:if>
    <table class="table table-striped table-hover">
        <thead>
        <tr>


            <g:sortableColumn property="title"
                              title="${message(code: "assignment.label")}"/>

            <th><g:message code="schedule.startdate.label" default="Start date"/></th>
            <th><g:message code="schedule.enddate.label" default="End date"/></th>

            <g:sortableColumn property="lastUpdated"
                              title="${message(code: "assignment.lastupdated.label")}"/>

        </tr>
        </thead>
        <tbody>
        <g:each in="${assignmentInstanceList}" status="i" var="assignmentInstance">
            <g:set var="scheduleInstance" value="${assignmentInstance.schedule}"/>
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show"
                            id="${assignmentInstance.id}">${fieldValue(bean: assignmentInstance, field: "title")}</g:link></td>

                <td><g:formatDate date="${scheduleInstance?.startDate}"/></td>

                <td><g:formatDate date="${scheduleInstance?.endDate}"/></td>

                <td><g:formatDate date="${assignmentInstance.lastUpdated}"/></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="note-list-pagination">
        <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${assignmentInstanceCount ?: 0}"/>
    </div>
</div>
</div>
</body>
</html>
