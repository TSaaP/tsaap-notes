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

<%@ page import="org.tsaap.assignments.Assignment" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui"/>
  <g:set var="entityName" value="${message(code: message(code: "player.assignment.label"), default: 'Work')}"/>
  <title><g:message code="player.my.assignment.list.label" /></title>
</head>

<body>

<h2 class="ui header">
  <i class="travel icon"></i>

  <div class="content">
    <g:message code="player.my.assignment.list.label"/>
    <div class="sub header">
      <g:message code="player.my.assignment.list.description" />
    </div>
  </div>
</h2>
<div class="ui hidden divider"></div>

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


<table class="ui single selectable line unstackable table">
  <thead>
  <tr>
    <th><g:message code="player.assignment.label" default="Work"/></th>
    <th><g:message code="assignment.lastupdated.label" default="Last update"/></th>
    <th></th>
  </tr>
  </thead>
  <tbody>
  <g:each in="${learnerAssignmentList}" status="i" var="assignmentInstance">
    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

      <td>
        <g:link action="playFirstSequence" controller="player" id="${assignmentInstance.id}">
          ${fieldValue(bean: assignmentInstance, field: "title")}
        </g:link>
      </td>

      <td><g:formatDate date="${assignmentInstance.lastUpdated}"/></td>

      <td>
        <g:link class="ui compact icon primary button"
                controller="player"
                action="playFirstSequence"
                id="${assignmentInstance.id}"
                data-tooltip="${g.message(code: 'player.assignment.play')}"
                data-inverted=""><i class="play icon"></i></g:link>
      </td>

    </tr>
  </g:each>
  </tbody>
</table>

<div class="ui hidden divider"></div>

<div class="ui right aligned basic segment">
  <elaastic:paginate class="ui tiny pagination menu" prev="&laquo;" next="&raquo;"
                     total="${learnerAssignmentListCount ?: 0}"/>
</div>


</body>
</html>
