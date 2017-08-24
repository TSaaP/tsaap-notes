<%@ page import="org.tsaap.assignments.QuestionType" %>
<div class="panel panel-default" style="margin-top: 10px;">
    <div class="panel-heading"><g:message code="sequence.interaction.configuration"/></div>

    <div class="panel-body">
        <div class="checkbox" id="phaseFirstSubmission_${sequenceId}">
            <label>
                <input type="checkbox"
                       name="studentsProvideExplanation"
                       id="studentsProvideExplanation_${sequenceId}_${questionType}"
                       value="${QuestionType.OpenEnded.name() == questionType}" ${QuestionType.OpenEnded.name() == questionType ? 'checked' : ''}/> <g:message code="sequence.interaction.studentsProvideAtextualExplanation"/>
            </label>
        </div>

        <div class="checkbox hidden" id="phaseConfrontation_${sequenceId}">
            <label>
                <input type="checkbox" checked disabled value="true"> <g:message
                    code="sequence.interaction.studentsEvaluate"/>
            <g:select id="responseToEvaluateCount_${sequenceId}‡" name="responseToEvaluateCount" from="${5..1}"/>
            <g:message code="sequence.interaction.answers"/>
            </label>
        </div>
    </div>
</div>

<r:script>
    manageConfigurationChange("${sequenceId}", "${questionType}", $("#studentsProvideExplanation_${sequenceId}_${questionType}"));
</r:script>