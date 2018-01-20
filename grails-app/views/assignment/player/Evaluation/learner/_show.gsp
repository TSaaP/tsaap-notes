<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:set var="responseInteractionInstance" value="${sequence.responseSubmissionInteraction}"/>
<g:set var="responseInteractionSpec" value="${responseInteractionInstance.interactionSpecification}"/>
<g:set var="responsesToGrade" value="${sequence.findRecommendedResponsesForUser(user)}"/>
<g:if test="${sequence.userHasCompletedPhase2(user)}">
    <div class="alert alert-warning"
         role="alert">${message(code: "player.sequence.interaction.afterResponseSubmission.message", args: [interactionInstance.sequence.activeInteraction.rank])} <g:remoteLink
            controller="player" action="updateSequenceDisplay" id="${interactionInstance.sequenceId}" title="Refresh"
            update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-refresh"
                                                                      aria-hidden="true"></span></g:remoteLink></div>
</g:if>
<g:else>
    <g:form>
        <g:hiddenField name="id" value="${interactionInstance.id}"/>
        <g:if test="${responsesToGrade}">
            <div class="alert alert-info">${message(code: 'player.sequence.interaction.evaluation.intro')}</div>
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
                                <g:select from="${['1','2','3','4','5','-1']}" name="grade_${currentResponse.id}" id="grade_${currentResponse.id}" valueMessagePrefix="player.sequence.interaction.grade" value="-1"/>
                            </p>
                        </li>
                    </g:if>
                </g:each>
            </ul>

        </g:if>
        <g:set var="shouldPresentExplanationAndConfidenceFields"
               value="${interactionInstance.sequence.executionIsBlendedOrDistance()}"/>
        <g:set var="responseSubmissionSpecificationInstance" value="${responseInteractionInstance.interactionSpecification}"/>
        <g:if test="${sequence.allowsSecondAttemptInLongProcess()}">
            <g:if test="${sequence.userHasSubmittedSecondAttempt(user)}">
                <div class="alert alert-info">${message(code: 'player.sequence.interaction.secondAttemptSubmitted')}</div>
            </g:if>
            <g:else>
                <div class="alert alert-info">${message(code: 'player.sequence.interaction.secondAttemptSubmittable')}</div>
                <g:render template="/assignment/player/ResponseSubmission/learner/response_form"
                          model="[user: user, interactionInstance: responseInteractionInstance, attempt: 2,
                                  shouldPresentExplanationAndConfidenceFields:shouldPresentExplanationAndConfidenceFields,
                                  responseSubmissionSpecificationInstance:responseSubmissionSpecificationInstance]"/>
            </g:else>
        </g:if>
        <g:if test="${shouldPresentExplanationAndConfidenceFields && responseSubmissionSpecificationInstance.studentsProvideExplanation}">
            <g:submitToRemote controller="player" action="submitGradesAndSecondAttempt"
                              update="sequence_${interactionInstance.sequenceId}" class="btn btn-default"
                              value="${message(code: 'player.sequence.interaction.submitResponse')}"
                              before="document.getElementById('explanation_${responseInteractionInstance.id}').textContent = CKEDITOR.instances.explanation_${responseInteractionInstance.id}.getData()"/>
        </g:if>
        <g:else>
            <g:submitToRemote controller="player" action="submitGradesAndSecondAttempt"
                              update="sequence_${interactionInstance.sequenceId}" class="btn btn-default"
                              value="${message(code: 'player.sequence.interaction.submitResponse')}"/>
        </g:else>
    </g:form>
</g:else>