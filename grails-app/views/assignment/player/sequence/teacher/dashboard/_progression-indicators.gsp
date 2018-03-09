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

<g:set var="responseSubmissionInteraction" value="${sequence.responseSubmissionInteraction}"/>
<div class="ui message" style="font-size: 1rem;">
  <p>
    <g:message code="player.sequence.interaction.responseCount" args="[1]"/> :
    <span id="response_count_${responseSubmissionInteraction.id}">
      ${responseSubmissionInteraction.interactionResponseCount(1)}
    </span>
  </p>

  <p>
    <g:message code="player.sequence.interaction.responseCount" args="[2]"/> :
    <span id="response_count_${responseSubmissionInteraction.id}">
      ${responseSubmissionInteraction.interactionResponseCount(2)}
    </span>
  </p>

  <p>
    ${message(code: 'player.sequence.interaction.evaluationCount', args: [])} : <span
      id="evaluation_count_${responseSubmissionInteraction.id}">${responseSubmissionInteraction.evaluationCount()}</span>
  </p>

</div>




