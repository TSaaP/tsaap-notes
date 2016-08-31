<%@ page import="org.tsaap.assignments.interactions.ResponseSubmissionSpecification; org.tsaap.assignments.interactions.ChoiceInteractionType" %>
<div class="panel panel-default">
    <div class="panel-heading"><g:message code="sequence.interaction.phase" /> 1</div>

    <div class="panel-body">
        <div class="checkbox">
            <label>
                <input type="checkbox" checked disabled> <g:message code="sequence.interaction.studentsSelect" />
                <g:select name="choiceInteractionType" id="choiceInteractionType" from="${ChoiceInteractionType.values()*.name()}" value="${responseSubmissionSpecificationInstance?.choiceInteractionType}"/> <g:message code="sequence.interaction.choicesIn" />
                <g:select name="itemCount" id="itemCount" from="${2..10}" value="${responseSubmissionSpecificationInstance?.itemCount}"/>
                <g:message code="sequence.interaction.items" />
            </label>
        </div>

        <div class="checkbox text-center hidden" id="multiple_choice">
        </div>

        <div class="radio text-center" id="exclusive_choice">
            <label class="radio-inline">
                <input type="radio" name="expectedChoiceList" value="1"> 1
            </label>
            <label class="radio-inline">
                <input type="radio" name="expectedChoiceList" value="2"> 2
            </label>
        </div>

        <div class="checkbox">
            <label>
                <g:checkBox name="studentsProvideExplanation" checked="${responseSubmissionSpecificationInstance?.studentsProvideExplanation}" value="true"/> <g:message code="sequence.interaction.studentsProvideAtextualExplanation" />
            </label>
        </div>

        <div class="checkbox">
            <label>
                <g:checkBox name="studentsProvideConfidenceDegree" checked="${responseSubmissionSpecificationInstance?.studentsProvideConfidenceDegree}" value="true"/> <g:message code="sequence.interaction.studentsProvideConfidenceDegree" />
            </label>
        </div>


    </div>
</div>

<r:script>
    $(document).ready(function(){

        manageChoices()
        managePhase2Display();

        function managePhase2Display() {
            var chBoxStstudentsProvideExplanation = $("input:checkbox[name='studentsProvideExplanation']")
            chBoxStstudentsProvideExplanation.change(function() {
                $('#phase_2').toggleClass("hidden");
                var chBoxStudentsProvideConfidenceDegree = $("input:checkbox[name='studentsProvideConfidenceDegree']");
                var hiddenStudentsProvideConfidenceDegree = $("input:hidden[name='_studentsProvideConfidenceDegree']");
                if(chBoxStstudentsProvideExplanation.is(':checked')) {
                    chBoxStudentsProvideConfidenceDegree.prop('checked',true);
                    chBoxStudentsProvideConfidenceDegree.prop('disabled',true);
                    hiddenStudentsProvideConfidenceDegree.val(true)
                } else {
                    chBoxStudentsProvideConfidenceDegree.prop('disabled',false);
                }
            });
        };

        function manageChoices() {
            $("#choiceInteractionType, #itemCount").change(function() {
                var isMultiple = ($("#choiceInteractionType").val() == "MULTIPLE");
                if (isMultiple) {
                    $('#multiple_choice').removeClass('hidden');
                    $('#exclusive_choice').addClass('hidden');
                    $("#multiple_choice").empty();
                    for (var i=1 ; i <= $("#itemCount").val(); i++) {
                        var chckBox = $('<label class="checkbox-inline"> <input type="checkbox" name="expectedChoiceList" value="'+i+'"> '+i+' </label>');
                        $("#multiple_choice").append(chckBox);
                    }
                } else {
                    $('#exclusive_choice').removeClass('hidden');
                    $('#multiple_choice').addClass('hidden');
                    $('#exclusive_choice').empty();
                    for (var j=1 ; j <= $("#itemCount").val(); j++) {
                        var radioBox = $('<label class="radio-inline"> <input type="radio" name="expectedChoiceList" value="' + j + '"> ' + j + ' </label>');
                        $("#exclusive_choice").append(radioBox);
                    }
                }
            })
        }

    });
</r:script>