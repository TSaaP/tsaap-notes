<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:render template="/assignment/player/Read/teacher/dashboard" model="[interactionInstance:interactionInstance, user:user, resultsArePublished: false]"/>
<div style="margin-top: 15px">
    <g:remoteLink class="btn btn-success" controller="player" action="startInteraction" id="${interactionInstance.id}" update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-play"></span> ${message(code: "player.sequence.readinteraction.start", args: [interactionInstance.rank])}</g:remoteLink>
</div>