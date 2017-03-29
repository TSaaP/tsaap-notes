<g:set var="displaySubmission" value="${sequenceInstance.statement.hasChoices() ? 'block' : 'none'}"/>
<g:set var="displayConfrontation" value="${sequenceInstance.statement.hasChoices() ? 'none' : 'block'}"/>
<div id="interactionSpec_${sequenceInstance.id}">
  <div style="display: ${displaySubmission};">
    <g:render template="/assignment/player/interactions/teacher/phase_submission_1" model="[sequenceId: sequenceInstance.id]"/>
  </div>
  <div id="phaseConfrontation_${sequenceInstance.id}" style="display: ${displayConfrontation};">
    <g:render template="/assignment/player/interactions/teacher/phase_confrontation_2" model="[sequenceId: sequenceInstance.id]"/>
  </div>
  <div class="checkbox">
    <label>
      <input type="checkbox" value="false" name="asynchronousProcess" id="asynchronousProcess_${sequenceInstance.id}" onclick="switchBtnText(this, ${sequenceInstance.id})"/>
      <g:message code="sequence.interaction.asynchronousProcess"/>
    </label>
  </div>
  <p>
    <g:remoteLink class="btn btn-success" controller="player" action="initializeInteractionsAndStartFirst" id="${sequenceInstance.id}"
                  update="sequence_${sequenceInstance.id}"
                  onSuccess="remoteLinkSuccess(${sequenceInstance.id});"
                  params="{studentsProvideExplanation:\$('#studentsProvideExplanation_${sequenceInstance.id}').val(), studentsProvideConfidenceDegree:\$('#studentsProvideConfidenceDegree_${sequenceInstance.id}').val(), responseToEvaluateCount:\$('#responseToEvaluateCount_${sequenceInstance.id}').val(), asynchronousProcess: \$('#asynchronousProcess_${sequenceInstance.id}').val()}"
    >
      <span class="glyphicon glyphicon-play"></span>
      <span id="synchronous_${sequenceInstance.id}">${message(code: "player.sequence.interaction.start", args: [1])} </span>
      <span id="asynchronous_${sequenceInstance.id}" style="display: none"> ${message(code: "player.sequence.interaction.asynchronous.start")} </span>
    </g:remoteLink>
  </p>
</div>