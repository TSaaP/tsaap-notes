<g:set var="spec" value="${interactionInstance.interactionSpecification}"/>
<g:set var="resultList" value="${interactionInstance.resultsAsList()}"/>
<g:each var="i" in="${(1..spec.itemCount)}">
    <g:set var="choiceStatus" value="${spec.expectedChoiceListContainsChoiceWithIndex(i) ? 'success' : 'danger'}"/>
    <g:set var="percentResult" value="${resultList[i]}"/>
    <div class="label label-${choiceStatus}">
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

