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
<g:set var="sequence" value="${interactionInstance.sequence}"/>

<div class="ui segment">
  <div class="ui dividing header">
    <g:message code="sequence.phase.Evaluation.description"/>
  </div>


  <g:if test="${!sequence.userHasCompletedPhase2(user)}">

    <%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
    <g:set var="responseInteractionInstance" value="${sequence.responseSubmissionInteraction}"/>
    <g:set var="responseInteractionSpec" value="${responseInteractionInstance.interactionSpecification}"/>
    <g:set var="responsesToGrade" value="${sequence.findRecommendedResponsesForUser(user)}"/>

    <g:form class="ui form">
      <g:hiddenField name="id" value="${interactionInstance.id}"/>
      <g:if test="${responsesToGrade}">
        <div class="ui blue message">
          ${message(code: 'player.sequence.interaction.evaluation.intro')}
        </div>

        <div id="phase2-evaluation-app">
          <g:each in="${responsesToGrade}" var="currentResponse" status="i">
            <div class="ui attached segment">
              <g:if test="${i < interactionInstance.interactionSpecification.responseToEvaluateCount}">
                <g:if test="${sequence.statement.hasChoices()}">
                  <strong>${message(code: 'player.sequence.interaction.choice.label')} ${currentResponse.choiceList()}</strong>
                  <br/>
                </g:if>
                ${raw(currentResponse.explanation)}


                <evaluation-input current-response-id="${currentResponse.id}"
                                  value="${currentResponse.getGradeFromUserAsString(user)}"></evaluation-input>

              </g:if>
            </div>
          </g:each>
        </div>

        <div class="ui hidden divider"></div>

      </g:if>
      <g:set var="shouldPresentExplanationAndConfidenceFields"
             value="${interactionInstance.sequence.executionIsBlendedOrDistance()}"/>
      <g:set var="responseSubmissionSpecificationInstance"
             value="${responseInteractionInstance.interactionSpecification}"/>
      <g:if test="${sequence.allowsSecondAttemptInLongProcess()}">
        <g:if test="${sequence.userHasSubmittedSecondAttempt(user)}">
          <div class="ui blue message">
            ${message(code: 'player.sequence.interaction.secondAttemptSubmitted')}
          </div>
        </g:if>
        <g:else>
          <div class="ui blue message">
            ${message(code: 'player.sequence.interaction.secondAttemptSubmittable')}
          </div>

          <div class="ui basic segment">
            <g:render template="/assignment/player/ResponseSubmission/learner/response_form-elaastic"
                      model="[user                                       : user, interactionInstance: responseInteractionInstance, attempt: 2,
                              shouldPresentExplanationAndConfidenceFields: shouldPresentExplanationAndConfidenceFields,
                              responseSubmissionSpecificationInstance    : responseSubmissionSpecificationInstance]"/>
          </div>
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

  </g:if>
  <g:else>
    <div class="ui blue message">
      <g:message code="player.sequence.phase.completedByUser"
                 args="[interactionInstance.sequence.activeInteraction.rank]"/>
    </div>
  </g:else>
</div>


<r:style>
  .field.grade .active.item {
    background-color: #dff0ff !important;
    color: #0e6eb8 !important;
  }
</r:style>

<r:script>
  new Vue({
    el: '#phase2-evaluation-app',
    components: {
      'evaluation-input' : {
        props: ['currentResponseId', 'value'],
        template: '\
        <div class="field grade">\
            <input type="hidden" :id="\'grade_\'+currentResponseId" :name="\'grade_\'+currentResponseId" v-model="grade" />\
            <label><g:message code="player.sequence.interaction.your.evaluation"/></label>\
  <div class="ui stackable pagination menu">\
    <a class="item" v-bind:class="{ active: grade === -1 }" v-on:click="grade = -1">\
    ${g.message(code: 'player.sequence.interaction.grade.-1').replaceAll("'", "\\\\u0027")}\
    </a>\
    <div class="disabled item">\
    \
    </div>\
    \
    <g:each in="[1, 2, 3, 4, 5]" var="grade">\
      <a class="item" \
         v-bind:class="{ active: grade === ${grade} }" \
         v-on:click="grade = ${grade}" \
         data-inverted="" \
         data-tooltip="${raw(g.message(code: 'player.sequence.interaction.grade.' + grade).replaceAll("'", "\\\\u0027"))}" \
         data-position="top center">\
      ${grade}\
      </a>\
    </g:each>\
  </div>\
        </div>',
        data: function() {
          return {
            grade: this.value
          }
        }
      }
    }
});
</r:script>
