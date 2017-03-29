<%@ page import="org.tsaap.assignments.Schedule" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'player.assignment.label', default: 'Play Assignment')}"/>
    <title><g:message code="assignment.label" args="[entityName]"/></title>
</head>

<body>

<div id="show-assignment" class="container" role="main">

    <ol class="breadcrumb">
        <sec:ifAnyGranted roles="${org.tsaap.directory.RoleEnum.STUDENT_ROLE.label}">
            <li><g:link class="list" action="index"><g:message code="player.assignment.list.label"
                                                               args="[entityName]"/></g:link></li>
            <li class="active">${message(code: 'player.assignment.label')} "${assignmentInstance?.title}"</li>
        </sec:ifAnyGranted>
        <sec:ifAnyGranted roles="${org.tsaap.directory.RoleEnum.TEACHER_ROLE.label}">
            <li><g:link class="list" action="index" controller="assignment"><g:message code="assignment.list.label"
                                                                                       args="[entityName]"/></g:link></li>
            <li class="active">${message(code: 'player.assignment.label')} "${assignmentInstance?.title}"</li>
        </sec:ifAnyGranted>

    </ol>

    <g:if test="${flash.message}">
        <div class="alert alert-info" role="status">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            ${flash.message}
        </div>
    </g:if>
    <g:set var="scheduleInstance" value="${assignmentInstance.schedule}"/>

    <sec:ifAnyGranted roles="${org.tsaap.directory.RoleEnum.TEACHER_ROLE.label}">
        <g:if test="${assignmentInstance.owner.username == sec.username().toString()}">
        <div class="btn-toolbar" role="toolbar">
            <div class="btn-group btn-group-sm" role="group">
                <g:link role="button" class="btn btn-default" action="show" controller="assignment"
                        resource="${assignmentInstance}"><span class="glyphicon glyphicon-pencil"
                                                               aria-hidden="true"></span> <g:message
                        code="assignment.action.show.label" default="Edit"/></g:link>
            </div>
        </div>
        </g:if>
    </sec:ifAnyGranted>

    <h4>${assignmentInstance.title}
        <g:if test="${scheduleInstance?.startDate}">
            <small>
                <span id="startDate-label" class="property-label"><g:message code="schedule.startdate.label"
                                                                             default="Start Date"/></span>
                <span class="property-value" aria-labelledby="startDate-label"><g:formatDate
                        date="${scheduleInstance?.startDate}"/>.</span>
                <g:if test="${scheduleInstance?.endDate}">
                    <span id="endDate-label" class="property-label"><g:message code="schedule.enddate.label"
                                                                               default="End Date"/></span>
                    <span class="property-value" aria-labelledby="endDate-label"><g:formatDate
                            date="${scheduleInstance?.endDate}"/>.</span>

                </g:if>
            </small>
        </g:if>
    </h4>

    <div class="well well-sm"><g:message code="player.assignment.registeredUserCount"/> <span
            id="registered_user_count">${assignmentInstance.registeredUserCount()}</span> <g:remoteLink
            controller="player" action="updateRegisteredUserCount" id="${assignmentInstance.id}" title="Refresh"
            update="registered_user_count"><span class="glyphicon glyphicon-refresh"
                                                 aria-hidden="true"></span></g:remoteLink>
        <br/>
        <small><span title="${g.message(code: 'player.assignment.registration.link.tooltip')}"><g:createLink
                controller="player" action="register" absolute="true"
                params="[globalId: assignmentInstance.globalId]"/></span></small>
    </div>
    <ul class="list-group">
        <g:set var="userRole" value="${user == assignmentInstance.owner ? 'teacher' : 'learner'}"/>
        <g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
            <li class="list-group-item" id="sequence_${sequenceInstance.id}">
                <g:render template="/assignment/player/sequence/show"
                          model="[userRole: userRole, sequenceInstance: sequenceInstance, user: user]"/>
            </li>
        </g:each>
    </ul>
</div>
<script lang="text/javascript">
    $('input[name=studentsProvideExplanation]').on('change', function () {
        var currentInput = $('#' + this.id);
        var sequenceId = this.id.split("_")[1];
        var studentsProvideConfidenceDegree =  $('#' + 'studentsProvideConfidenceDegree_' + sequenceId);
        var phaseConfrontationPanel =  $('#' + 'phaseConfrontation_' + sequenceId);
        if (currentInput.is(':checked')) {
            this.value = true;
            studentsProvideConfidenceDegree.prop('value', true);
            studentsProvideConfidenceDegree.prop( 'checked', true );
            studentsProvideConfidenceDegree.prop( 'disabled', true );
            phaseConfrontationPanel.css( 'display', 'block' );
        } else {
            this.value = false;
            studentsProvideConfidenceDegree.prop('value', false);
            studentsProvideConfidenceDegree.prop( 'checked', false );
            studentsProvideConfidenceDegree.prop( 'disabled', false );
            phaseConfrontationPanel.css( 'display', 'none' );
        }
    });

    $('input[name=studentsProvideConfidenceDegree]').on('change', function () {
        this.value = this.checked;
    });

    $('input[name=asynchronousProcess]').on('change', function () {
        this.value = this.checked;
    });

    function remoteLinkSuccess (id) {
        $('#' + 'interactionSpec_' + id).css('display', 'none')
    }

    function switchBtnText(checkbox, sequenceId) {
       if (checkbox.checked)  {
           $('#synchronous_' + sequenceId).css('display', 'none');
           $('#asynchronous_' + sequenceId).css('display', 'inline-block');
       }  else {
           $('#asynchronous_' + sequenceId).css('display', 'none');
           $('#synchronous_' + sequenceId).css('display', 'inline-block');
       }
    }
</script>
</body>
</html>
