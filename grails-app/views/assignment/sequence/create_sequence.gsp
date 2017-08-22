<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'sequence.label', default: 'Question')}"/>
    <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div id="edit-sequence" class="container" role="main">

    <ol class="breadcrumb">
        <li>
            <g:link class="list" action="index" controller="assignment">
                <g:message code="assignment.list.label" args="[entityName]"/>
            </g:link>
        </li>
        <li>
            <g:link class="list" action="show" controller="assignment" id="${assignmentInstance.id}">
                ${message(code: "assignment.label")} ${assignmentInstance?.title}
            </g:link>
        </li>
        <li class="active">
            ${message(code: "sequence.creation.label")}
        </li>
    </ol>
    <g:set var="statementInstance" value="${sequenceInstance?.statement}"/>
    <g:if test="${flash.message}">
        <div class="message" role="status">
          ${flash.message}
        </div>
    </g:if>
    <g:hasErrors bean="${sequenceInstance}">
        <div class="alert alert-danger">
            <ul class="errors" role="alert">
                <g:eachError bean="${sequenceInstance}" var="error">
                    <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                            error="${error}"/></li>
                </g:eachError>
            </ul>
        </div>
    </g:hasErrors>
    <g:form controller="sequence" action="saveSequence" method="POST" enctype="multipart/form-data">
        <g:hiddenField name="assignment_instance_id" value="${assignmentInstance?.id}"/>
        <fieldset class="form">
            <g:render template="/assignment/sequence/statement_form" bean="${statementInstance}" model="[assignmentInstance:assignmentInstance]"/>
            <g:render template="/assignment/sequence/statement_question_type_form"/>
            <g:render template="/assignment/sequence/statement_explanations_form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:actionSubmit class="btn btn-info" action="saveSequence"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
