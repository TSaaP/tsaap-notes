<g:set var="sequence" value="${interactionInstance.sequence}"/>

<g:set var="responseSubmissionInteraction" value="${sequence.responseSubmissionInteraction}"/>
<div class="well well-sm">
    <p>
        <g:message code="player.sequence.interaction.responseCount" args="[1]"/>
        <span id="response_count_${responseSubmissionInteraction.id}">
            ${responseSubmissionInteraction.interactionResponseCount(1)}
        </span>
    </p>
    <g:if test="${sequence.isDefaultProcess()}">
        <g:if test="${sequence.statement.hasChoices()}">
            <p>
                <g:message code="player.sequence.interaction.responseCount" args="[2]"/>
                <span id="response_count_${responseSubmissionInteraction.id}">
                    ${responseSubmissionInteraction.interactionResponseCount(2)}
                </span>
            </p>
        </g:if>
        <p>
            ${message(code: 'player.sequence.interaction.evaluationCount', args: [])} <span
                id="evaluation_count_${responseSubmissionInteraction.id}">${responseSubmissionInteraction.evaluationCount()}</span>
        </p>
    </g:if>
</div>
<g:if test="${resultsArePublished}">
    <div class="alert alert-warning"
         role="alert">${message(code: "player.sequence.interaction.read.teacher.show.message", args: [interactionInstance.rank])}</div>
</g:if>

<g:if test="${sequence.hasExplanations()}">
    <g:set var="attempt" value="${sequence.executionIsBlendedOrDistance() ? 2 : 1}"/>
    <g:if test="${sequence.statement.hasChoices()}">
        <g:set var="responses" value="${sequence.findAllGoodResponses(attempt)}"/>
        <g:set var="badResponses" value="${sequence.findAllBadResponses(attempt)}"/>
    </g:if>
    <g:else>
        <g:set var="responses" value="${sequence.findAllOpenResponses(attempt)}"/>
        <g:set var="badResponses" value="${[:]}"/>
    </g:else>
    <g:render template="/assignment/player/${org.tsaap.skin.SkinUtil.getView(params, session, 'ExplanationList')}"
              model="[responses: responses, sequence: sequence, badResponses: badResponses]"/>
</g:if>
<g:if test="${sequence.executionIsBlendedOrDistance() && !sequence.isStopped()}">
    <div style="margin-top: 15px">
        <g:remoteLink class="btn btn-success" controller="player" action="updateResultsAndSequenceDisplay"
                      id="${sequence.id}" update="sequence_${interactionInstance.sequenceId}"><span
                class="glyphicon glyphicon-refresh"></span> ${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}</g:remoteLink>
    </div>
</g:if>

