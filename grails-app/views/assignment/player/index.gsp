<%@ page import="org.tsaap.assignments.Assignment" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: message(code: "player.assignment.label"), default: 'Work')}"/>
    <title><g:message code="player.assignment.list.label" args="[entityName]"/></title>
</head>

<body>

<div id="list-assignment" class="container">

    <ol class="breadcrumb">
        <li class="active"><g:message code="player.assignment.list.label" args="[entityName]"/></li>
    </ol>

    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">${flash.message}</div>
    </g:if>
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th><g:message code="player.assignment.label" default="Work"/></th>
            <th><g:message code="assignment.lastupdated.label" default="Last update"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${learnerAssignmentList}" status="i" var="assignmentInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td><g:link action="show" controller="player"
                            id="${assignmentInstance.id}">${fieldValue(bean: assignmentInstance, field: "title")}</g:link></td>

                <td><g:formatDate date="${assignmentInstance.lastUpdated}"/></td>

            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="note-list-pagination">
        <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${learnerAssignmentListCount ?: 0}"/>
    </div>
</div>
</div>
</body>
</html>
