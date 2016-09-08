<div class="panel panel-default">
    <div class="panel-heading"><g:message code="sequence.interaction.closurePhase" /></div>
    <div class="panel-body">
        <g:message code="sequence.interaction.resultDisplay" />
        <div id="schedulePhase3" class="hidden">
            <g:render template="/assignment/sequence/phase_schedule_form" model="[scheduleInstance:sequenceInstance?.readInteraction?.schedule, indexSchedule:3]"/>
        </div>
    </div>

</div>