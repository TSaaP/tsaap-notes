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

<g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}-elaastic"
          model="[statementInstance: sequenceInstance.statement]"/>

<g:set var="questionType" value="${sequenceInstance.statement.questionType.name()}"/>
<div id="interactionSpec_${sequenceInstance.id}">
  <form class="ui form">
    <input type="hidden" name="id" value="${sequenceInstance.id}">

    <h5 class="ui top attached block header">
      <g:message code="sequence.interaction.executionContext"/>
    </h5>

    <div class="ui bottom attached segment">

      <div class="inline fields">
        <div class="field">
          <div class="ui radio checkbox">
            <input type="radio" name="executionContext" id="executionContext_${sequenceInstance.id}_${questionType}"
                   value="FaceToFace" checked="checked"/>
            <label for="executionContext_${sequenceInstance.id}_${questionType}">
              <g:message code="sequence.interaction.executionContext.faceToFace"
                         for="executionContext_${sequenceInstance.id}_${questionType}"/>
            </label>
          </div>
        </div>

        <div class="field">
          <div class="ui radio checkbox fieldExecutionContext">
            <input type="radio" name="executionContext" id="executionContext_${sequenceInstance.id}_${questionType}"
                   value="Distance"/>
            <label for="executionContext_${sequenceInstance.id}_${questionType}">
              <g:message code="sequence.interaction.executionContext.distance"/>
            </label>
          </div>
        </div>

        <div class="field">
          <div class="ui radio checkbox">
            <input type="radio" name="executionContext" id="executionContext_${sequenceInstance.id}_${questionType}"
                   value="Blended">
            <label for="executionContext_${sequenceInstance.id}_${questionType}">
              <g:message code="sequence.interaction.executionContext.blended"/>
            </label>
          </div>
        </div>
      </div>

    </div>


    <div id="configuration_${sequenceInstance.id}">
      <g:render template="/assignment/player/sequence/teacher/interactions_configuration-elaastic"
                model="[sequenceId: sequenceInstance.id, questionType: questionType]"/>
    </div>

    <div class="ui hidden divider"></div>
    <div>
      <g:submitToRemote class="ui primary button"
                        url="[action: 'initializeInteractionsAndStartFirst', controller: 'player']"
                        update="sequence_${sequenceInstance.id}"
                        value="${message(code: "player.sequence.start")}"/>
    </div>
  </form>
</div>