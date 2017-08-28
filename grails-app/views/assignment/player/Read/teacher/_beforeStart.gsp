<g:set var="sequence" value="${interactionInstance.sequence}"/>
<p>
    <g:if test="${sequence.executionIsBlendedOrDistance()}">
        <g:remoteLink class="btn btn-success" controller="player" action="updateResultsAndSequenceDisplay" id="${sequence.id}" update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-refresh"></span> ${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}</g:remoteLink>
    </g:if>
    <g:remoteLink class="btn btn-success" controller="player" action="startInteraction" id="${interactionInstance.id}" update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-play"></span> ${message(code: "player.sequence.readinteraction.start", args: [interactionInstance.rank])}</g:remoteLink>
</p>