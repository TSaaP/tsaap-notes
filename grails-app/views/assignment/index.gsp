<%@ page import="org.tsaap.assignments.Assignment" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: message(code: "assignment.label"), default: 'Assignment')}"/>
    <title><g:message code="assignment.list.label" args="[entityName]"/></title>
</head>

<body>

<div id="list-assignment" class="container">

    <ol class="breadcrumb">
        <li class="active"><g:message code="assignment.list.label" args="[entityName]"/></li>
    </ol>

    <div class="btn-toolbar" role="toolbar">
        <div class="btn-group btn-group-sm" role="group">
            <g:link class="btn btn-default" action="create"><span
                    class="glyphicon glyphicon-plus"></span>${message(code: 'assignment.index.addAssignment.button')}</g:link>
        </div>
    </div>

    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">${raw(flash.message)}</div>
    </g:if>
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th><g:message code="assignment.label"/></th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${assignmentInstanceList}" status="i" var="assignmentInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <g:link action="show"
                            id="${assignmentInstance.id}">${fieldValue(bean: assignmentInstance, field: "title")}</g:link> <small>(<g:message code="assignment.lastupdated.label"/> ${assignmentInstance.lastUpdated})</small>
                    <br/>
                    <small><span title="${g.message(code: 'player.assignment.registration.link.tooltip')}"><g:createLink controller="player" action="register" absolute="true" params="[globalId:assignmentInstance.globalId]"/></span></small>
                </td>


                <td><g:link controller="player" action="show" id="${assignmentInstance.id}" title="${g.message(code: 'player.assignment.play')}"><span class="glyphicon glyphicon-play" aria-hidden="true"></span></g:link></td>

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
