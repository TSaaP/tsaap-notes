<div id="show-assignment" class="container" role="main">
  <h4>${assignmentInstance.title}</h4>
  <ul class="list-group">
    <g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
      <li class="list-group-item" id="sequence_${sequenceInstance.id}">
        <g:render template="/elaastic/anonymous/sequence/show"
                  model="[sequenceInstance: sequenceInstance]"/>
      </li>
    </g:each>
  </ul>
</div>