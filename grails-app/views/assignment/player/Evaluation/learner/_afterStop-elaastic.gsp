<div style="font-size: 1rem;">
<div class="ui warning message" role="alert">
    ${message(code: "player.sequence.interaction.afterResponseSubmission.message", args: [interactionInstance.sequence.activeInteraction.rank])}
    <g:remoteLink controller="player"
                  action="updateSequenceDisplay"
                  id="${interactionInstance.sequenceId}"
                  title="Refresh"
                  update="sequence_${interactionInstance.sequenceId}">
        <i class="refresh icon"></i>
    </g:remoteLink>
</div>
</div>