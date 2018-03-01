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

<g:render template="/assignment/player/statement/teacher/show-elaastic"
          model="[statementInstance: sequenceInstance.statement]"/>

<g:set var="currentInteraction" value="${sequenceInstance.activeInteraction}"/>
<g:render
        template="/assignment/player/${currentInteraction.interactionType}/teacher/${currentInteraction.stateForTeacher(user)}-elaastic"
        model="[interactionInstance: currentInteraction, user: user, attempt: 1]"/>

<hr/>

<p>
    <g:if test="${sequenceInstance.isStopped()}">
        <g:remoteLink class="ui button"
                      controller="player"
                      action="reopenSequence"
                      id="${sequenceInstance.id}"
                      update="sequence_${sequenceInstance.id}">
            <i class="play icon"></i>
            ${message(code: "player.sequence.reopenSequence")}
        </g:remoteLink>
    </g:if>
    <g:if test="${!sequenceInstance.isStopped()}">
        <g:remoteLink class="ui primary button"
                      controller="player"
                      action="stopSequence"
                      id="${sequenceInstance.id}"
                      update="sequence_${sequenceInstance.id}">
            <i class="stop icon"></i>
            ${message(code: "player.sequence.readinteraction.stopSequence")}
        </g:remoteLink>
    </g:if>
    <g:if test="${sequenceInstance.resultsCanBePublished()}">
        <g:remoteLink class="ui primary button"
                      controller="player"
                      action="publishResultsForSequence"
                      id="${sequenceInstance.id}"
                      update="sequence_${sequenceInstance.id}">
            <i class="stop icon"></i>
            ${message(code: "player.sequence.publishResults")}
        </g:remoteLink>
    </g:if>
    <g:if test="${sequenceInstance.resultsArePublished}">
        <g:remoteLink class="ui primary button"
                      controller="player"
                      action="unpublishResultsForSequence"
                      id="${sequenceInstance.id}"
                      update="sequence_${sequenceInstance.id}">
            <i class="stop icon"></i>
            ${message(code: "player.sequence.unpublishResults")}
        </g:remoteLink>
    </g:if>
</p>

