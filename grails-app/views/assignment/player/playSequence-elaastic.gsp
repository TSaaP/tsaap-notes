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

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="3columns-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui,vue_js"/>
  <title>elaastic-questions: ${assignmentInstance?.title}</title>
</head>

<body>

<g:set var="userRole" value="${user == assignmentInstance.owner ? 'teacher' : 'learner'}"/>


<content tag="specificMenu">
  <g:if test="${userRole == 'teacher'}">
    <g:link class="item"
            controller="assignment"
            action="show"
            id="${assignmentInstance.id}"
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
  </g:if>
</content>
<content tag="aside">
  <g:render template="/assignment/player/assignment_overview"
            model="[assignmentInstance: assignmentInstance,
                    selectedSequence  : sequenceInstance,
                    userRole          : userRole]"/>
</content>

<g:if test="${sequenceInstance}">
  <div class="ui attached large text segment" id="sequence_${sequenceInstance.id}">
    <g:render
        template="/assignment/player/sequence/${userRole}/${org.tsaap.skin.SkinUtil.getView(params, session, sequenceInstance.state)}"
        model="[userRole: userRole, sequenceInstance: sequenceInstance, user: user]"/>

  </div>
</g:if>

<g:render template="/assignment/player/assignment/manage_configuration"/>
<g:render template="/assignment/learner_access_modal-elaastic"/>

</body>
</html>