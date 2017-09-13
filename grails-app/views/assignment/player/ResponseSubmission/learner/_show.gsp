<g:if test="${interactionInstance.hasResponseForUser(user, attempt)}">
    <div class="alert alert-warning"
         role="alert">${message(code: "player.sequence.interaction.afterResponseSubmission.message", args: [interactionInstance.sequence.activeInteraction.rank])} <g:remoteLink
            controller="player" action="updateSequenceDisplay" id="${interactionInstance.sequenceId}" title="Refresh"
            update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-refresh"
                                                                      aria-hidden="true"></span></g:remoteLink></div>
</g:if>
<g:else>
    <g:set var="shouldPresentExplanationAndConfidenceFields"
           value="${attempt == 1 || interactionInstance.sequence.executionIsBlendedOrDistance()}"/>
    <g:set var="responseSubmissionSpecificationInstance" value="${interactionInstance.interactionSpecification}"/>
    <g:form>
        <g:hiddenField name="id" value="${interactionInstance.id}"/>
        <g:render template="/assignment/player/ResponseSubmission/learner/response_form" model="[user: user, interactionInstance:interactionInstance,attempt:1,
                                                                                                 shouldPresentExplanationAndConfidenceFields:shouldPresentExplanationAndConfidenceFields,
                                                                                                 responseSubmissionSpecificationInstance:responseSubmissionSpecificationInstance]" />
        <g:if test="${shouldPresentExplanationAndConfidenceFields && responseSubmissionSpecificationInstance.studentsProvideExplanation}">
            <g:submitToRemote controller="player" action="submitResponse"
                              update="sequence_${interactionInstance.sequenceId}" class="btn btn-default"
                              value="${message(code: 'player.sequence.interaction.submitResponse')}"
                              before="document.getElementById('explanation_${interactionInstance.id}').textContent = CKEDITOR.instances.explanation_${interactionInstance.id}.getData()"/>
        </g:if>
        <g:else>
            <g:submitToRemote controller="player" action="submitResponse"
                              update="sequence_${interactionInstance.sequenceId}" class="btn btn-default"
                              value="${message(code: 'player.sequence.interaction.submitResponse')}"/>
        </g:else>
    </g:form>
</g:else>