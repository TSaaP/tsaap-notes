<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:set var="displayedResultInteraction" value="${sequence.responseSubmissionInteraction}"/>
<g:set var="spec" value="${displayedResultInteraction.interactionSpecification}"/>

<div class="alert alert-warning" role="alert">
  ${message(code:"elaastic.anonymous.sequence.read.show.message")}
</div>
<g:if test="${sequence.hasExplanations()}">
  <g:if test="${spec.hasChoices()}">
    <g:set var="responses" value="${sequence.findAllGoodResponses()}"/>
  </g:if>
  <g:else>
    <g:set var="responses" value="${sequence.findAllOpenResponses()}"/>
  </g:else>
  <g:render template="/assignment/player/ExplanationList" model="[responses: responses, sequence: sequence]"/>
</g:if>