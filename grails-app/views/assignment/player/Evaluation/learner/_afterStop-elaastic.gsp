<g:if test="${interactionInstance.sequence.isStopped()}">
    <div class="ui warning message" style="font-size: 1rem;">
        ${message(code: "player.sequence.readinteraction.beforeStart.message", args: [interactionInstance.rank])}
        <g:remoteLink controller="player"
                      action="updateSequenceDisplay"
                      id="${interactionInstance.sequenceId}"
                      title="Refresh"
                      update="sequence_${interactionInstance.sequenceId}">
            <i class="refresh icon"></i>
        </g:remoteLink></div>
</g:if>
<g:else>
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
</g:else>