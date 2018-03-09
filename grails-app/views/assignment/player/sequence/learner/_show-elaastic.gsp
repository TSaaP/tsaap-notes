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

<g:set var="activeInteraction" value="${sequenceInstance.activeInteractionForLearner(user)}"/>
<g:set var="activeInteractionState" value="${activeInteraction.stateForLearner(user)}"/>

<g:render template="/assignment/player/sequence/steps/steps-elaastic"
          model="[sequence              : sequenceInstance,
                  stateByInteractionType: [
                      (InteractionType.ResponseSubmission): sequenceInstance.responseSubmissionInteraction?.stateForLearner(user),
                      (InteractionType.Evaluation)        : sequenceInstance.evaluationInteraction?.stateForLearner(user),
                      (InteractionType.Read)              : sequenceInstance.readInteraction?.stateForLearner(user),
                  ]]"/>

<g:render template="/assignment/player/sequence/learner/sequenceInfo/sequenceInfo"
          model="[sequence              : sequenceInstance,
                  activeInteraction     : activeInteraction,
                  activeInteractionState: activeInteractionState]"/>


<g:render template="/assignment/player/statement/show-elaastic"
          model="[statementInstance: sequenceInstance.statement, hideStatement: false]"/>

<g:if test="${activeInteraction.interactionType != InteractionType.Read.name()}">
  <g:render
      template="/assignment/player/${activeInteraction.interactionType}/learner/${sequenceInstance.isStopped() ? StateType.afterStop.name() : activeInteractionState}-elaastic"
      model="[interactionInstance: activeInteraction, user: user, attempt: 1]"/>
</g:if>

%{-- Result display --}%
<g:if test="${sequenceInstance.isStopped() || activeInteraction.isRead()}">
  <g:if test="${sequenceInstance.resultsArePublished}">
    <g:render template="/assignment/player/Read/learner/show-results-elaastic"
              model="[interactionInstance: activeInteraction, user: user]"/>
  </g:if>
  <g:elseif
      test="${activeInteraction.interactionType == InteractionType.Read.name() && sequenceInstance.state == StateType.show.name()}">
    <g:render template="/assignment/player/Read/learner/info_not_published_yet-elaastic"
              model="[interactionInstance: activeInteraction]"/>
  </g:elseif>
  <g:elseif test="${sequenceInstance.state == StateType.afterStop.name()}">
    <g:render template="/assignment/player/Read/learner/info_not_published-elaastic"
              model="[interactionInstance: activeInteraction]"/>
  </g:elseif>
</g:if>

<div class="ui hidden divider"></div>

