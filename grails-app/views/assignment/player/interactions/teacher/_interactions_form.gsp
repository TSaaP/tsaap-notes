<g:set var="displaySubmission" value="${sequenceInstance.statement.hasChoices() ? 'block' : 'none'}"/>
<g:set var="displayConfrontation" value="${sequenceInstance.statement.hasChoices() ? 'none' : 'block'}"/>
<div id="interactionSpec_${sequenceInstance.id}">
    <div>
        <div class="panel panel-default" style="margin-top: 10px">
            <div class="panel-heading"><g:message code="sequence.interaction.executionContext"/></div>

            <div class="panel-body">
                <div class="radio">
                    <label class="radio-inline">
                        <input type="radio" name="executionContext_${sequenceInstance.id}" value="FaceToFace" checked/> <g:message
                            code="sequence.interaction.executionContext.faceToFace"/>
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="executionContext_${sequenceInstance.id}" value="Distance"> <g:message
                            code="sequence.interaction.executionContext.distance"/>
                    </label>
                    <label class="radio-inline">
                    <input type="radio" name="executionContext_${sequenceInstance.id}" value="Blended"> <g:message
                            code="sequence.interaction.executionContext.blended"/>
                </label>
                </div>
            </div>

        </div>
    </div>

    <div style="display: ${displaySubmission};" id="configuration_${sequenceInstance.id}">
        <g:render template="/assignment/player/interactions/teacher/interactions_configuration"
                  model="[sequenceId: sequenceInstance.id]"/>
    </div>

    <p>
        <g:remoteLink class="btn btn-success" controller="player" action="initializeInteractionsAndStartFirst"
                      id="${sequenceInstance.id}"
                      update="sequence_${sequenceInstance.id}"
                      onSuccess="remoteLinkSuccess(${sequenceInstance.id});"
                      params="{studentsProvideExplanation:\$('#studentsProvideExplanation_${sequenceInstance.id}').val(), studentsProvideConfidenceDegree:\$('#studentsProvideConfidenceDegree_${sequenceInstance.id}').val(), responseToEvaluateCount:\$('#responseToEvaluateCount_${sequenceInstance.id}').val(), asynchronousProcess: \$('#asynchronousProcess_${sequenceInstance.id}').val()}">
            <span class="glyphicon glyphicon-play"></span>
            <span id="synchronous_${sequenceInstance.id}">${message(code: "player.sequence.interaction.start", args: [1])}</span>
            <span id="asynchronous_${sequenceInstance.id}"
                  style="display: none">${message(code: "player.sequence.interaction.asynchronous.start")}</span>
        </g:remoteLink>
    </p>
</div>