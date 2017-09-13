<g:set var="choiceSpecification" value="${interactionInstance.sequence.statement.getChoiceSpecificationObject()}"/>
<g:set var="isMultipleChoice" value="${interactionInstance.sequence.statement.isMultipleChoice() ?: false}"/>
<g:set var="hasChoices" value="${interactionInstance.sequence.statement.hasChoices()}"/>
<g:set var="firstAttemptResponse" value="${interactionInstance.responseForUser(user)}"/>

<g:hiddenField name="attempt" value="${attempt}"/>
<g:if test="${hasChoices}">
    <g:set var="itemCount" value="${choiceSpecification.itemCount}"/>
    <div class="checkbox ${isMultipleChoice ? '' : 'hidden'}" id="multiple_choice_${interactionInstance.id}">
        <g:each in="${1..itemCount}" var="checkBoxElet" status="i">
            <g:set var="choiceIsExpected"
                   value="${choiceSpecification?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
            <label class="checkbox-inline" style="margin-right: 20px">
                <input type="checkbox" name="choiceList"
                       value="${i + 1}" ${firstAttemptResponse?.choiceList()?.contains(i + 1) ? 'checked' : ''}> ${i + 1}
            </label>
        </g:each>
    </div>

    <div class="radio ${isMultipleChoice ? 'hidden' : ''}" id="exclusive_choice_${interactionInstance.id}">
        <g:each in="${1..itemCount}" var="radioBoxElet" status="i">
            <g:set var="choiceIsExpected"
                   value="${choiceSpecification?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
            <label class="radio-inline" style="margin-right: 20px">
                <input type="radio" name="exclusiveChoice"
                       value="${i + 1}" ${firstAttemptResponse?.choiceList()?.contains(i + 1) ? 'checked' : ''}> ${i + 1}
            </label>
        </g:each>
    </div>
</g:if>
<g:if test="${shouldPresentExplanationAndConfidenceFields}">
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
