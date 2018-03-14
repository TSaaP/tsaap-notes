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

<div class="ui attached stackable icon menu">

  <g:if test="${sequenceInstance.isStopped()}">
    <g:if test="${!sequenceInstance.executionIsFaceToFace() || !sequenceInstance.activeInteraction.isRead()}">
      <g:set var="showReopenSequence" value="${true}"/>
    </g:if>
  </g:if>
  <g:else><!-- sequence in show state -->

    <g:if
        test="${interactionInstance.stateForTeacher(user) == StateType.beforeStart.name() && !interactionInstance.isRead()}">

      <g:link class="item"
              controller="player"
              action="startInteraction"
              id="${interactionInstance.id}"
              params="[reloadPage: true]">
        <i class="green play icon"></i>
        &nbsp; ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank])}
      </g:link>

    </g:if>

    <g:elseif test="${interactionInstance.stateForTeacher(user) == StateType.show.name()}">

      <g:if test="${!interactionInstance.isRead()}">
        <a class="item">

          <i class="disabled green play icon"></i>
          <span
              style="opacity: 0.45;">&nbsp; ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank])}</span>

        </a>

        <g:link class="item"
                controller="player"
                action="stopInteraction"
                id="${interactionInstance.id}"
                params="[reloadPage: true]">
          <i class="pause icon"></i>
          &nbsp; ${message(code: "player.sequence.interaction.stop", args: [interactionInstance.rank])}
        </g:link>
      </g:if>
    </g:elseif>

    <g:elseif test="${interactionInstance.stateForTeacher(user) == StateType.afterStop.name()}">
      <g:if test="${!interactionInstance.isRead()}">
        <g:if test="${interactionInstance.isResponseSubmission()}">
          <g:remoteLink class="item"
                        controller="player"
                        action="startNextInteraction"
                        id="${interactionInstance.id}"
                        update="sequence_${interactionInstance.sequenceId}">
            <i class="green play icon"></i> &nbsp; ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank + 1])}</g:remoteLink>
        </g:if>

        <g:remoteLink class="item"
                      controller="player"
                      action="startInteraction"
                      id="${interactionInstance.id}"
                      update="sequence_${interactionInstance.sequenceId}">
          <i class="red undo alternate  icon"></i>
          &nbsp; ${message(code: "player.sequence.interaction.restart", args: [interactionInstance.rank])}
        </g:remoteLink>

      </g:if>
    </g:elseif>

    <g:set var="showStopSequence" value="${true}"/>
  </g:else>

  <div class="right menu">
    <g:if test="${showReopenSequence}">
      <g:remoteLink class="item"
                    controller="player"
                    action="reopenSequence"
                    id="${sequenceInstance.id}"
                    update="sequence_${sequenceInstance.id}">
        <i class="red undo alternate icon"></i>
        &nbsp; ${message(code: "player.sequence.reopenSequence")}
      </g:remoteLink>
    </g:if>

    <g:if test="${showStopSequence}">
      <g:remoteLink class="item"
                    controller="player"
                    action="stopSequence"
                    id="${sequenceInstance.id}"
                    update="sequence_${sequenceInstance.id}">
        <i class="red stop icon"></i>
        &nbsp; ${message(code: "player.sequence.readinteraction.stopSequence")}
      </g:remoteLink>
    </g:if>

    <g:if test="${sequenceInstance.resultsCanBePublished()}">
      <g:remoteLink class="item"
                    controller="player"
                    action="publishResultsForSequence"
                    id="${sequenceInstance.id}"
                    update="sequence_${sequenceInstance.id}">
        <i class="feed icon"></i>
        &nbsp; ${message(code: "player.sequence.publishResults")}
      </g:remoteLink>
    </g:if>

    <g:if test="${sequenceInstance.resultsArePublished}">
      <g:remoteLink class="item"
                    controller="player"
                    action="unpublishResultsForSequence"
                    id="${sequenceInstance.id}"
                    update="sequence_${sequenceInstance.id}">
        <i class="red close icon"></i>
        &nbsp; ${message(code: "player.sequence.unpublishResults")}
      </g:remoteLink>
    </g:if>
  </div>
</div>
