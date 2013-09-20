<%@ page import="org.tsaap.notes.Context" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main">
  <g:set var="entityName"
         value="${message(code: 'context.label', default: 'Context')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<a href="#show-context" class="skip" tabindex="-1"><g:message
        code="default.link.skip.label" default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
  <ul>
    <li><a class="home" href="${createLink(uri: '/')}"><g:message
            code="default.home.label"/></a></li>
    <li><g:link class="list" action="index"><g:message code="default.list.label"
                                                       args="[entityName]"/></g:link></li>
    <li><g:link class="create" action="create"><g:message
            code="default.new.label" args="[entityName]"/></g:link></li>
  </ul>
</div>

<div id="show-context" class="content scaffold-show" role="main">
  <h1><g:message code="default.show.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
  </g:if>
  <ol class="property-list context">

    <g:if test="${context?.contextName}">
      <li class="fieldcontain">
        <span id="contextName-label" class="property-label"><g:message
                code="context.contextName.label" default="Context Name"/></span>

        <span class="property-value"
              aria-labelledby="contextName-label"><g:fieldValue
                bean="${context}" field="contextName"/></span>

      </li>
    </g:if>

    <g:if test="${context?.url}">
      <li class="fieldcontain">
        <span id="url-label" class="property-label"><g:message
                code="context.url.label" default="Url"/></span>

        <span class="property-value" aria-labelledby="url-label"><g:fieldValue
                bean="${context}" field="url"/></span>

      </li>
    </g:if>

    <g:if test="${context?.descriptionAsNote}">
      <li class="fieldcontain">
        <span id="descriptionAsNote-label" class="property-label"><g:message
                code="context.descriptionAsNote.label"
                default="Description As Note"/></span>

        <span class="property-value"
              aria-labelledby="descriptionAsNote-label"><g:fieldValue
                bean="${context}" field="descriptionAsNote"/></span>

      </li>
    </g:if>

    <g:if test="${context?.dateCreated}">
      <li class="fieldcontain">
        <span id="dateCreated-label" class="property-label"><g:message
                code="context.dateCreated.label" default="Date Created"/></span>

        <span class="property-value"
              aria-labelledby="dateCreated-label"><g:formatDate
                date="${context?.dateCreated}"/></span>

      </li>
    </g:if>

    <g:if test="${context?.lastUpdated}">
      <li class="fieldcontain">
        <span id="lastUpdated-label" class="property-label"><g:message
                code="context.lastUpdated.label" default="Last Updated"/></span>

        <span class="property-value"
              aria-labelledby="lastUpdated-label"><g:formatDate
                date="${context?.lastUpdated}"/></span>

      </li>
    </g:if>

    <g:if test="${context?.owner}">
      <li class="fieldcontain">
        <span id="owner-label" class="property-label"><g:message
                code="context.owner.label" default="Owner"/></span>

        <span class="property-value" aria-labelledby="owner-label"><g:link
                controller="user" action="show"
                id="${context?.owner?.id}">${context?.owner?.encodeAsHTML()}</g:link></span>

      </li>
    </g:if>

    <g:if test="${context?.ownerIsTeacher}">
      <li class="fieldcontain">
        <span id="ownerIsTeacher-label" class="property-label"><g:message
                code="context.ownerIsTeacher.label"
                default="Owner Is Teacher"/></span>

        <span class="property-value"
              aria-labelledby="ownerIsTeacher-label"><g:formatBoolean
                boolean="${context?.ownerIsTeacher}"/></span>

      </li>
    </g:if>

  </ol>
  <g:form url="[resource: context, action: 'delete']" method="DELETE">
    <fieldset class="buttons">
      <g:link class="edit" action="edit" resource="${context}"><g:message
              code="default.button.edit.label" default="Edit"/></g:link>
      <g:actionSubmit class="delete" action="delete"
                      value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                      onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
    </fieldset>
  </g:form>
</div>
</body>
</html>
