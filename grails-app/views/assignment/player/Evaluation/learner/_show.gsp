<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:set var="responseInteractionInstance" value="${sequence.responseSubmissionInteraction}"/>
<g:set var="responseInteractionSpec" value="${responseInteractionInstance.interactionSpecification}"/>
<g:set var="responsesToGrade" value="${sequence.findRecommendedResponsesForUser(user)}"/>
<g:if test="${!responseInteractionInstance.hasResponseForUser(user, 2)}">
    <g:if test="${responsesToGrade}">
        <div class="alert alert-info">${message(code: 'player.sequence.interaction.evaluation.intro')}</div>
        <g:form>
            <g:hiddenField name="id" value="${interactionInstance.id}"/>
            <ul class="list-group">
                <g:each in="${responsesToGrade}" var="currentResponse" status="i">
                    <g:if test="${i < interactionInstance.interactionSpecification.responseToEvaluateCount}">
                        <li class="list-group-item">
                            <p>
                                <g:if test="${sequence.statement.hasChoices()}">
                                    <strong>${message(code: 'player.sequence.interaction.choice.label')} ${currentResponse.choiceList()}</strong>
                                    <br/>
                                </g:if>
                                ${raw(currentResponse.explanation)}
                                <input id="grade_${currentResponse.id}" name="grade_${currentResponse.id}" class="rating"
                                       value="${currentResponse.getGradeFromUser(user)}">
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
                    </g:if>
                </g:each>
            </ul>
            <g:submitToRemote controller="player" action="submitGrades"
                              update="sequence_${interactionInstance.sequenceId}" class="btn btn-default"
                              value="${message(code: 'player.sequence.interaction.submitGrades')}"/>
            <p></p>
        </g:form>
    </g:if>
    <g:else>
        <div class="alert alert-info">${message(code: 'player.sequence.interaction.evaluation.intro.noresponsestograde')}</div>
    </g:else>
</g:if>
<g:if test="${sequence.statement.hasChoices()}">
    <g:render template="/assignment/player/ResponseSubmission/learner/show"
              model="[user: user, interactionInstance: responseInteractionInstance, attempt: 2]"/>
</g:if><g:else>
    <div class="alert alert-warning"
         role="alert">${message(code: "player.sequence.interaction.afterResponseSubmission.message", args: [interactionInstance.sequence.activeInteraction.rank])} <g:remoteLink
            controller="player" action="updateSequenceDisplay" id="${interactionInstance.sequenceId}" title="Refresh"
            update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-refresh"
                                                                      aria-hidden="true"></span></g:remoteLink></div>
</g:else>
