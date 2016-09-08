<g:set var="evaluationSpecificationInstance" value="${sequenceInstance?.evaluationSpecification}"/>
<g:set var="hidden" value="${!(sequenceInstance?.evaluationInteraction) || sequenceInstance.evaluationInteraction.disabled()}"/>
<div class="panel panel-default ${hidden ? 'hidden' : ''}" id="phase_2">
    <div class="panel-heading"><g:message code="sequence.interaction.phase"/> 2</div>

    <div class="panel-body">
        <div class="checkbox">
            <label>
                <input type="checkbox" checked disabled value="true"> <g:message code="sequence.interaction.studentsEvaluate" />
                <g:select name="responseToEvaluateCount" from="${1..3}" value="${evaluationSpecificationInstance?.responseToEvaluateCount}"/>
                <g:message code="sequence.interaction.answers" />
            </label>
        </div>

        <div id="schedulePhase2" class="hidden">
            <g:render template="/assignment/sequence/phase_schedule_form" model="[scheduleInstance:sequenceInstance?.evaluationInteraction?.schedule, indexSchedule:2]"/>
        </div>
    </div>

</div>