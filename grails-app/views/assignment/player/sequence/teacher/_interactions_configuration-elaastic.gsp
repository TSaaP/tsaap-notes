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

<%@ page import="org.tsaap.assignments.QuestionType" %>


<div class="ui field">
  <div class="ui checkbox" id="phaseFirstSubmission_${sequenceId}">
    <input type="checkbox"
           class="hidden"
           name="studentsProvideExplanation"
           id="studentsProvideExplanation_${sequenceId}_${questionType}"
           value="true" checked />
    <label>
      <g:message
          code="sequence.interaction.studentsProvideAtextualExplanation"/>
    </label>
  </div>
</div>

<div class="ui inline fields" id="phaseConfrontation_${sequenceId}">
  <div class="field">
    <div class="ui checkbox">
      <input type="checkbox" checked disabled value="true">

      <label>
        <g:message
            code="sequence.interaction.studentsEvaluate"/>
      </label>
    </div>
  </div>

  <div class="field">
    <g:select id="responseToEvaluateCount_${sequenceId}‡"
              name="responseToEvaluateCount"
              class="compact"
              from="${5..1}"/>
    <g:message code="sequence.interaction.answers"/>
  </div>
</div>


<r:script>
    manageConfigurationChange("${sequenceId}", "${questionType}", $("#studentsProvideExplanation_${sequenceId}_${questionType}"));
</r:script>