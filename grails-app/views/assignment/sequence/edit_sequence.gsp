<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main">
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
  <g:set var="entityName" value="${message(code: 'sequence.label', default: 'Question')}"/>
  <title>
    <g:message code="sequence.label" />
  </title>
</head>

<body>

<div id="edit-sequence" class="container" role="main">
  <g:set var="assignmentInstance" value="${sequenceInstance.assignment}"/>
  <g:set var="statementInstance" value="${sequenceInstance.statement}"/>
  <ol class="breadcrumb">
    <li><g:link class="list" action="index" controller="assignment"><g:message code="assignment.list.label"
                                                                               args="[entityName]"/></g:link></li>
    <li><g:link class="list" action="show" controller="assignment"
                id="${assignmentInstance.id}">${message(code: "assignment.label")} ${assignmentInstance?.title}</g:link></li>
    <li class="active">${message(code: "sequence.edition.label")}</li>
  </ol>

  <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${sequenceInstance}">
    <div class="alert alert-danger">
      <ul class="errors" role="alert">
        <g:eachError bean="${sequenceInstance}" var="error">
          <li<g:if
                 test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
              error="${error}"/></li>
        </g:eachError>
      </ul>
    </div>
  </g:hasErrors>
  <g:form controller="sequence" action="updateSequence" method="post" enctype="multipart/form-data">
    <g:hiddenField name="sequence_instance_id" value="${sequenceInstance?.id}"/>
    <g:render template="/assignment/sequence/statement_form" bean="${statementInstance}"
              model="[assignmentInstance: assignmentInstance, sequenceInstance: sequenceInstance]"/>
    <g:render template="/assignment/sequence/statement_question_type_form"
              model="[sequenceInstance: sequenceInstance]"/>
    <g:render template="/assignment/sequence/statement_explanations_form" model="[sequenceInstance: sequenceInstance]"/>
    <button type="submit"
            class="btn btn-default">
      ${message(code: 'default.button.update.label', default: 'Update')}
    </button>
    </fieldset>
  </g:form>
</div>
</body>
</html>
