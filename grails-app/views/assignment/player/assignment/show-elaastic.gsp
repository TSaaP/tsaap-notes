<%@ page import="org.tsaap.skin.SkinUtil" %>
%{--
  -
  -  Copyright (C) 2017 Ticetime
  -
  -      This program is free software: you can redistribute it and/or modify
  -      it under the terms of the GNU Affero General Public License as published by
  -      the Free Software Foundation, either version 3 of the License, or
  -      (at your option) any later version.
  -
  -      This program is distributed in the hope that it will be useful,
  -      but WITHOUT ANY WARRANTY; without even the implied warranty of
  -      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -      GNU Affero General Public License for more details.
  -
  -      You should have received a copy of the GNU Affero General Public License
  -      along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -
  --}%

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui,vue_js"/>
  <g:set var="entityName" value="${message(code: 'player.assignment.label', default: 'Play Assignment')}"/>
  <title><g:message code="assignment.label" args="[entityName]"/></title>
</head>

<body>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>
<r:script>

  $(document)
      .ready(function () {
    $('.message .close')
        .on('click', function () {
      $(this)
          .closest('.message')
          .transition('fade')
      ;
    });

  });
</r:script>

<sec:ifAnyGranted roles="${org.tsaap.directory.RoleEnum.TEACHER_ROLE.label}">
  <g:if test="${assignmentInstance.owner.username == sec.username().toString()}">

    <content tag="specificMenu">
      <g:link class="item"
              action="show"
              controller="assignment"
              resource="${assignmentInstance}"
              data-tooltip="${message(code: 'assignment.action.show.label')}"
              data-position="right center"
              data-inverted="">
        <i class="yellow edit icon"></i>
      </g:link>

      <div class="item"
           onclick="window.learnerAccessModalApp.showLearnerAccessModal('${g.createLink(controller: 'player', action: 'register', absolute: true, params: [globalId: assignmentInstance.globalId])}', '${assignmentInstance.title.replaceAll("'", "\\\\u0027")}')"
           data-tooltip="${message(code: 'assignment.learnerAccess.oneLine')}"
           data-position="right center"
           data-inverted="">
        <i class="yellow feed icon"></i>
      </div>
    </content>

  </g:if>
</sec:ifAnyGranted>


<h2 class="ui top attached block header">
  <i class="book icon"></i>

  <div class="content">
    ${assignmentInstance?.title}
  </div>
</h2>

<div class="ui attached segment">
  <g:message code="player.assignment.registeredUserCount"/> <span
    id="registered_user_count">${assignmentInstance.registeredUserCount()}</span>

  <g:remoteLink
      controller="player" action="updateRegisteredUserCount" id="${assignmentInstance.id}" title="Refresh"
      update="registered_user_count">
    <i class="refresh icon"></i></g:remoteLink>
  <br/>

</div>

<g:set var="userRole" value="${user == assignmentInstance.owner ? 'teacher' : 'learner'}"/>
<g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
  <div class="ui attached large text segment" id="sequence_${sequenceInstance.id}">
    <g:render
        template="/assignment/player/sequence/${userRole}/${org.tsaap.skin.SkinUtil.getView(params, session, sequenceInstance.state)}"
        model="[userRole: userRole, sequenceInstance: sequenceInstance, user: user]"/>

  </div>
</g:each>

<div class="ui hidden divider"></div>

<r:script>

  function manageConfigurationChange(sequenceId, questionType, sourceEvent) {
    var phaseConfrontationPanel = $('#phaseConfrontation_' + sequenceId);
    var phaseFirstSubmission = $('#phaseFirstSubmission_' + sequenceId);
    if (questionType == "OpenEnded") {
      sourceEvent.prop("checked", true);
      phaseFirstSubmission.hide();
      sourceEvent.val(true);
      phaseConfrontationPanel.show();
    } else if (sourceEvent.is(':checked')) {
      sourceEvent.val(true);
      phaseConfrontationPanel.show();
    } else {
      sourceEvent.val(false);
      phaseConfrontationPanel.hide();
    }
  }

  function manageExecutionContext(sequenceId, questionType, sourceEvent) {
    var studentsProvideExplanation = $('#studentsProvideExplanation_' + sequenceId + '_' + questionType);
    var configurationPanel = $('#configuration_' + sequenceId);
    switch (sourceEvent.val()) {
    case 'FaceToFace':
      configurationPanel.show();
      studentsProvideExplanation.prop("checked", false);
      studentsProvideExplanation.prop("disabled", false);
      manageConfigurationChange(sequenceId, questionType, studentsProvideExplanation);
      break;
    default:
      studentsProvideExplanation.prop("checked", true);
      studentsProvideExplanation.prop("disabled", true);
      manageConfigurationChange(sequenceId, questionType, studentsProvideExplanation);
      break;
    }

  }

  $('input[name=studentsProvideExplanation]').on('change', function () {
    var infos = this.id.split("_")
    var sequenceId = infos[1];
    var questionType = infos[2];
    manageConfigurationChange(sequenceId, questionType, $(this))
  });

  $("input[type=radio][name='executionContext']").on('change', function () {
    var infos = this.id.split("_");
    var sequenceId = infos[1];
    var questionType = infos[2];
    manageExecutionContext(sequenceId, questionType, $(this))
  });

  $(document).ready(function () {
    $('.ui.checkbox').checkbox();
  });


</r:script>

<g:render template="/assignment/learner_access_modal-elaastic"/>

</body>
</html>
