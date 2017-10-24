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

<div class="menu">
  <div class="item"
       resource="${assignmentInstance}"
       onclick="$('#assignment-editProperties-modal').modal('show');">
    <i class="setting icon"></i> <g:message code="assignment.action.propertiesEdition.label"/>
  </div>
  <a href="#"
     class="item"
     onclick="$('#confirm-duplicate-assignment-modal').modal('show');">
    <i class="copy icon"></i> <g:message code="assignment.action.duplicate.label"/>
  </a>

  <div class="divider"></div>
  <a href="#"
     class="item"
     onclick="$('#confirm-delete-assignment-modal').modal('show');">
    <i class="outline trash icon"></i> <g:message code="assignment.action.delete.label"/>
  </a>
</div>


%{-- Delete confirm modal --}%
<div>
  <div id="confirm-delete-assignment-modal" class="ui tiny modal">
    <div class="header">
      <g:message code="common.delete"/>
    </div>

    <div class="content">
      <p>
        <g:message code="assignment.delete.confirm" args="[assignmentInstance.title]"/>
      </p>
    </div>

    <div class="actions">
      <div class="ui red approve button">
        <g:message code="common.yes"/>
      </div>

      <div class="ui cancel button">
        <g:message code="common.no"/>
      </div>
    </div>
  </div>
  <r:script>
  $('#confirm-delete-assignment-modal').modal({
    onApprove: function() {
      window.location = '${createLink(controller: "assignment", action: "delete", id: assignmentInstance.id)}';
    }
  });
  </r:script>
</div>

%{-- Duplicate confirm modal --}%
<div>
  <div id="confirm-duplicate-assignment-modal" class="ui tiny modal">
    <div class="header">
      <g:message code="common.duplicate"/>
    </div>

    <div class="content">
      <p>
        <g:message code="assignment.duplicate.confirm" args="[assignmentInstance.title]"/>
      </p>
    </div>

    <div class="actions">
      <div class="ui primary approve button">
        <g:message code="common.yes"/>
      </div>

      <div class="ui cancel button">
        <g:message code="common.no"/>
      </div>
    </div>
  </div>
  <r:script>
  $('#confirm-duplicate-assignment-modal').modal({
    onApprove: function() {
      window.location = '${createLink(controller: "assignment", action: "duplicate", id: assignmentInstance.id)}';
    }
  });
  </r:script>
</div>