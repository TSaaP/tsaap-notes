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

    <h4>${assignmentInstance.title}</h4>

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
<r:script>

    function manageConfigurationChange(sequenceId, questionType, sourceEvent) {
        var phaseConfrontationPanel =  $('#phaseConfrontation_' + sequenceId);
        var phaseFirstSubmission =  $('#phaseFirstSubmission_' + sequenceId);
        if (questionType == "OpenEnded") {
            sourceEvent.prop("checked", true);
            phaseFirstSubmission.addClass("hidden");
            sourceEvent.val(true);
            phaseConfrontationPanel.removeClass('hidden');
        } else if (sourceEvent.is(':checked')) {
            sourceEvent.val(true);
            phaseConfrontationPanel.removeClass('hidden');
        } else {
            sourceEvent.val(false);
            phaseConfrontationPanel.addClass('hidden');
        }
    }

    function manageExecutionContext(sequenceId, questionType, sourceEvent) {
        var studentsProvideExplanation =  $('#studentsProvideExplanation_' + sequenceId + '_' + questionType);
        var configurationPanel = $('#configuration_' + sequenceId);
        var startMessageHolderSync = $('#synchronous_'+sequenceId);
        var startMessageHolderASync = $('#asynchronous_'+sequenceId);
        switch(sourceEvent.val()) {
            case 'FaceToFace':
                configurationPanel.removeClass('hidden')
                studentsProvideExplanation.prop("checked", false);
                manageConfigurationChange(sequenceId,questionType, studentsProvideExplanation);
                startMessageHolderASync.addClass("hidden");
                startMessageHolderSync.removeClass("hidden");
                break;
            default:
                studentsProvideExplanation.prop("checked", true);
                startMessageHolderSync.addClass("hidden");
                startMessageHolderASync.removeClass("hidden");
                if (questionType != "OpenEnded") {
                    configurationPanel.addClass('hidden');
                }
                break;
        }

    }

    $('input[name=studentsProvideExplanation]').on('change', function () {
        var infos =  this.id.split("_")
        var sequenceId = infos[1];
        var questionType = infos[2];
        manageConfigurationChange(sequenceId, questionType, $(this))
    });

    $("input[type=radio][name='executionContext']").on('change', function() {
        var infos = this.id.split("_");
        var sequenceId = infos[1];
        var questionType = infos[2];
        manageExecutionContext(sequenceId, questionType, $(this))
    });


    function remoteLinkSuccess (id) {
        $('#' + 'interactionSpec_' + id).css('display', 'none')
    }

</r:script>
</body>
</html>
