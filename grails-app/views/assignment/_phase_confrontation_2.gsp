<div class="panel panel-default hidden" id="phase_2">
    <div class="panel-heading"><g:message code="sequence.interaction.phase"/> 2</div>

    <div class="panel-body">
        <div class="checkbox">
            <label>
                <input type="checkbox" checked disabled value="true"> <g:message code="sequence.interaction.studentsEvaluate" />
                <g:select name="responseToEvaluateCount" from="${1..3}" value="${evaluationSpecificationInstance?.responseToEvaluateCount}"/>
                <g:message code="sequence.interaction.answers" />
            </label>
        </div>
    </div>
</div>

