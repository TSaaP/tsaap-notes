<g:set var="responsesToGrade" value="${interactionInstance.findRecommendedResponsesForUser(user)}"/>
<div class="alert alert-info">${message(code:'player.sequence.interaction.evaluation.intro')}</div>
<g:form>
    <ul class="list-group">
    <g:each in="${(0..<interactionInstance.interactionSpecification.responseToEvaluateCount)}" var="i">
        <g:set var="currentResponse" value="${responsesToGrade[i]}"/>
        <li class="list-group-item">
            <p>
                <strong>${message(code:'player.sequence.interaction.choice.label')} ${currentResponse.choiceList()}</strong><br/>
                ${raw(currentResponse.explanation)}
            </p>
        </li>
    </g:each>
    </ul>
</g:form>
<g:set var="responseInteractionInstance" value="${interactionInstance.sequence.responseSubmissionInteraction}"/>
<g:render template="/assignment/player/ResponseSubmission/learner/show" model="[user:user, interactionInstance:responseInteractionInstance, attempt:2]"/>