<g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}"
          model="[statementInstance: sequenceInstance.statement]"/>
<div class="alert alert-warning" role="alert">
  ${message(code:"player.sequence.beforeStart.message")}
  <g:remoteLink controller="player" action="updateSequenceDisplay" id="${sequenceInstance.id}" title="Refresh" update="sequence_${sequenceInstance.id}">
    <span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
  </g:remoteLink>
</div>