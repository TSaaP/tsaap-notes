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

<g:if test="${sequence.state == StateType.show.name()}">
  <g:if test="${sequence.executionIsFaceToFace()}">
    <g:if
        test="${activeInteraction.interactionType in [InteractionType.ResponseSubmission, InteractionType.Evaluation]*.name()}">
      <g:render
          template="/assignment/player/sequence/learner/sequenceInfo/phaseState/${activeInteractionState}-elaastic"
          model="[interactionInstance: activeInteraction]"/>
    </g:if>
    <g:elseif test="${activeInteraction.interactionType == InteractionType.Read.name()}">
      <g:render template="/assignment/player/Read/learner/result_publication_status"
                model="[interactionInstance: activeInteraction]"/>
    </g:elseif>
  </g:if>
  <g:else>
    <div class="ui blue bottom attached message">
      <g:message code="player.sequence.open"/>
    </div>
  </g:else>
</g:if>
<g:elseif test="${sequence.state == StateType.afterStop.name()}">
  <div class="ui bottom attached message">
    <g:message code="player.sequence.closed"/>
  </div>
</g:elseif>