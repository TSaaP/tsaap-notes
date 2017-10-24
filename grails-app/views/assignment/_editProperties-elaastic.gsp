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

<div id="assignment-editProperties-modal" class="ui modal">
    <div class="header">
      <g:message code="assignment.action.propertiesEdition.label"/>
    </div>

    <div class="content">
      <g:form name="editProperties-form"
              class="ui form"
              controller="assignment"
              action="update"
              method="PUT"
              id="${assignmentInstance.id}">
        <g:hiddenField name="version" value="${assignmentInstance?.version}"/>
        <g:render template="assignment_form-elaastic" bean="${assignmentInstance}"/>
      </g:form>
    </div>

    <div class="actions">
      <div class="ui primary button" onclick="$('#editProperties-form').submit();">
        <g:message code="default.button.update.label" />
      </div>
      
      <div class="ui cancel button"><g:message code="common.cancel"/></div>
    </div>
</div>