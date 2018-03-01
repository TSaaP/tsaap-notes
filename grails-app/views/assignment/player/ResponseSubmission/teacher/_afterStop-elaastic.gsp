<g:render template="/assignment/player/sequence/teacher/dashboard/dashboard-elaastic"
          model="[interactionInstance:interactionInstance, user: user, resultsArePublished: false]"/>

<g:if test="${!interactionInstance.sequence.isStopped()}">
<hr/>
<p>
  <g:remoteLink class="ui primary button"
                controller="player"
                action="startInteraction"
                id="${interactionInstance.id}"
                update="sequence_${interactionInstance.sequenceId}">
    <i class="play icon"></i> ${message(code: "player.sequence.interaction.restart", args: [interactionInstance.rank])}</g:remoteLink>
  <g:remoteLink class="ui primary button"
                controller="player"
                action="startNextInteraction"
                id="${interactionInstance.id}"
                update="sequence_${interactionInstance.sequenceId}">
    <i class="play icon"></i> ${message(code: "player.sequence.interaction.start", args: [interactionInstance.rank + 1])}</g:remoteLink>
</p>
</g:if>