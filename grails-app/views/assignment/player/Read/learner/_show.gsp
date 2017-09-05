<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:set var="displayedResultInteraction" value="${sequence.responseSubmissionInteraction}"/>
<g:set var="choiceSpecification" value="${sequence.statement.getChoiceSpecificationObject()}"/>
<g:if test="${sequence.statement.hasChoices()}">
    <g:set var="resultList" value="${displayedResultInteraction.resultsOfLastAttempt()}"/>
    <g:set var="userResponse" value="${displayedResultInteraction.lastAttemptResponseForUser(user)}"/>
    <g:each var="i" in="${(1..choiceSpecification.itemCount)}">
        <g:set var="choiceStatus"
               value="${choiceSpecification.expectedChoiceListContainsChoiceWithIndex(i) ? 'success' : 'danger'}"/>
        <g:set var="percentResult" value="${resultList[i]}"/>
        <div class="panel panel-${choiceStatus}">
            <div class="panel-heading">
                <g:if test="${userResponse?.choiceList()?.contains(i)}">
                    <g:set var="suffix"
                           value="${choiceSpecification.choiceWithIndexInExpectedChoiceList(i)?.score > 0 ? 'up' : 'down'}"/>
                    <span class="glyphicon glyphicon-thumbs-${suffix}"></span>
                </g:if>
                ${message(code: "player.sequence.interaction.choice.label")} ${i}
            </div>

            <div class="panel-body">
                <div class="progress">
                    <div class="progress-bar progress-bar-${choiceStatus}" role="progressbar"
                         aria-valuenow="${percentResult}"
                         aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult}%">
                        <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
                    </div>
                </div>
            </div>
        </div>
    </g:each>
    <g:set var="percentResult" value="${resultList[0]}"/>
    <g:if test="${percentResult}">
        <div class="panel panel-warning">
            <div class="panel-heading">${message(code: "player.sequence.interaction.NoResponse.label")}</div>

            <div class="panel-body">
                <div class="progress">
                    <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${percentResult}"
                         aria-valuemin="0"
                         aria-valuemax="100" style="width: ${percentResult}%">
                        <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
                    </div>
                </div>
            </div>
        </div>
    </g:if>
    <div class="alert alert-info"
         role="alert">${message(code: "player.sequence.interaction.read.learner.show.score.message")} <g:formatNumber
            number="${userResponse?.score}" type="number" maxFractionDigits="2"/></div>
</g:if>


<g:if test="${sequence.hasExplanations()}">
    <g:set var="attempt" value="${sequence.executionIsBlendedOrDistance() ? 2 : 1}"/>
    <g:if test="${sequence.statement.hasChoices()}">
        <g:set var="responses" value="${sequence.findAllGoodResponses(attempt)}"/>
    </g:if>
    <g:else>
        <g:set var="responses" value="${sequence.findAllOpenResponses(attempt)}"/>
    </g:else>
    <g:render template="/assignment/player/ExplanationList" model="[responses: responses, sequence: sequence]"/>
</g:if>

<g:if test="${sequence.executionIsDistance() && !sequence.isStopped()}">
    <div style="margin-top: 15px">
        <g:remoteLink class="btn btn-success" controller="player" action="updateResultsAndSequenceDisplay"
                      id="${sequence.id}" update="sequence_${interactionInstance.sequenceId}"><span
                class="glyphicon glyphicon-refresh"></span> ${message(code: "player.sequence.readinteraction.updateAllResults", args: [interactionInstance.rank])}</g:remoteLink>
    </div>
</g:if>