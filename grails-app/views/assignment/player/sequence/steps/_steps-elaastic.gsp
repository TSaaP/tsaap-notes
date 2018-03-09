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

<%@ page import="org.tsaap.assignments.InteractionType" %>
<%@ page import="org.tsaap.assignments.StateType" %>

<g:if test="${stateByInteractionType[InteractionType.ResponseSubmission] == StateType.beforeStart.name()}">
  <g:set var="responseSubmissionCss" value="disabled"/>
</g:if>
<g:elseif test="${stateByInteractionType[InteractionType.ResponseSubmission] == StateType.show.name()}">
  <g:set var="responseSubmissionCss" value="active-phase"/>
</g:elseif>
<g:elseif test="${stateByInteractionType[InteractionType.ResponseSubmission] == StateType.afterStop.name()}">
  <g:set var="responseSubmissionCss" value="completed-phase"/>
</g:elseif>

<g:if test="${stateByInteractionType[InteractionType.Evaluation] == StateType.beforeStart.name()}">
  <g:set var="evaluationCss" value="disabled"/>
</g:if>
<g:elseif test="${stateByInteractionType[InteractionType.Evaluation] == StateType.show.name()}">
  <g:set var="evaluationCss" value="active-phase"/>
</g:elseif>
<g:elseif test="${stateByInteractionType[InteractionType.Evaluation] == StateType.afterStop.name()}">
  <g:set var="evaluationCss" value="completed-phase"/>
</g:elseif>

<g:if test="${stateByInteractionType[InteractionType.Read] == StateType.beforeStart.name()}">
  <g:set var="readCss" value="disabled"/>
</g:if>
<g:elseif test="${stateByInteractionType[InteractionType.Read] == StateType.show.name()}">
  <g:set var="readCss" value="active-phase"/>
</g:elseif>
<g:elseif test="${stateByInteractionType[InteractionType.Read] == StateType.afterStop.name()}">
  <g:set var="readCss" value="completed-phase"/>
</g:elseif>

<r:style>
  .ui.steps .step.completed-phase, .ui.steps .step.completed-phase:after {
    background-color: #f3f4f5;
  }

  .ui.steps .step.active-phase, .ui.steps .step.active-phase:after {
    background-color: #dff0ff;
  }

  .ui.steps .step.active-phase .title, .ui.steps .step.active-phase i:before {
    color: #4183c4;
}
</r:style>

<g:set var="responseSubmissionInteraction" value="${sequence.responseSubmissionInteraction}"/>
<div class="ui three top attached steps">
  <div class="${responseSubmissionCss} step">
    <g:if test="${showStatistics}">
      <div class="floating ui grey label" style="left: 3em;" data-inverted="" data-tooltip="${g.message(code: 'player.sequence.interaction.responseCount', args: [1])}" data-position="bottom left"><i
          class="comment outline icon"></i>${responseSubmissionInteraction.interactionResponseCount(1)}</div>
    </g:if>

    <i class="comment outline icon"></i>

    <div class="content">
      <div class="title"><g:message code="sequence.phase.1"/></div>

      <div class="description" style="min-height: 2.5rem;"><g:message
          code="sequence.phase.ResponseSubmission.description"/></div>
    </div>
  </div>

  <div class="${evaluationCss} step">

    <g:if test="${showStatistics}">
      <div class="floating ui grey label" style="left: 3em;"data-inverted="" data-tooltip="${g.message(code: 'player.sequence.interaction.peerEvaluationCount')}" data-position="bottom left"><i class="comments outline icon"></i>${responseSubmissionInteraction.evaluationCount()}</div>

      <div class="floating ui grey label" style="left: 9em;" data-inverted="" data-tooltip="${g.message(code: 'player.sequence.interaction.responseCount', args: [2])}" data-position="bottom left"><i class="comment outline icon"></i>${responseSubmissionInteraction.interactionResponseCount(2)}</div>
    </g:if>

    <i class="comments outline icon"></i>

    <div class="content">
      <div class="title"><g:message code="sequence.phase.2"/></div>

      <div class="description" style="min-height: 2.5rem;"><g:message
          code="sequence.phase.Evaluation.description"/></div>
    </div>
  </div>

  <div class="${readCss} step">
    <i class="bar chart icon"></i>

    <div class="content">
      <div class="title"><g:message code="sequence.phase.3"/></div>

      <div class="description" style="min-height: 2.5rem;"><g:message code="sequence.phase.Read.description"/></div>
    </div>
  </div>
</div>