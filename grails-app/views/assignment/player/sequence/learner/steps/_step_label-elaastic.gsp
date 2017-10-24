<%@ page import="org.tsaap.assignments.InteractionType" %>
<%@ page import="org.tsaap.assignments.ExecutionContextType" %>
<%@ page import="org.tsaap.assignments.StateType" %>

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

<g:if test="${activeInteraction.sequence.executionContext == ExecutionContextType.FaceToFace.toString()}">
  <g:if test="${activeInteraction.state == StateType.beforeStart.toString()}">
    <g:if test="${activeInteraction.interactionType == InteractionType.Read.toString()}">
      <g:render template="/assignment/player/sequence/learner/steps/step_label_read_before_start-elaastic"
                model="[activeInteraction: activeInteraction]"/>
    </g:if>
    <g:else>
      <div class="ui label">
        <g:message code="sequence.state.beforeStart"/>
      </div>
    </g:else>
  </g:if>
  <g:elseif test="${activeInteraction.state == StateType.show.toString()}">
    <g:if test="${activeInteraction.interactionType == InteractionType.Read.toString()}">
      <div class="ui blue label">
        <g:message code="sequence.state.published"/>
      </div>
    </g:if>
    <g:else>
      <div class="ui blue label">
        <g:message code="sequence.state.inprogress"/>
      </div>
    </g:else>
  </g:elseif>
  <g:elseif test="${activeInteraction.state == StateType.afterStop.toString()}">
    <g:render template="/assignment/player/sequence/learner/steps/step_label_closed-elaastic"/>
  </g:elseif>
</g:if>

<g:elseif test="${activeInteraction.sequence.executionContext == ExecutionContextType.Distance.toString()}">
  <g:if test="${activeInteraction.interactionType == InteractionType.Read.toString()}">
    <g:if test="${activeInteraction.state == StateType.afterStop.toString()}">
      <g:render template="/assignment/player/sequence/learner/steps/step_label_closed-elaastic"/>
    </g:if>
    <g:else>
      <div class="ui blue label">
        <g:message code="sequence.state.published"/>
      </div>
    </g:else>
  </g:if>
  <g:else>
    <div class="ui blue label">
      <g:message code="sequence.state.inprogress"/>
    </div>
  </g:else>
</g:elseif>

<g:elseif test="${activeInteraction.sequence.executionContext == ExecutionContextType.Blended.toString()}">
  <g:if test="activeInteraction.interactionType == InteractionType.Read.toString()">
    <g:if test="${activeInteraction.state == StateType.beforeStart.toString()}">
      <g:render template="/assignment/player/sequence/learner/steps/step_label_read_before_start-elaastic"
                model="[activeInteraction: activeInteraction]"/>
    </g:if>
    <g:elseif test="${activeInteraction.state == StateType.afterStop.toString()}">
      <g:render template="/assignment/player/sequence/learner/steps/step_label_closed-elaastic"/>
    </g:elseif>
    <g:else>
      <div class="ui label">
        <g:message code="sequence.state.published"/>
      </div>
    </g:else>
  </g:if>
  <g:else>
    <div class="ui blue label">
      <g:message code="sequence.state.inprogress"/>
    </div>
  </g:else>

</g:elseif>

<g:else>
  Error !
</g:else>

