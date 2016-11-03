<%@ page import="org.tsaap.assignments.Schedule; org.tsaap.assignments.interactions.ResponseSubmissionSpecification; org.tsaap.assignments.interactions.ChoiceInteractionType" %>
<g:set var="responseSubmissionSpecificationInstance" value="${sequenceInstance?.responseSubmissionSpecification ?: new ResponseSubmissionSpecification()}"/>
<div class="panel panel-default">
    <div class="panel-heading"><g:message code="sequence.interaction.phase"/> 1</div>

    <div class="panel-body">
        <div class="checkbox">
            <label>
                <input type="checkbox" name="hasChoices" ${responseSubmissionSpecificationInstance.choiceInteractionType ? 'checked' : ''}> <g:message code="sequence.interaction.studentsSelect"/>
            <g:select name="choiceInteractionType" id="choiceInteractionType"
                      from="${ChoiceInteractionType.values()*.name()}"
                      value="${responseSubmissionSpecificationInstance?.choiceInteractionType}"/> <g:message
                    code="sequence.interaction.choicesIn"/>
            <g:select name="itemCount" id="itemCount" from="${2..10}"
                      value="${responseSubmissionSpecificationInstance?.itemCount}"/>
            <g:message code="sequence.interaction.items"/>
            </label>
        </div>
        <g:set var="itemCount" value="${responseSubmissionSpecificationInstance?.itemCount ?: 2}"/>
        <g:set var="isMultipleChoice" value="${responseSubmissionSpecificationInstance?.isMultipleChoice() ?: false}"/>
        <div class="checkbox text-center ${isMultipleChoice ? '' : 'hidden'}" id="multiple_choice">
            <g:each in="${1..itemCount}" var="checkBoxElet" status="i">
                <g:set var="choiceIsExpected"
                       value="${responseSubmissionSpecificationInstance?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
                <label class="checkbox-inline" style="margin-right: 20px">
                    <input type="checkbox" name="expectedChoiceList"
                           value="${i + 1}" ${choiceIsExpected ? 'checked' : ''}> ${i + 1}
                </label>
            </g:each>
        </div>

        <div class="radio text-center ${isMultipleChoice ? 'hidden' : ''}" id="exclusive_choice" title="${message(code:'sequence.interaction.expectedChoice.hasToBeSelected')}">
            <g:each in="${1..itemCount}" var="radioBoxElet" status="i">
                <g:set var="choiceIsExpected"
                       value="${responseSubmissionSpecificationInstance?.expectedChoiceListContainsChoiceWithIndex(i + 1)}"/>
                <label class="radio-inline" style="margin-right: 20px">
                    <input type="radio" name="exclusiveChoice"
                           value="${i + 1}" ${choiceIsExpected ? 'checked' : ''}> ${i + 1}
                </label>
            </g:each>
        </div>

        <div class="checkbox">
            <label>
                <input type="checkbox"
                       name="studentsProvideExplanation" ${responseSubmissionSpecificationInstance?.studentsProvideExplanation ? 'checked' : ''}
                       value="true"/> <g:message code="sequence.interaction.studentsProvideAtextualExplanation"/>
            </label>
        </div>

        <div class="checkbox">
            <label>
                <input type="checkbox"
                       name="studentsProvideConfidenceDegree" ${responseSubmissionSpecificationInstance?.studentsProvideConfidenceDegree ? 'checked' : ''}
                       value="true"/> <g:message code="sequence.interaction.studentsProvideConfidenceDegree"/>
            </label>
        </div>

        <div id="schedulePhase1" class="hidden">
            <g:render template="/assignment/sequence/phase_schedule_form"
                      model="[scheduleInstance: sequenceInstance?.responseSubmissionInteraction?.schedule, indexSchedule: 1, defaultDate:defaultDate]"/>
        </div>
    </div>
</div>

<r:script>
    manageChoices()
    managePhase2Display();
    manageScheduleDisplay();
    manageHasChoices();

    function manageScheduleDisplay() {
        var displaySchedule = $("input:checkbox[name='displaySchedule']");
        if (displaySchedule.is(':checked')) {
            $('#schedulePhase1').removeClass('hidden');
            $('#schedulePhase2').removeClass('hidden');
            $('#schedulePhase3').removeClass('hidden');
        }
        displaySchedule.change(function () {
            $('#schedulePhase1').toggleClass('hidden');
            $('#schedulePhase2').toggleClass('hidden');
            $('#schedulePhase3').toggleClass('hidden');
        })

    }

    function managePhase2Display() {
        var chBoxStstudentsProvideExplanation = $("input:checkbox[name='studentsProvideExplanation']");
        chBoxStstudentsProvideExplanation.change(function () {
            var chBoxStudentsProvideConfidenceDegree = $("input:checkbox[name='studentsProvideConfidenceDegree']");
            if (chBoxStstudentsProvideExplanation.is(':checked')) {
                $('#phase_2').removeClass("hidden");
                chBoxStudentsProvideConfidenceDegree.prop('checked', true);
                chBoxStudentsProvideConfidenceDegree.prop('disabled', true);
            } else {
                $('#phase_2').addClass("hidden");
                chBoxStudentsProvideConfidenceDegree.prop('disabled', false);
            }
        });
    };

    function manageHasChoices() {
        var chkBoxHasChoices = $("input:checkbox[name='hasChoices']");
        chkBoxHasChoices.change(function() {
            var hasChoices = chkBoxHasChoices.is(':checked');
            var chBoxStstudentsProvideExplanation = $("input:checkbox[name='studentsProvideExplanation']");
            var selectMultipleVsExclusive = $("#choiceInteractionType");
            if (!hasChoices) {
                chBoxStstudentsProvideExplanation.prop('checked', true);
                chBoxStstudentsProvideExplanation.trigger("change");
                chBoxStstudentsProvideExplanation.prop('disabled', true);
                selectMultipleVsExclusive.prop('disabled', true);
                $('#exclusive_choice').addClass('hidden');
                $('#multiple_choice').addClass('hidden');
                $("#itemCount").prop('disabled',true);
            } else {
                chBoxStstudentsProvideExplanation.prop('disabled', false);
                selectMultipleVsExclusive.prop('disabled', false);
                $("#itemCount").prop('disabled',false);
                if ($('#multiple_choice').hasClass('hidden') && $('#exclusive_choice').hasClass('hidden')) {
                    $("#itemCount").trigger("change");
                }
            }
        });
        chkBoxHasChoices.trigger("change");
    }

    function manageChoices() {
        $("#choiceInteractionType, #itemCount").change(function () {
            var isMultiple = ($("#choiceInteractionType").val() == "MULTIPLE");
            if (isMultiple) {
                $('#multiple_choice').removeClass('hidden');
                $('#exclusive_choice').addClass('hidden');
                $("#multiple_choice").empty();
                for (var i = 1; i <= $("#itemCount").val(); i++) {
                    var chckBox = $('<label class="checkbox-inline" style="margin-right: 20px"> <input type="checkbox" name="expectedChoiceList" value="' + i + '"> ' + i + ' </label>');
                    $("#multiple_choice").append(chckBox);
                    if (i == 1) {
                        chckBox.find('input').prop( "checked", true );
                    }
                }
            } else {
                $('#exclusive_choice').removeClass('hidden');
                $('#multiple_choice').addClass('hidden');
                $('#exclusive_choice').empty();
                for (var j = 1; j <= $("#itemCount").val(); j++) {
                    var currentRadioBox = $('<label class="radio-inline" style="margin-right: 20px"> <input type="radio" name="exclusiveChoice" value="' + j + '"> ' + j + ' </label>');
                    $("#exclusive_choice").append(currentRadioBox);
                    if (j == 1) {
                        currentRadioBox.find('input').prop( "checked", true );
                    }
                }
            }
        })
    }
</r:script>