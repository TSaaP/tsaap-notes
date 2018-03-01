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
<g:if test="${interactionInstance.sequence.isStopped()}">
  <div class="ui warning message" style="font-size: 1rem;">
    ${message(code: "player.sequence.readinteraction.beforeStart.message", args: [interactionInstance.rank])}
    <g:remoteLink controller="player"
                  action="updateSequenceDisplay"
                  id="${interactionInstance.sequenceId}"
                  title="Refresh"
                  update="sequence_${interactionInstance.sequenceId}">
      <i class="refresh icon"></i>
    </g:remoteLink></div>
</g:if>
<g:else>
<div style="font-size: 1rem;">
  <%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
  <g:set var="sequence" value="${interactionInstance.sequence}"/>
  <g:set var="responseInteractionInstance" value="${sequence.responseSubmissionInteraction}"/>
  <g:set var="responseInteractionSpec" value="${responseInteractionInstance.interactionSpecification}"/>
  <g:set var="responsesToGrade" value="${sequence.findRecommendedResponsesForUser(user)}"/>
  <g:if test="${sequence.userHasCompletedPhase2(user)}">
    <div class="ui warning message" role="alert">
      ${message(code: "player.sequence.interaction.afterResponseSubmission.message", args: [interactionInstance.sequence.activeInteraction.rank])}
      <g:remoteLink controller="player"
                    action="updateSequenceDisplay"
                    id="${interactionInstance.sequenceId}"
                    title="Refresh"
                    update="sequence_${interactionInstance.sequenceId}">
        <i class="refresh icon"></i>
      </g:remoteLink>
    </div>
  </g:if>
  <g:else>
    <g:form class="ui form">
      <g:hiddenField name="id" value="${interactionInstance.id}"/>
      <g:if test="${responsesToGrade}">
        <div class="ui info top attached message">${message(code: 'player.sequence.interaction.evaluation.intro')}</div>

        <g:each in="${responsesToGrade}" var="currentResponse" status="i">
          <div class="ui attached segment">
            <g:if test="${i < interactionInstance.interactionSpecification.responseToEvaluateCount}">
              <g:if test="${sequence.statement.hasChoices()}">
                <strong>${message(code: 'player.sequence.interaction.choice.label')} ${currentResponse.choiceList()}</strong>
                <br/>
              </g:if>
              ${raw(currentResponse.explanation)}

              <g:select from="${['1','2','3','4','5', '-1']}"
                        name="grade_${currentResponse.id}"
                        id="grade_${currentResponse.id}"
                        valueMessagePrefix="player.sequence.interaction.grade"
                        value="${currentResponse.getGradeFromUserAsString(user)}"/>

              <r:script>
                $('#grade_${currentResponse.id}').dropdown();
              </r:script>
            </g:if>
          </div>
        </g:each>
        <div class="ui hidden divider"></div>

      </g:if>
      <g:set var="shouldPresentExplanationAndConfidenceFields"
             value="${interactionInstance.sequence.executionIsBlendedOrDistance()}"/>
      <g:set var="responseSubmissionSpecificationInstance"
             value="${responseInteractionInstance.interactionSpecification}"/>
      <g:if test="${sequence.allowsSecondAttemptInLongProcess()}">
        <g:if test="${sequence.userHasSubmittedSecondAttempt(user)}">
          <div class="ui info message">
            ${message(code: 'player.sequence.interaction.secondAttemptSubmitted')}
          </div>
        </g:if>
        <g:else>
          <div class="ui info message">
            ${message(code: 'player.sequence.interaction.secondAttemptSubmittable')}
          </div>
          <g:render template="/assignment/player/ResponseSubmission/learner/response_form-elaastic"
                    model="[user                                       : user, interactionInstance: responseInteractionInstance, attempt: 2,
                            shouldPresentExplanationAndConfidenceFields: shouldPresentExplanationAndConfidenceFields,
                            responseSubmissionSpecificationInstance    : responseSubmissionSpecificationInstance]"/>
        </g:else>
      </g:if>
      <g:if
          test="${shouldPresentExplanationAndConfidenceFields && responseSubmissionSpecificationInstance.studentsProvideExplanation}">
        <g:submitToRemote controller="player"
                          action="submitGradesAndSecondAttempt"
                          update="sequence_${interactionInstance.sequenceId}"
                          class="ui primary button"
                          value="${message(code: 'player.sequence.interaction.submitResponse')}"
                          before="document.getElementById('explanation_${responseInteractionInstance.id}').textContent = CKEDITOR.instances.explanation_${responseInteractionInstance.id}.getData()"/>
      </g:if>
      <g:else>
        <g:submitToRemote controller="player"
                          action="submitGradesAndSecondAttempt"
                          update="sequence_${interactionInstance.sequenceId}"
                          class="ui primary button"
                          value="${message(code: 'player.sequence.interaction.submitResponse')}"/>
      </g:else>
    </g:form>
  </g:else>
</div>
</g:else>