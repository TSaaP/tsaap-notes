<div class="panel panel-default">
    <div class="panel-heading"><g:message code="sequence.interaction.closurePhase" /></div>
    <div class="panel-body">
        <g:message code="sequence.interaction.resultDisplay" />
        <div id="schedulePhase3" class="hidden">
            <g:render template="/assignment/sequence/phase_schedule" model="[scheduleInstance:sequenceInstance?.readInteraction?.schedule]"/>
        </div>
    </div>

</div>