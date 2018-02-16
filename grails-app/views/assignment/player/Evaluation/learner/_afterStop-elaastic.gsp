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

<g:set var="sequence" value="${interactionInstance.sequence}"/>

<g:if test="${!sequence.userHasCompletedPhase2(user)}">
  <div class="ui segment">
    <div class="ui dividing header">
      <g:message code="sequence.phase.Evaluation.description"/>
    </div>

    <div class="ui blue message">
      <g:message code="player.sequence.phase.completedByUser"
                 args="[interactionInstance.sequence.activeInteraction.rank]"/>
    </div>
  </div>
</g:if>