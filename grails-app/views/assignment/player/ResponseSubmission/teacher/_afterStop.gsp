<g:set var="choiceSpecification" value="${interactionInstance.sequence.statement.getChoiceSpecificationObject()}"/>
<g:if test="${interactionInstance.sequence.statement.hasChoices()}">
    <g:set var="resultList" value="${interactionInstance.resultsByAttempt()["1"]}"/>
    <g:set var="resultList2" value="${interactionInstance.resultsByAttempt()?.get("2")}"/>
    <g:each var="i" in="${(1..choiceSpecification.itemCount)}">
        <g:set var="choiceStatus" value="${choiceSpecification.expectedChoiceListContainsChoiceWithIndex(i) ? 'success' : 'danger'}"/>
        <g:set var="percentResult" value="${resultList?.get(i)}"/>
        <g:set var="percentResult2" value="${resultList2?.get(i)}"/>
        <div class="panel panel-${choiceStatus}">
            <div class="panel-heading">
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
                <g:if test="${percentResult2 != null}">
                    <div class="progress">
                        <div class="progress-bar progress-bar-${choiceStatus}" role="progressbar"
                             aria-valuenow="${percentResult2}"
                             aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult2}%">
                            <span><g:formatNumber number="${percentResult2}" type="number"
                                                  maxFractionDigits="2"/>%</span>
                        </div>
                    </div>
                </g:if>
            </div>
        </div>
    </g:each>
    <g:set var="percentResult" value="${resultList?.get(0)}"/>
    <g:set var="percentResult2" value="${resultList2?.get(0)}"/>
    <g:if test="${percentResult || percentResult2}">
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
                <g:if test="${percentResult2 != null}">
                    <div class="progress">
                        <div class="progress-bar progress-bar-warning" role="progressbar"
                             aria-valuenow="${percentResult2}"
                             aria-valuemin="0"
                             aria-valuemax="100" style="width: ${percentResult2}%">
                            <span><g:formatNumber number="${percentResult2}" type="number"
                                                  maxFractionDigits="2"/>%</span>
                        </div>
                    </div>
                </g:if>
            </div>
        </div>
    </g:if>
</g:if>


