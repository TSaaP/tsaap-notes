<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:set var="displayedResultInteraction" value="${sequence.responseSubmissionInteraction}"/>
<g:set var="resultList" value="${displayedResultInteraction.resultsByAttempt()["1"]}"/>
<g:set var="spec" value="${displayedResultInteraction.interactionSpecification}"/>
<g:set var="userResponse" value="${displayedResultInteraction.responseForUser(user)}"/>
<g:each var="i" in="${(1..spec.itemCount)}">
    <g:set var="choiceStatus" value="${spec.expectedChoiceListContainsChoiceWithIndex(i) ? 'success' : 'danger'}"/>
    <g:set var="percentResult" value="${resultList[i]}"/>
    <div class="label label-${choiceStatus}">
        <g:if test="${userResponse.choiceList().contains(i)}">
            <g:set var="suffix" value="${spec.choiceWithIndexInExpectedChoiceList(i)?.score > 0 ? 'up' : 'down'}"/>
            <span class="glyphicon glyphicon-thumbs-${suffix}"></span>
        </g:if>
        ${message(code: "player.sequence.interaction.choice.label")} ${i}
    </div>

    <div class="progress">
        <div class="progress-bar progress-bar-${choiceStatus}" role="progressbar" aria-valuenow="${percentResult}"
             aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult}%">
            <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
        </div>
    </div>
</g:each>
<g:set var="percentResult" value="${resultList[0]}"/>
<div class="label label-warning">${message(code: "player.sequence.interaction.NoResponse.label")}</div>

<div class="progress">
    <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${percentResult}" aria-valuemin="0"
         aria-valuemax="100" style="width: ${percentResult}%">
        <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
    </div>
</div>

<div class="alert alert-info" role="alert">${message(code:"player.sequence.interaction.read.learner.show.score.message")} <g:formatNumber number="${userResponse.score}" type="number" maxFractionDigits="2"/></div>