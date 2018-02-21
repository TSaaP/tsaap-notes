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

<%@ page import="org.tsaap.assignments.StateType" %>
<h3 class="ui top attached block header" style="position: relative;">
  <i class="book icon"></i>

  <div class="content">
    ${assignmentInstance?.title}

    <div class="ui label"
         style="margin-left: 2em;"
         data-tooltip="${message(code: 'player.assignment.registeredUserCount2')}"
         data-position="bottom center">
      <i class="users icon"></i> ${assignmentInstance.registeredUserCount()}
    </div>
  </div>

</h3>

<g:if test="${!assignmentInstance.sequences}">
  <div class="ui segment bottom attached">
    <g:message code="assignment.empty"/>
  </div>
</g:if>

<g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
  <div class="ui clearing attached segment ${sequenceInstance.id == selectedSequence?.id ? 'selected' : ''}"
       id="${context}-${sequenceInstance.id}"
       style="padding-bottom: 1em; cursor: pointer; "
       onclick="window.location = '${g.createLink(controller: 'player', action: 'playSequence', id: sequenceInstance.id)}';">
    <div style="overflow: hidden; height: 100%; padding-top: 2px;">
      <h4 class="ui header">
        <div class="ui tiny circular label" style="margin-right: 1em;">${i+1}.</div> ${fieldValue(bean: sequenceInstance, field: "title")}
      </h4>

      <g:if test="${userRole == 'teacher' || sequenceInstance.state != StateType.beforeStart.name()}">
        <div style="height: 4em; overflow: hidden;">
          ${raw(sequenceInstance.content)}
        </div>
      </g:if>
    </div>
  </div>
</g:each>
<g:if test="${context == 'aside' && selectedSequence?.id}">
  <r:script>
    (function() {
      var container = $('#layout-aside');
      var target = $('#${context}-${selectedSequence.id}');
      
      if(!elaastic.util.isVisible(target, container, 50)) {
        elaastic.util.ensureIsVisible(target, container);
      }
    })();

  </r:script>
</g:if>


<r:style>
  .ui.selected.segment {
     background-color: #fffaf3;
     color: #573a08;
  }
</r:style>