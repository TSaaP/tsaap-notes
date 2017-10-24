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

<g:render template="/assignment/player/Read/teacher/dashboard-elaastic"
          model="[interactionInstance: interactionInstance, user: user, resultsArePublished: true]"/>
<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:if test="${!sequence.isStopped()}">
  <div style="margin-top: 15px">
    <g:remoteLink class="ui primary button"
                  controller="player"
                  action="stopSequence"
                  id="${sequence.id}"
                  update="sequence_${interactionInstance.sequenceId}">
      <i class="stop icon"></i>
      ${message(code: "player.sequence.readinteraction.stopSequence", args: [interactionInstance.rank])}
    </g:remoteLink>
  </div>
</g:if>


