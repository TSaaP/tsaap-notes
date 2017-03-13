<div class="panel panel-default" id="phase_2" style="margin-top: 10px">
    <div class="panel-heading"><g:message code="sequence.interaction.phase"/> 2</div>

    <div class="panel-body">
        <div class="checkbox">
            <label>
                <input type="checkbox" checked disabled value="true"> <g:message code="sequence.interaction.studentsEvaluate" />
                <g:select id="responseToEvaluateCount_${sequenceId}" name="responseToEvaluateCount" from="${3..1}"/>
                <g:message code="sequence.interaction.answers" />
            </label>
        </div>
    </div>

</div>