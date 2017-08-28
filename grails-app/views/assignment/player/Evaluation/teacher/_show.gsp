<g:set var="responseInteraction" value="${interactionInstance.sequence.responseSubmissionInteraction}"/>
<p>
<g:if test="${interactionInstance.sequence.statement.hasChoices()}">
    <div class="well well-sm">${message(code: 'player.sequence.interaction.secondAttemptSubmissionCount', args: [])} <span
            id="second_attempt_count_${responseInteraction.id}">${responseInteraction.interactionResponseCount(2)}</span> <g:remoteLink
            controller="player" action="updateSecondAttemptCount" id="${responseInteraction.id}" title="Refresh"
            update="second_attempt_count_${responseInteraction.id}"><span class="glyphicon glyphicon-refresh"
                                                                          aria-hidden="true"></span></g:remoteLink>
    </div>
</g:if><g:else>
    <div class="well well-sm">${message(code: 'player.sequence.interaction.evaluationCount', args: [])} <span
            id="evaluation_count_${responseInteraction.id}">${responseInteraction.evaluationCount()}</span> <g:remoteLink
            controller="player" action="updateEvaluationCount" id="${responseInteraction.id}" title="Refresh"
            update="evaluation_count_${responseInteraction.id}"><span class="glyphicon glyphicon-refresh"
                                                                          aria-hidden="true"></span></g:remoteLink>
    </div>
</g:else>
<g:remoteLink class="btn btn-success" controller="player" action="stopInteraction" id="${interactionInstance.id}"
              update="sequence_${interactionInstance.sequenceId}"><span
        class="glyphicon glyphicon-stop"></span> ${message(code: "player.sequence.interaction.stop", args: [interactionInstance.rank])}</g:remoteLink>
</p>