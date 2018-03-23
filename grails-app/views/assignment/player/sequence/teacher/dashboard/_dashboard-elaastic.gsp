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

<div class="ui accordion segment" id="result-accordion">

  <div class="ui dividing active title header">
    <i class="dropdown icon"></i><g:message code="questions.results"/>

    <g:if test="${!sequence.isStopped()}">
      <g:remoteLink style="float: right;"
                    controller="player"
                    action="updateResultsAndSequenceDisplay"
                    id="${sequence.id}"
                    update="sequence_${interactionInstance.sequenceId}"
                    data-tooltip="${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}"
                    data-inverted=""
                    data-position="top right">
        <i class="small refresh icon"></i></g:remoteLink>
    </g:if>

  </div>

  <div class="ui basic padded segment active content" style="font-size: 1rem;"
       id="interaction_${interactionInstance.id}_result">

    <g:set var="responseSubmissionInteraction" value="${sequence.responseSubmissionInteraction}"/>
    <g:if test="${!interactionInstance.sequence.statement.hasChoices() || responseSubmissionInteraction.hasAnyResult()}">
      <g:render template="/assignment/player/sequence/teacher/dashboard/responseDistributionCharts"
                model="[interactionInstance: responseSubmissionInteraction]"/>

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
    </g:if>
    <g:else>
      <div class="ui warning message">
        ${message(code: "player.sequence.noContribution")}
      </div>
    </g:else>
  </div>
</div>

<r:script>
  $(document).ready(function () {
    $('#result-accordion').accordion();
  });
</r:script>




