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
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">

  <r:require modules="semantic_ui,jquery,elaastic_ui,vue_js"/>
  <g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}"/>
  <title><g:message code="assignment.label" args="[entityName]"/></title>
</head>

<body>

<content tag="specificMenu">
  <g:link class="item"
          action="addSequence"
          controller="assignment"
          data-tooltip="${message(code: 'assignment.action.addSequence.label')}"
          data-position="right center"
          data-inverted=""
          resource="${assignmentInstance}">
    <i class="yellow plus square outline icon"></i>
  </g:link>

  <g:link class="item"
          controller="player"
          action="playFirstSequence"
          id="${assignmentInstance.id}"
          data-tooltip="${message(code: 'player.assignment.play')}"
          data-position="right center"
          data-inverted="">
    <i class="yellow play icon"></i>
  </g:link>

  <div class="item"
       onclick="window.learnerAccessModalApp.showLearnerAccessModal('${g.createLink(controller: 'player', action: 'register', absolute: true, params: [globalId: assignmentInstance.globalId])}', '${assignmentInstance.title.replaceAll("'", "\\\\u0027")}')"
       data-tooltip="${message(code: 'assignment.learnerAccess.oneLine')}"
       data-position="right center"
       data-inverted="">
    <i class="yellow feed icon"></i>
  </div>

  <div id="show-assignment-more-actions-dropdown"
       class="ui dropdown item"
       data-tooltip="${message(code: "common.more.actions")}"
       data-position="right center"
       data-inverted="">
    <i class="yellow sidebar icon"></i>
    <g:render template="assignment_actions-elaastic" model="[assignmentInstance: assignmentInstance]"/>
  </div>

  <r:script>
    $(document)
        .ready(function () {

      // Initialize dropdown
      $('#show-assignment-more-actions-dropdown').dropdown();
    });
  </r:script>

</content>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>

<h2 class="ui top attached block header">
  <i class="book icon"></i>

  <div class="content">
    <span onclick="$('#assignment-editProperties-modal').modal('show');"
          style="cursor: pointer">
      ${assignmentInstance?.title}
    </span>

    <g:link action="index"
            controller="assignment"
            id="${assignmentInstance.id}"
            class="ui small compact button"
            style="margin-left: 2em;
        vertical-align: text-bottom">

      <i class="caret left icon"></i>
      <g:message code="common.back"/>
    </g:link>
    <div class="sub header">
      <g:message code="assignment.action.show.label"/>
    </div>
  </div>
</h2>

<g:if test="${!assignmentInstance.sequences}">
  <div class="ui segment bottom attached">
    <g:message code="assignment.empty"/>
  </div>
</g:if>

<g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
  <div class="ui clearing segment attached large text" id="sequence_${sequenceInstance.id}">
    <div class="ui clearing basicsegment">
      <g:render template="sequence/sequence_actions-elaastic" model="[sequenceInstance: sequenceInstance]"/>
      <h3 class="ui header">
        <g:link action="editSequence" controller="sequence"
                id="${sequenceInstance.id}">
          <g:message code="common.question"/> ${i + 1} - ${fieldValue(bean: sequenceInstance, field: "title")}
        </g:link>
      </h3>
    </div>

    <div class="ui hidden divider"></div>

    <g:set var="attachment" value="${sequenceInstance?.statement?.attachment}"/>
    <g:if test="${attachment != null}">
      <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
      <div class="ui hidden divider"></div>
    </g:if>

    ${raw(sequenceInstance.content)}
  </div>
</g:each>

<g:render template="learner_access_modal-elaastic"/>

<r:script>
  $(document)
      .ready(function () {

    if (window.location.hash) {
      $(window.location.hash).scrollTop(400);
    }


    $('.message .close')
        .on('click', function () {
      $(this)
          .closest('.message')
          .transition('fade')
      ;
    });
  });

</r:script>

<g:render template="editProperties-elaastic"
          model="[assignmentInstance: assignmentInstance]"/>

</body>
</html>
