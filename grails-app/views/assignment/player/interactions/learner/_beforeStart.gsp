<div class="alert alert-warning" role="alert">
  ${message(code:"player.sequence.interaction.beforeStart.message", args:[1])}
  <g:remoteLink controller="player" action="updateSequenceDisplay" id="${sequenceInstance.id}" title="Refresh" update="sequence_${sequenceInstance.id}">
    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
  </g:remoteLink>
</div>