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
    <g:message code="common.answer"/>
  </div>
  <g:if test="${!interactionInstance.hasResponseForUser(user, attempt)}">

    <div class="ui blue bottom attached message">
      <g:message code="sequence.responseSubmission.instruction"/>
    </div>

    <div class="ui basic padded large text segment">

      <g:set var="shouldPresentExplanationAndConfidenceFields"
             value="${attempt == 1 || interactionInstance.sequence.executionIsBlendedOrDistance()}"/>
      <g:set var="responseSubmissionSpecificationInstance" value="${interactionInstance.interactionSpecification}"/>
      <g:form class="ui form">
        <g:hiddenField name="id" value="${interactionInstance.id}"/>
        <g:render template="/assignment/player/ResponseSubmission/learner/response_form-elaastic"
                  model="[user                                       : user, interactionInstance: interactionInstance, attempt: 1,
                          shouldPresentExplanationAndConfidenceFields: shouldPresentExplanationAndConfidenceFields,
                          responseSubmissionSpecificationInstance    : responseSubmissionSpecificationInstance]"/>
        <g:if
            test="${shouldPresentExplanationAndConfidenceFields && responseSubmissionSpecificationInstance.studentsProvideExplanation}">
          <g:submitToRemote controller="player"
                            action="submitResponse"
                            update="sequence_${interactionInstance.sequenceId}"
                            class="ui primary button"
                            value="${message(code: 'player.sequence.interaction.submitResponse')}"
                            before="document.getElementById('explanation_${interactionInstance.id}').textContent = CKEDITOR.instances.explanation_${interactionInstance.id}.getData()"/>
        </g:if>
        <g:else>
          <g:submitToRemote controller="player"
                            action="submitResponse"
                            update="sequence_${interactionInstance.sequenceId}"
                            class="ui primary button"
                            value="${message(code: 'player.sequence.interaction.submitResponse')}"/>
        </g:else>
      </g:form>

    </div>

  </g:if>
  <g:else>
    <div class="ui blue bottom attached message">
      <g:message code="player.sequence.interaction.responseSubmission.hasBeenRecoded"/>
    </div>
  </g:else>
</div>