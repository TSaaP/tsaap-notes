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
<g:set var="choiceSpecification" value="${interactionInstance.sequence.statement.getChoiceSpecificationObject()}"/>
<g:set var="isMultipleChoice" value="${interactionInstance.sequence.statement.isMultipleChoice() ?: false}"/>
<g:set var="hasChoices" value="${interactionInstance.sequence.statement.hasChoices()}"/>
<g:set var="firstAttemptResponse" value="${interactionInstance.responseForUser(user)}"/>

<g:hiddenField name="attempt" value="${attempt}"/>
<g:if test="${hasChoices}">
  <g:set var="itemCount" value="${choiceSpecification.itemCount}"/>
  <div class="field">
    <g:if test="${isMultipleChoice}">
      <div class="inline fields">
        <g:each in="${1..itemCount}" var="checkBoxElet" status="i">
          <div class="field">
            <div class="ui checkbox" id="multiple_choice_${interactionInstance.id}_${i}">
              <g:set var="choiceIsExpected"
                     value="${choiceSpecification?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>

              <input type="checkbox"
                     name="choiceList"
                     value="${i + 1}" ${firstAttemptResponse?.choiceList()?.contains(i + 1) ? 'checked' : ''}/>
              <label style="margin-right: 20px">${i + 1}</label>
            </div>
          </div>
          <r:script>
$('#multiple_choice_${interactionInstance.id}_${i}').checkbox();
          </r:script>
        </g:each>
      </div>
    </g:if>
    <g:else>
      <div class="inline fields">
        <g:each in="${1..itemCount}" var="radioBoxElet" status="i">
          <g:set var="choiceIsExpected"
                 value="${choiceSpecification?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
          <div class="field">
            <div class="ui radio checkbox" id="exclusive_choice_${interactionInstance.id}_${i}">
              <input type="radio" name="exclusiveChoice"
                     value="${i + 1}" ${firstAttemptResponse?.choiceList()?.contains(i + 1) ? 'checked' : ''}>
              <label style="margin-right: 20px">
                ${i + 1}
              </label>
            </div>
          </div>
          <r:script>
$('#exclusive_choice_${interactionInstance.id}_${i}').checkbox();
          </r:script>

        </g:each>
      </div>
    </g:else>
  </div>
</g:if>

<g:if test="${shouldPresentExplanationAndConfidenceFields}">
  <g:if test="${responseSubmissionSpecificationInstance.studentsProvideExplanation}">
    <div class="field">
      <label for="explanation_${interactionInstance.id}">
        <g:message code="player.sequence.interaction.explanation.label" default="Title"/>
      </label>
      <ckeditor:editor name="explanation" id="explanation_${interactionInstance.id}">
        ${firstAttemptResponse?.explanation}
      </ckeditor:editor>
    </div>
  </g:if>

  <g:if test="${responseSubmissionSpecificationInstance.studentsProvideConfidenceDegree}">
    <div class="field">
      <label for="confidenceDegree_${interactionInstance.id}">
        <g:message code="player.sequence.interaction.confidenceDegree.label"/>
      </label>
      <g:select class="ui dropdown"
                name="confidenceDegree"
                id="confidenceDegree_${interactionInstance.id}"
                from="${org.tsaap.assignments.ConfidenceDegreeEnum.values()}"
                optionKey="integerValue"
                optionValue="${{
                  message(code: 'player.sequence.interaction.confidenceDegree.' + it.name)
                }}"
                value="${firstAttemptResponse?.confidenceDegree}">
      </g:select>
      <r:script>
$('#confidenceDegree_${interactionInstance.id}').dropdown();
      </r:script>
    </div>
  </g:if>
</g:if>
