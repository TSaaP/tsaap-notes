<div class="form-group fieldcontain ${hasErrors(bean: statementInstance, field: 'title', 'error')} ">
    <label for="title">
        <g:message code="sequence.statement.title.label" default="Title"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="title" required="" value="${statementInstance?.title}" class="form-control"/>
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









