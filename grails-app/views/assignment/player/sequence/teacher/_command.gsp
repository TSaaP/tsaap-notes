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

<%@ page import="org.tsaap.assignments.StateType" %>

%{-- TODO Toolbar--}%
%{--<div class="ui attached stackable labeled icon menu">--}%
%{--<a class="item">--}%
%{--<i class="red disabled undo alternate icon"></i>--}%
%{--<g:message code="common.reset"/>--}%
%{--</a>--}%
%{--<a class="item">--}%
%{--<i class="disabled backward icon"></i>--}%
%{--<g:message code="common.previous"/>--}%
%{--</a>--}%
%{--<a class="item">--}%
%{--<i class="disabled play icon"></i>--}%
%{--<g:message code="common.start"/>--}%
%{--</a>--}%
%{--<a class="item">--}%
%{--<i class="stop icon"></i>--}%
%{--<g:message code="common.stop"/>--}%
%{--</a>--}%
%{--<a class="item">--}%
%{--<i class="disabled forward icon"></i>--}%
%{--<g:message code="common.next"/>--}%
%{--</a>--}%
%{--</div>--}%

<div class="ui attached segment">

  <g:if test="${sequenceInstance.isStopped()}">
    <g:if test="${!sequenceInstance.executionIsFaceToFace() || !sequenceInstance.activeInteraction.isRead()}">
      <g:remoteLink class="ui button"
                    controller="player"
                    action="reopenSequence"
                    id="${sequenceInstance.id}"
                    update="sequence_${sequenceInstance.id}">
        <i class="play icon"></i>
        ${message(code: "player.sequence.reopenSequence")}
      </g:remoteLink>
    </g:if>
  </g:if>
  <g:else><!-- sequence in show state -->

    <g:if
        test="${interactionInstance.stateForTeacher(user) == StateType.beforeStart.name() && !interactionInstance.isRead()}">
      <p>
        <g:link class="ui primary button"
                controller="player"
                action="startInteraction"
                id="${interactionInstance.id}"
                params="[reloadPage: true]">
          <i class="play icon"></i>
          ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank])}
        </g:link>
      </p>
    </g:if>

    <g:elseif test="${interactionInstance.stateForTeacher(user) == StateType.show.name()}">

      <g:if test="${!interactionInstance.isRead()}">
        <g:link class="ui primary button"
                controller="player"
                action="stopInteraction"
                id="${interactionInstance.id}"
                params="[reloadPage: true]">
          <i class="stop icon"></i>
          ${message(code: "player.sequence.interaction.stop", args: [interactionInstance.rank])}
        </g:link>
      </g:if>
    </g:elseif>

    <g:elseif test="${interactionInstance.stateForTeacher(user) == StateType.afterStop.name()}">
      <g:if test="${!interactionInstance.isRead()}">
        <g:remoteLink class="ui primary button"
                      controller="player"
                      action="startInteraction"
                      id="${interactionInstance.id}"
                      update="sequence_${interactionInstance.sequenceId}">
          <i class="play icon"></i>
          ${message(code: "player.sequence.interaction.restart", args: [interactionInstance.rank])}
        </g:remoteLink>

        <g:if test="${interactionInstance.isResponseSubmission()}">
          <g:remoteLink class="ui primary button"
                        controller="player"
                        action="startNextInteraction"
                        id="${interactionInstance.id}"
                        update="sequence_${interactionInstance.sequenceId}">
            <i class="play icon"></i> ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank + 1])}</g:remoteLink>
        </g:if>
      </g:if>
    </g:elseif>


    <g:remoteLink class="ui primary button"
                  controller="player"
                  action="stopSequence"
                  id="${sequenceInstance.id}"
                  update="sequence_${sequenceInstance.id}">
      <i class="stop icon"></i>
      ${message(code: "player.sequence.readinteraction.stopSequence")}
    </g:remoteLink>
  </g:else>
  <g:if test="${sequenceInstance.resultsCanBePublished()}">
    <g:remoteLink class="ui primary button"
                  controller="player"
                  action="publishResultsForSequence"
                  id="${sequenceInstance.id}"
                  update="sequence_${sequenceInstance.id}">
      <i class="stop icon"></i>
      ${message(code: "player.sequence.publishResults")}
    </g:remoteLink>
  </g:if>
  <g:if test="${sequenceInstance.resultsArePublished}">
    <g:remoteLink class="ui primary button"
                  controller="player"
                  action="unpublishResultsForSequence"
                  id="${sequenceInstance.id}"
                  update="sequence_${sequenceInstance.id}">
      <i class="stop icon"></i>
      ${message(code: "player.sequence.unpublishResults")}
    </g:remoteLink>
  </g:if>
</div>
