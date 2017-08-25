<g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}"
          model="[statementInstance: sequenceInstance.statement]"/>
<g:set var="rank" value="${sequenceInstance.activeInteraction.rank}"/>
<g:set var="i" value="${0}"/>
<g:while test="${i < rank}">
    <g:set var="currentInteraction" value="${sequenceInstance.interactions[i++]}"/>
    <g:render
            template="/assignment/player/${currentInteraction.interactionType}/${userRole}/${currentInteraction.stateForTeacher(user)}"
            model="[interactionInstance: currentInteraction, user: user, attempt: 1]"/>
</g:while>


