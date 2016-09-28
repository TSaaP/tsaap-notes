<g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}"
          model="[statementInstance: sequenceInstance.statement]"/>
<g:set var="activeInteraction" value="${sequenceInstance.activeInteraction}"/>
<g:render template="/assignment/player/${activeInteraction.interactionType}/${userRole}/${activeInteraction.state}"
          model="[interactionInstance: activeInteraction, user:user]"/>
