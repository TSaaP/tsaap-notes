<div class="alert alert-warning"
     role="alert">${message(code: "player.sequence.interaction.read.teacher.show.message", args: [interactionInstance.rank])}</div>
<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:if test="${sequence.hasExplanations()}">
    <g:set var="responses" value="${sequence.findAllGoodResponses()}"/>
    <g:set var="badResponses" value="${sequence.findAllBadResponses()}"/>
    <g:render template="/assignment/player/ExplanationList"
              model="[responses: responses, sequence: sequence, badResponses: badResponses]"/>
</g:if>