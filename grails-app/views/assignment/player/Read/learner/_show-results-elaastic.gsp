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

<div class="ui segment">
  <div class="ui dividing header">
    <g:message code="questions.results"/>
  </div>

  <div class="ui basic padded segment"
       style="font-size: 1rem;"
       id="interaction_${interactionInstance.id}_result">

    <g:set var="sequence" value="${interactionInstance.sequence}"/>
    <g:set var="displayedResultInteraction" value="${sequence.responseSubmissionInteraction}"/>
    <g:set var="choiceSpecification" value="${sequence.statement.getChoiceSpecificationObject()}"/>
    <g:if test="${sequence.statement.hasChoices()}">
      <g:set var="resultList" value="${displayedResultInteraction.resultsOfLastAttempt()}"/>
      <g:set var="userResponse" value="${displayedResultInteraction.lastAttemptResponseForUser(user)}"/>

      <g:each var="i" in="${(1..choiceSpecification.itemCount)}">
        <g:set var="choiceStatus"
               value="${choiceSpecification.expectedChoiceListContainsChoiceWithIndex(i) ? 'green' : 'red'}"/>
        <g:set var="percentResult" value="${resultList[i]}"/>

        <div class="ui ${choiceStatus} progress" data-percent="${percentResult}">
          <div class="bar">
            <div class="progress"></div>
          </div>

          <div class="label">
            <g:if test="${userResponse?.choiceList()?.contains(i)}">
              <g:set var="suffix"
                     value="${choiceSpecification.choiceWithIndexInExpectedChoiceList(i)?.score > 0 ? 'up' : 'down'}"/>
              <i class="thumbs ${suffix} icon"></i>

            </g:if>
            ${message(code: "player.sequence.interaction.choice.label")} ${i}
          </div>
        </div>

      </g:each>
      <g:set var="percentResult" value="${resultList[0]}"/>

      <g:if test="${percentResult}">
        <div class="ui top attached warning small message">
          <div class="header">
            ${message(code: "player.sequence.interaction.NoResponse.label")}
          </div>
        </div>

        <div class="ui bottom attached segment" style="margin-bottom: 1rem;">
          <div class="ui warning compact progress"
               data-percent="${percentResult}">
            <div class="bar">
              <div class="progress"></div>
            </div>
          </div>
        </div>

      </g:if>
      <div class="ui message">
        <g:if test="${userResponse?.score != null}">
          ${message(code: "player.sequence.interaction.read.learner.show.score.message")}
          <g:formatNumber number="${userResponse?.score}" type="number" maxFractionDigits="2"/>
        </g:if>
        <g:else>
          <g:message code="player.sequence.user.noScore" />
        </g:else>
      </div>
    </g:if>

    <g:if test="${sequence.hasExplanations()}">
      <g:set var="attempt" value="${sequence.executionIsBlendedOrDistance() ? 2 : 1}"/>
      <g:if test="${sequence.statement.hasChoices()}">
        <g:set var="responses" value="${sequence.findAllGoodResponses(attempt)}"/>
      </g:if>
      <g:else>
        <g:set var="responses" value="${sequence.findAllOpenResponses(attempt)}"/>
      </g:else>
      <g:render template="/assignment/player/${org.tsaap.skin.SkinUtil.getView(params, session, 'ExplanationList')}"
                model="[responses: responses, sequence: sequence]"/>
    </g:if>

    <g:if test="${sequence.executionIsDistance() && !sequence.isStopped()}">
      <div style="margin-top: 15px">
        <g:remoteLink class="ui button"
                      controller="player"
                      action="updateResultsAndSequenceDisplay"
                      id="${sequence.id}"
                      update="sequence_${interactionInstance.sequenceId}"
                      onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'sequence_${interactionInstance.sequenceId}'])">
          <i class="refresh icon"></i>
          ${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}</g:remoteLink>
      </div>
    </g:if>

  </div>
</div>

<r:script>
$('#interaction_${interactionInstance.id}_result .ui.progress').progress({
  autoSuccess: false,
  showActivity: false
});
</r:script>
