<%@ page import="org.tsaap.notes.Context" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main">
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>

  <g:set var="entityName"
         value="${message(code: 'context.label', default: 'Scope')}"/>
  <title>Tsaap Notes - <g:message code="default.show.label"
                                  args="[entityName]"/></title>
</head>

<body>

<div class="container context-nav" role="navigation">
  <ol class="breadcrumb">
    <li><g:link class="list" action="index"><g:message code="default.list.label"
                                                       args="[entityName]"/></g:link></li>
    <li class="active">Show scope "${context?.contextName}"</li>
  </ol>
</div>

<div id="show-context" class="container" role="main">
  <g:if test="${flash.message}">
    <div class="alert alert-info" role="status">${flash.message}</div>
  </g:if>

  <g:if test="${context}">
    <table class="table table-bordered">
      <colgroup>
        <col class="col-lg-1">
        <col class="col-lg-7">
      </colgroup>
      <tbody>
      <tr>
        <td>
          <g:message code="context.contextName.label" default="Scope Name"/>
        </td>
        <td>${context.contextName}</td>
      </tr>
      <tr>
        <td>
          <g:message code="context.url.label" default="Url"/>
        </td>
        <td><a href="${context.url}" target="_blank">${context.url}</a></td>
      </tr>
      <tr>
        <td>
          <g:message code="context.descriptionAsNote.label"
                     default="Description"/>
        </td>
        <td>${context.descriptionAsNote}</td>
      </tr>
      <tr>
        <td>
          <g:message code="context.owner.label" default="Owner"/>
        </td>
        <td>@${context.owner.username} <g:if
                test="${context.ownerIsTeacher}">(as teacher)</g:if></td>
      </tr>
      <tr>
        <td>
          <g:message code="context.dateCreated.label" default="Created"/>
        </td>
        <td><g:formatDate
                date="${context.dateCreated}"/></td>
      </tr>
      <tr>
        <td>
          <g:message code="context.lastUpdated.label" default="Updated"/>
        </td>
        <td><g:formatDate
                date="${context.lastUpdated}"/></td>
      </tr>
      <tr>
        <td>
          The notes on this scope
        </td>
        <td><g:link controller="notes"
                    params="[displaysAll: 'on', contextName: context?.contextName, contextId: context.id]"><g:createLink
                  absolute="true" controller="notes"
                  params="[displaysAll: 'on', contextName: context?.contextName, contextId: context.id]"/></g:link></td>
      </tr>
      </tbody>
    </table>


    <g:if test="${context.owner == user}">
    <g:form url="[resource: context, action: 'delete']" method="DELETE">
      <fieldset class="buttons">

        <g:link class="btn btn-primary" action="edit"
                resource="${context}"><g:message
                code="default.button.edit.label" default="Edit"/></g:link>
      <g:if test="${!context.hasNotes()}">
          <g:actionSubmit class="btn btn-default" action="delete"
                          value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                          onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
      </g:if>
      </fieldset>
    </g:form>
    </g:if>
  </g:if>
</div>
</body>
</html>
