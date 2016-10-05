<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<g:set var="responsesToGrade" value="${interactionInstance.findRecommendedResponsesForUser(user)}"/>
<div class="alert alert-info">${message(code: 'player.sequence.interaction.evaluation.intro')}</div>
<ul class="list-group">
    <g:each in="${(0..<interactionInstance.interactionSpecification.responseToEvaluateCount)}" var="i">
        <g:set var="currentResponse" value="${responsesToGrade[i]}"/>
        <li class="list-group-item">
            <p>
                <strong>${message(code: 'player.sequence.interaction.choice.label')} ${currentResponse.choiceList()}</strong>
                 <br/>
                ${raw(currentResponse.explanation)}
                <input id="grade_${currentResponse.id}" name="grade_${currentResponse.id}" class="rating" >
            </p>
        </li>
        <r:script>
            $("#grade_${currentResponse.id}").rating({
                size: "xs",
                step: 1,
                showCaption: false,
                showClear: false,
                language: "${RequestContextUtils.getLocale(request).language}"
            });
        </r:script>
    </g:each>
</ul>
<g:set var="responseInteractionInstance" value="${interactionInstance.sequence.responseSubmissionInteraction}"/>
<g:render template="/assignment/player/ResponseSubmission/learner/show"
          model="[user: user, interactionInstance: responseInteractionInstance, attempt: 2]"/>
