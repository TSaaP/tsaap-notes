<g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}"
          model="[statementInstance: sequenceInstance.statement]"/>
<g:if test="${sequenceInstance.activeInteraction}">
  <g:set var="rank" value="${sequenceInstance.activeInteraction.rank}"/>
  <g:set var="i" value="${0}"/>
  <g:while test="${i < rank}">
    <g:set var="currentInteraction" value="${sequenceInstance.interactions[i++]}"/>
    <g:if test="${currentInteraction.enabled}">
      <g:render template="/assignment/player/${currentInteraction.interactionType}/${userRole}/${currentInteraction.stateForUser(user)}" model="[interactionInstance: currentInteraction, user: user, attempt:1]"/>
    </g:if>
  </g:while>
</g:if>

