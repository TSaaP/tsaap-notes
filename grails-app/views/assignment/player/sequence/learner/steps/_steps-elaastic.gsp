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

<g:if test="${activeInteraction.interactionType == InteractionType.ResponseSubmission.toString()}">
  <g:set var="responseSubmissionCss" value="active"/>
  <g:set var="evaluationCss" value="disabled"/>
  <g:set var="readCss" value="disabled"/>
</g:if>
<g:elseif test="${activeInteraction.interactionType == InteractionType.Evaluation.toString()}">
  <g:set var="responseSubmissionCss" value=""/>
  <g:set var="evaluationCss" value="active"/>
  <g:set var="readCss" value="disabled"/>
</g:elseif>
<g:elseif test="${activeInteraction.interactionType == InteractionType.Read.toString() && activeInteraction.state != org.tsaap.assignments.StateType.afterStop.toString()}">
  <g:set var="responseSubmissionCss" value=""/>
  <g:set var="evaluationCss" value=""/>
  <g:set var="readCss" value="active"/>
</g:elseif>

<div class="ui three top attached steps">
  <div class="${responseSubmissionCss} step">
    <i class="comment outline icon"></i>

    <div class="content">
      <div class="title"><g:message code="sequence.phase.1" /></div>

      <div class="description"><g:message code="sequence.phase.ResponseSubmission.description" /></div>
      <g:if test="${activeInteraction.interactionType == InteractionType.ResponseSubmission.toString()}">
        <g:render template="/assignment/player/sequence/learner/steps/step_label-elaastic"
                  model="[activeInteraction: activeInteraction]"/>
      </g:if>
      <g:elseif test="${InteractionType.valueOf(activeInteraction.interactionType).ordinal() > InteractionType.ResponseSubmission.ordinal()}">
        <g:render template="/assignment/player/sequence/learner/steps/step_label_closed-elaastic" />
      </g:elseif>
    </div>
  </div>

  <div class="${evaluationCss} step">
    <i class="comments outline icon"></i>

    <div class="content">
      <div class="title"><g:message code="sequence.phase.2" /></div>

      <div class="description"><g:message code="sequence.phase.Evaluation.description" /></div>
      <g:if test="${activeInteraction.interactionType == InteractionType.Evaluation.toString()}">
        <g:render template="/assignment/player/sequence/learner/steps/step_label-elaastic"
                  model="[activeInteraction: activeInteraction]"/>
      </g:if>
      <g:elseif test="${InteractionType.valueOf(activeInteraction.interactionType).ordinal() > InteractionType.Evaluation.ordinal()}">
        <g:render template="/assignment/player/sequence/learner/steps/step_label_closed-elaastic" />
      </g:elseif>
    </div>
  </div>

  <div class="${readCss} step">
    <i class="bar chart icon"></i>

    <div class="content">
      <div class="title"><g:message code="sequence.phase.3" /></div>

      <div class="description"><g:message code="sequence.phase.Read.description" /></div>
      <g:if test="${activeInteraction.interactionType == InteractionType.Read.toString()}">
        <g:render template="/assignment/player/sequence/learner/steps/step_label-elaastic"
                  model="[activeInteraction: activeInteraction]"/>
      </g:if>
    </div>
  </div>
</div>

<div class="ui hidden divider"></div>