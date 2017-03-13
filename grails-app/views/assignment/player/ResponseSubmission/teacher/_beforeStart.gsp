<p>
    <g:remoteLink class="btn btn-success" controller="player" action="startInteraction" id="${interactionInstance.id}" update="sequence_${interactionInstance.sequenceId}">
        <span class="glyphicon glyphicon-play"></span>
        ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank])}
    </g:remoteLink>
</p>