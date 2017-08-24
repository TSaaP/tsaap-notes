<g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}"
          model="[statementInstance: sequenceInstance.statement]"/>
<g:set var="questionType" value="${sequenceInstance.statement.questionType.name()}"/>
<div id="interactionSpec_${sequenceInstance.id}">
    <form>
        <input type="hidden" name="id" value="${sequenceInstance.id}">
        <div>
            <div class="panel panel-default" style="margin-top: 10px">
                <div class="panel-heading"><g:message code="sequence.interaction.executionContext"/></div>

                <div class="panel-body">
                    <div class="radio">
                        <label class="radio-inline">
                            <input type="radio" name="executionContext" id="executionContext_${sequenceInstance.id}_${questionType}"
                                   value="FaceToFace" checked/> <g:message
                                code="sequence.interaction.executionContext.faceToFace"/>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="executionContext" id="executionContext_${sequenceInstance.id}_${questionType}"
                                   value="Distance"> <g:message
                                code="sequence.interaction.executionContext.distance"/>
                        </label>
                        <label class="radio-inline">
                            <input type="radio" name="executionContext" id="executionContext_${sequenceInstance.id}_${questionType}"
                                   value="Blended"> <g:message
                                code="sequence.interaction.executionContext.blended"/>
                        </label>
                    </div>
                </div>

            </div>
        </div>

        <div id="configuration_${sequenceInstance.id}">
            <g:render template="/assignment/player/sequence/teacher/interactions_configuration"
                      model="[sequenceId: sequenceInstance.id, questionType: questionType]"/>
        </div>

        <div>
            <g:submitToRemote class="btn btn-success"
                              url="[action: 'initializeInteractionsAndStartFirst', controller: 'player']"
                              update="sequence_${sequenceInstance.id}"
                              value="${message(code: "player.sequence.interaction.start", args: [1])}"/>
        </div>
    </form>
</div>
