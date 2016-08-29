<%@ page import="org.tsaap.assignments.Assignment" %>

<div class="form-group fieldcontain ${hasErrors(bean: assignmentInstance, field: 'title', 'error')} ">
    <label for="title">
        <g:message code="assignment.title.label" default="Title"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textField name="title" required="" value="${assignmentInstance?.title}" class="form-control"/>
</div>




