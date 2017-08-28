<p>
<div class="well well-sm">
  <g:message code="player.sequence.interaction.responseCount"/>
  <span id="response_count_${interactionInstance.id}">
    ${interactionInstance.interactionResponseCount()}
  </span>
  <g:remoteLink controller="player" action="updateInteractionResponseCount" id="${interactionInstance.id}" title="Refresh" update="response_count_${interactionInstance.id}">
    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
  </g:remoteLink>
</div>
<g:remoteLink class="btn btn-success" controller="player" action="stopInteraction" id="${interactionInstance.id}" update="sequence_${interactionInstance.sequenceId}">
  <span class="glyphicon glyphicon-stop"></span>
  ${message(code: "player.sequence.interaction.stop", args: [interactionInstance.rank])}
</g:remoteLink>
</p>