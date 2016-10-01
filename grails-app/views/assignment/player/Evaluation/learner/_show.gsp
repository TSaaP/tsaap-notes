<g:set var="responsesToGrade" value="${interactionInstance.findRecommendedResponsesForUser(user)}"/>
<div class="alert alert-info">${message(code:'player.sequence.interaction.evaluation.intro')}</div>
<g:form>
    <ul class="list-group">
    <g:each in="${responsesToGrade}" var="response">
        <li class="list-group-item">
            <p>${message(code:'player.sequence.interaction.choice.label')} ${response.choiceList()}</p>
            <p>
                ${raw(response.explanation)}
            </p>
        </li>
    </g:each>
    </ul>
</g:form>