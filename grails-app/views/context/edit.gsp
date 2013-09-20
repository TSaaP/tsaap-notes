<%@ page import="org.tsaap.notes.Context" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main">
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
  <g:set var="entityName"
         value="${message(code: 'context.label', default: 'Context')}"/>
  <title>Tsaap Notes - <g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<div class="container context-nav" role="navigation">
  <ol class="breadcrumb">
    <li><g:link class="list" action="index"><g:message code="default.list.label"
                                                       args="[entityName]"/></g:link></li>
    <li class="active">Edit context ${context?.contextName}</li>
  </ol>
</div>

<div id="edit-context" class="container" role="main">
  <g:if test="${flash.message}">
    <div class="alert alert-info" role="status">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${context}">
    <div class="alert alert-danger">
    <ul class="errors" role="alert">
      <g:eachError bean="${context}" var="error">
        <li
          <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                error="${error}"/></li>
      </g:eachError>
    </ul>
    </div>
  </g:hasErrors>
  <g:form url="[resource: context, action: 'update']" method="PUT">
    <g:hiddenField name="version" value="${context?.version}"/>
    <fieldset class="form">
      <g:render template="form" model="[context:context]"/>
    </fieldset>
    <fieldset class="buttons">
      <g:actionSubmit class="btn btn-default" action="update"
                      value="${message(code: 'default.button.update.label', default: 'Update')}"/>
    </fieldset>
  </g:form>
</div>
</body>
</html>
