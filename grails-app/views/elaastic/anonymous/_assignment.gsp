<div id="show-assignment" class="container" role="main">
  <h4>${assignmentInstance.title}</h4>
  <ul class="list-group">
    <g:set var="userRole" value="${user == assignmentInstance.owner ? 'teacher' : 'learner'}"/>
    <g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
      <li class="list-group-item" id="sequence_${sequenceInstance.id}">
        <g:render template="/elaastic/anonymous/sequence/show"
                  model="[userRole: userRole, sequenceInstance: sequenceInstance, user: user]"/>
      </li>
    </g:each>
  </ul>
</div>