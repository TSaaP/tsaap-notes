<g:render template="/assignment/player/Read/teacher/dashboard" model="[interactionInstance:interactionInstance, user:user, resultsArePublished: true]"/>
<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:if test="${!sequence.isStopped()}">
    <div style="margin-top: 15px">
        <g:remoteLink class="btn btn-success" controller="player" action="stopSequence"
                      id="${sequence.id}" update="sequence_${interactionInstance.sequenceId}"><span
                class="glyphicon glyphicon-stop"></span> ${message(code: "player.sequence.readinteraction.stopSequence", args: [interactionInstance.rank])}</g:remoteLink>
    </div>
</g:if>

