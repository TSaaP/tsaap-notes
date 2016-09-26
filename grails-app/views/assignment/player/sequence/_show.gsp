<li class="list-group-item">
    <g:render template="/assignment/player/statement/${userRole}/${sequenceInstance.state}" bean="${sequenceInstance.statement}"/>
    <g:render template="/assignment/player/${sequenceInstance.activeInteraction.interactionType}/${userRole}/${sequenceInstance.state}" bean="${sequenceInstance.activeInteraction}"/>
</li>