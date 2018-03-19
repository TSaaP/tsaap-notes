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
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui,vue_js"/>
  <g:set var="entityName" value="${message(code: message(code: "assignment.label"), default: 'Assignment')}"/>
  <title><g:message code="assignment.list.label" args="[entityName]"/></title>
</head>

<body>

<content tag="specificMenu">
  <g:link action="create"
          class="item"
          data-tooltip="${message(code: 'assignment.create.label')}"
          data-position="right center"
          data-inverted="">
    <i class="yellow plus square outline icon"></i>
  </g:link>
</content>

<h2 class="ui header">
  <i class="book icon"></i>

  <div class="content">
    <g:message code="assignment.my.list.label"/>
    <div class="sub header">
      <g:message code="assignment.my.list.description"/>
    </div>
  </div>
</h2>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>

<g:if test="${assignmentInstanceList}">
  <table class="ui single selectable line unstackable table">
    <thead>
    <tr>
      <th><g:message code="common.title"/></th>
      <th class="not mobile"><g:message code="common.lastUpdate"/></th>
      <th></th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${assignmentInstanceList}" status="i" var="assignmentInstance">
      <tr>
        <td>

          <g:link action="show" id="${assignmentInstance.id}"
                  data-tooltip="${g.message(code: 'assignment.action.show.label')}" data-inverted="">
            ${fieldValue(bean: assignmentInstance, field: "title")}
          </g:link>

          &nbsp; [<g:link action="show" id="${assignmentInstance.id}"
                          data-tooltip="${g.message(code: 'assignment.action.show.label')}" data-inverted=""><i
              class="edit outline icon" style="margin: 0;"></i></g:link>]

        </td>

        <td class="not mobile"><g:formatDate date="${assignmentInstance.lastUpdated}"/></td>

        <td>
          <g:link class="ui compact icon primary button"
                  controller="player"
                  action="playFirstSequence"
                  id="${assignmentInstance.id}"
                  data-tooltip="${g.message(code: 'player.assignment.play')}"
                  data-inverted="">
            <i class="play icon"></i></g:link>
        </td>
      </tr>
    </g:each>
    </tbody>
  </table>
</g:if>
<g:else>
  <div class="ui info message">
    <g:message code="assignment.you.have.none" />
  </div>
</g:else>

<div class="ui basic segment">
  <g:link action="create"
          class="ui primary button">
    <i class="add icon"></i> <g:message code="assignment.create.label"/>
  </g:link>
</div>

<div class="ui hidden divider"></div>

<div class="ui right aligned basic segment">
  <elaastic:paginate class="ui tiny pagination menu" prev="&laquo;" next="&raquo;"
                     total="${assignmentInstanceCount ?: 0}"/>
</div>

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

</body>
</html>