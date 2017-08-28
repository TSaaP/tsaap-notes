<div class="alert alert-warning"
     role="alert">${message(code: "player.sequence.interaction.read.teacher.show.message", args: [interactionInstance.rank])}</div>
<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:if test="${sequence.hasExplanations()}">
    <g:if test="${sequence.statement.hasChoices()}">
        <g:set var="responses" value="${sequence.findAllGoodResponses()}"/>
        <g:set var="badResponses" value="${sequence.findAllBadResponses()}"/>
    </g:if>
    <g:else>
        <g:set var="responses" value="${sequence.findAllOpenResponses()}"/>
        <g:set var="badResponses" value="${[:]}"/>
    </g:else>
    <g:render template="/assignment/player/ExplanationList"
              model="[responses: responses, sequence: sequence, badResponses: badResponses]"/>
</g:if>
<g:if test="${sequence.executionIsBlendedOrDistance()}">
    <div style="margin-top: 15px">
        <g:remoteLink class="btn btn-success" controller="player" action="updateResultsAndSequenceDisplay"
                      id="${sequence.id}" update="sequence_${interactionInstance.sequenceId}"><span
                class="glyphicon glyphicon-refresh"></span> ${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}</g:remoteLink>
    </div>
</g:if>