<g:render template="/assignment/player/statement/learner/${sequenceInstance.state}"
          model="[statementInstance: sequenceInstance.statement]"/>
<g:set var="rank" value="${sequenceInstance.activeInteraction.rank}"/>
<g:set var="i" value="${0}"/>
<g:while test="${i < rank}">
  <g:set var="currentInteraction" value="${sequenceInstance.interactions[i++]}"/>
  <g:if test="${currentInteraction.enabled}">
    <g:render template="/elaastic/anonymous/${currentInteraction.interactionType}/${currentInteraction.state}"
              model="[interactionInstance: currentInteraction, attempt:1]"/>
  </g:if>
</g:while>