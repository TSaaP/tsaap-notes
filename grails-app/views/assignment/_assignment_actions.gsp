<div class="btn-group btn-group-sm" role="group">
    <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
        <span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
        <span class="caret"></span>
    </button>
    <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
        <li><g:link action="edit" resource="${assignmentInstance}">
        <g:message code="assignment.action.propertiesEdition.label"/></g:link></li>
        <li><g:link action="duplicate" controller="assignment" id="${assignmentInstance.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><g:message code="assignment.action.duplicate.label"/></g:link></li>
        <li role="separator" class="divider"></li>
        <li><g:link action="delete" controller="assignment" id="${assignmentInstance.id}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><g:message code="assignment.action.delete.label"/></g:link></li>
    </ul>
</div>