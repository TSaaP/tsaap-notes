<g:set var="sequence" value="${interactionInstance.sequence}"/>
<g:render template="/assignment/player/sequence/teacher/dashboard/dashboard-elaastic"
          model="[interactionInstance: interactionInstance, user: user, resultsArePublished: false]"/>