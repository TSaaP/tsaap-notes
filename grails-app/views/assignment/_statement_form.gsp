

<div class="form-group fieldcontain ${hasErrors(bean: statementInstance, field: 'title', 'error')} ">
    <label for="title">
        <g:message code="sequence.statement.title.label" default="Title"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="title" required="" value="${statementInstance?.title}" class="form-control"/>
</div>

<div class="form-group">
    <label for="myFile"><g:message code="default.fileInput.label" default="Attachment"/></label>
    <div id="attachment">
        <g:set var="attachment" value="${statementInstance.attachment}"/>
        <g:if test="${attachment != null}">
            <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
            <g:remoteLink controller="assignment" action="removeAttachement"
                          id="${statementInstance.id}"
                          update="attachment">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
            </g:remoteLink>
        </g:if>
        <g:else>
            <input type="file" name="myFile" id="myFile"
                   title="Image: gif, jpeg and png only"/>
        </g:else>
    </div>
</div>

<div class="form-group fieldcontain ${hasErrors(bean: statementInstance, field: 'content', 'error')} ">
    <label for="title">
        <g:message code="sequence.statement.content.label" default="Title"/>
        <span class="required-indicator">*</span>
    </label>
    <ckeditor:editor name="content" id="content">
        ${statementInstance?.content}
    </ckeditor:editor>
</div>









