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
    <g:message code="player.sequence.interaction.responseCount" args="[1]"/>
    <span id="response_count_${responseSubmissionInteraction.id}">
      ${responseSubmissionInteraction.interactionResponseCount(1)}
    </span>
  </p>
  <g:if test="${sequence.isDefaultProcess()}">
    <g:if test="${sequence.statement.hasChoices()}">
      <p>
        <g:message code="player.sequence.interaction.responseCount" args="[2]"/>
        <span id="response_count_${responseSubmissionInteraction.id}">
          ${responseSubmissionInteraction.interactionResponseCount(2)}
        </span>
      </p>
    </g:if>
    <p>
      ${message(code: 'player.sequence.interaction.evaluationCount', args: [])} <span
        id="evaluation_count_${responseSubmissionInteraction.id}">${responseSubmissionInteraction.evaluationCount()}</span>
    </p>
  </g:if>
</div>
<g:if test="${resultsArePublished}">
  <div class="ui warning message" style="font-size: 1rem;">
    ${message(code: "player.sequence.interaction.read.teacher.show.message", args: [interactionInstance.rank])}
  </div>
</g:if>

<g:if test="${sequence.hasExplanations()}">
  <g:set var="attempt" value="${sequence.executionIsBlendedOrDistance() ? 2 : 1}"/>
  <g:if test="${sequence.statement.hasChoices()}">
    <g:set var="responses" value="${sequence.findAllGoodResponses(attempt)}"/>
    <g:set var="badResponses" value="${sequence.findAllBadResponses(attempt)}"/>
  </g:if>
  <g:else>
    <g:set var="responses" value="${sequence.findAllOpenResponses(attempt)}"/>
    <g:set var="badResponses" value="${[:]}"/>
  </g:else>
  <g:render template="/assignment/player/${org.tsaap.skin.SkinUtil.getView(params, session, 'ExplanationList')}"
            model="[responses: responses, sequence: sequence, badResponses: badResponses]"/>
</g:if>
<g:if test="${sequence.executionIsBlendedOrDistance() && !sequence.isStopped()}">
  <div style="margin-top: 15px">
    <g:remoteLink class="ui button"
                  controller="player"
                  action="updateResultsAndSequenceDisplay"
                  id="${sequence.id}"
                  update="sequence_${interactionInstance.sequenceId}">
      <i class="refresh icon"></i>
      ${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}</g:remoteLink>
  </div>
</g:if>

