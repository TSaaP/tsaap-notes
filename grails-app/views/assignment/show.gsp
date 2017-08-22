<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}"/>
    <title><g:message code="assignment.label" args="[entityName]"/></title>
</head>

<body>

<div id="show-assignment" class="container" role="main">

    <ol class="breadcrumb">
        <li><g:link class="list" action="index"><g:message code="assignment.list.label"
                                                           args="[entityName]"/></g:link></li>
        <li class="active">${message(code: 'assignment.label')} "${assignmentInstance?.title}"</li>
    </ol>

    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            ${raw(flash.message)}
        </div>
    </g:if>

    <div class="btn-toolbar" role="toolbar">
        <div class="btn-group btn-group-sm" role="group">
            <g:link role="button" class="btn btn-default" action="addSequence" controller="assignment"
                    resource="${assignmentInstance}"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> <g:message
                    code="assignment.action.addSequence.label" default="Edit"/></g:link>
            <g:link role="button" class="btn btn-default" controller="player" action="show" id="${assignmentInstance.id}" title="${g.message(code: 'player.assignment.play')}"><span class="glyphicon glyphicon-play" aria-hidden="true"></span> ${g.message(code: 'player.assignment.play')}</g:link>
            <g:render template="assignment_actions" model="[assignmentInstance: assignmentInstance]"/>
        </div>
    </div>

    <h4>${assignmentInstance.title}</h4>
    <p><span title="${g.message(code: 'player.assignment.registration.link.tooltip')}"><g:createLink controller="player" action="register" absolute="true" params="[globalId:assignmentInstance.globalId]"/></span></p>
    <table class="table table-striped table-hover">
        <tbody>
        <g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <td><g:link action="editSequence" controller="sequence"
                            id="${sequenceInstance.id}">${fieldValue(bean: sequenceInstance, field: "title")}</g:link></td>

                <td><g:render template="sequence/sequence_actions" model="[sequenceInstance:sequenceInstance]"/></td>
            </tr>
        </g:each>
        </tbody>
    </table>

</div>
</body>
</html>
