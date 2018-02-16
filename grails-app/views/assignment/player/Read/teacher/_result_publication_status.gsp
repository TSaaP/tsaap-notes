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
<%@ page import="org.tsaap.assignments.StateType;" %>

<g:if test="${sequenceInstance.resultsArePublished}">
  <div class="ui bottom attached blue message">
    <g:message code="player.sequence.interaction.read.teacher.show.message"/>
  </div>
</g:if>
<g:elseif test="${sequenceInstance.state == StateType.show.name()}">
  <div class="ui blue bottom attached message">
    ${message(code: "player.sequence.readinteraction.beforeStart.message", args: [interactionInstance.rank])}
  </div>
</g:elseif>
<g:else>
  <div class="ui bottom attached message">
    ${message(code: "player.sequence.readinteraction.not.published")}
  </div>
</g:else>
