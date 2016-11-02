<g:if test="${interactionInstance.hasResponseForUser(user, attempt)}">
    <div class="alert alert-warning"
         role="alert">${message(code: "player.sequence.interaction.afterResponseSubmission.message", args: [interactionInstance.sequence.activeInteraction.rank])} <g:remoteLink
            controller="player" action="updateSequenceDisplay" id="${interactionInstance.sequenceId}" title="Refresh"
            update="sequence_${interactionInstance.sequenceId}"><span class="glyphicon glyphicon-refresh"
                                                                      aria-hidden="true"></span></g:remoteLink></div>
</g:if>
<g:else>
    <g:set var="responseSubmissionSpecificationInstance" value="${interactionInstance.interactionSpecification}"/>
    <g:set var="itemCount" value="${responseSubmissionSpecificationInstance.itemCount}"/>
    <g:set var="isMultipleChoice" value="${responseSubmissionSpecificationInstance.isMultipleChoice() ?: false}"/>
    <g:set var="hasChoices" value="${responseSubmissionSpecificationInstance.hasChoices()}"/>
    <g:set var="firstAttemptResponse" value="${interactionInstance.responseForUser(user)}"/>
    <g:form>
        <g:hiddenField name="id" value="${interactionInstance.id}"/>
        <g:hiddenField name="attempt" value="${attempt}"/>
        <g:if test="${hasChoices}">
            <div class="checkbox ${isMultipleChoice ? '' : 'hidden'}" id="multiple_choice_${interactionInstance.id}">
                <g:each in="${1..itemCount}" var="checkBoxElet" status="i">
                    <g:set var="choiceIsExpected"
                           value="${responseSubmissionSpecificationInstance?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
                    <label class="checkbox-inline" style="margin-right: 20px">
                        <input type="checkbox" name="choiceList"
                               value="${i + 1}" ${firstAttemptResponse?.choiceList()?.contains(i + 1) ? 'checked' : ''}> ${i + 1}
                    </label>
                </g:each>
            </div>

            <div class="radio ${isMultipleChoice ? 'hidden' : ''}" id="exclusive_choice_${interactionInstance.id}">
                <g:each in="${1..itemCount}" var="radioBoxElet" status="i">
                    <g:set var="choiceIsExpected"
                           value="${responseSubmissionSpecificationInstance?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
                    <label class="radio-inline" style="margin-right: 20px">
                        <input type="radio" name="exclusiveChoice"
                               value="${i + 1}" ${firstAttemptResponse?.choiceList()?.contains(i + 1) ? 'checked' : ''}> ${i + 1}
                    </label>
                </g:each>
            </div>
        </g:if>

        <g:if test="${attempt == 1}">
            <g:if test="${responseSubmissionSpecificationInstance.studentsProvideExplanation}">
                <div class="form-group fieldcontain">
                    <label for="explanation_${interactionInstance.id}">
                        <g:message code="player.sequence.interaction.explanation.label" default="Title"/>
                    </label>
                    <ckeditor:editor name="explanation" id="explanation_${interactionInstance.id}">
                        ${firstAttemptResponse?.explanation}
                    </ckeditor:editor>
                </div>
            </g:if>

            <g:if test="${responseSubmissionSpecificationInstance.studentsProvideConfidenceDegree}">
                <div class="form-group fieldcontain">
                    <label for="confidenceDegree_${interactionInstance.id}">
                        <g:message code="player.sequence.interaction.confidenceDegree.label"/>
                    </label>
                    <g:select class="form-control" name="confidenceDegree"
                              id="confidenceDegree_${interactionInstance.id}"
                              from="${org.tsaap.assignments.ConfidenceDegreeEnum.values()}"
                              optionKey="integerValue"
                              optionValue="${{
                                  message(code: 'player.sequence.interaction.confidenceDegree.' + it.name)
                              }}"
                              value="${firstAttemptResponse?.confidenceDegree}">
                    </g:select>
                </div>
            </g:if>
        </g:if>
        <g:if test="${attempt == 1 && responseSubmissionSpecificationInstance.studentsProvideExplanation}">
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