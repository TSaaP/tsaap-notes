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
<g:set var="responseInteraction" value="${interactionInstance.sequence.responseSubmissionInteraction}"/>
<div class="ui message" style="font-size: 1rem;">
  <g:if test="${interactionInstance.sequence.statement.hasChoices()}">
    <p>
      ${message(code: 'player.sequence.interaction.firstAttemptSubmissionCount', args: [])}
      <span id="first_attempt_count_${responseInteraction.id}">${responseInteraction.interactionResponseCount(1)}</span>
    </p>

    <p>
      ${message(code: 'player.sequence.interaction.secondAttemptSubmissionCount', args: [])}
      <span id="second_attempt_count_${responseInteraction.id}">
        ${responseInteraction.interactionResponseCount(2)}
      </span>
      <g:remoteLink
          controller="player"
          action="updateSecondAttemptCount"
          id="${responseInteraction.id}"
          title="Refresh"
          update="second_attempt_count_${responseInteraction.id}">
        <i class="refresh icon"></i>
      </g:remoteLink>
    </p>
  </g:if>
  <p>${message(code: 'player.sequence.interaction.evaluationCount', args: [])}
    <span id="evaluation_count_${responseInteraction.id}">${responseInteraction.evaluationCount()}</span>
    <g:remoteLink controller="player"
                  action="updateEvaluationCount"
                  id="${responseInteraction.id}"
                  title="Refresh"
                  update="evaluation_count_${responseInteraction.id}">
      <i class="refresh icon"></i>
    </g:remoteLink>
  </p>
</div>
<g:remoteLink class="ui primary button"
              controller="player"
              action="stopInteraction"
              id="${interactionInstance.id}"
              update="sequence_${interactionInstance.sequenceId}">
  <i class="stop icon"></i>
  ${message(code: "player.sequence.interaction.stop", args: [interactionInstance.rank])}
</g:remoteLink>
