<g:set var="sequenceInstanceList" value="${sequenceInstance.assignment.sequences}"/>
<div class="btn-group btn-group-sm" role="group">
    <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="true">
        <span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
        <g:if test="${sequenceInstance != sequenceInstanceList[0]}">
            <li><g:link action="upSequence" id="${sequenceInstance.id}" controller="sequence">
                <g:message code="sequence.action.upSequence.label"/></g:link></li>
        </g:if>
        <g:if test="${sequenceInstance != sequenceInstanceList.last()}">
            <li><g:link action="downSequence" id="${sequenceInstance.id}" controller="sequence">
                <g:message code="sequence.action.downSequence.label"/></g:link></li>
        </g:if>
        <li role="separator" class="divider"></li>
        <li><g:link action="deleteSequence" controller="sequence" id="${sequenceInstance.id}"
                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><g:message
                    code="assignment.action.delete.label"/></g:link></li>
    </ul>
</div>