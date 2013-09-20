<%@ page import="org.tsaap.notes.Context" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main">
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
  <g:set var="entityName"
         value="${message(code: 'context.label', default: 'Context')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div class="container context-nav">
  <g:link class="btn btn-primary btn-sm pull-right" action="create"><g:message
          code="default.new.label" args="[entityName]"/></g:link>
</div>

<div id="list-context" class="container">

  <g:if test="${flash.message}">
    <div class="message" role="status">${flash.message}</div>
  </g:if>
  <table class="table table-striped table-hover">
    <thead>
    <tr>

      <g:sortableColumn property="contextName"
                        title="${message(code: 'context.contextName.label', default: 'Context Name')}"/>


      <g:sortableColumn property="descriptionAsNote"
                        title="${message(code: 'context.descriptionAsNote.label', default: 'Description As Note')}"/>



      <th><g:message code="context.owner.label" default="Owner"/></th>

    </tr>
    </thead>
    <tbody>
    <g:each in="${contextList}" status="i" var="context">
      <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

        <td><g:link action="show"
                    id="${context.id}">${fieldValue(bean: context, field: "contextName")}</g:link><br/>
          ${fieldValue(bean: context, field: "url")}
        </td>


        <td>${fieldValue(bean: context, field: "descriptionAsNote")}</td>


        <td>${fieldValue(bean: context, field: "owner")}</td>

      </tr>
    </g:each>
    </tbody>
  </table>
</div>

<div class="container">
  <div class="pagination">
    <g:paginate total="${contextCount ?: 0}"/>
  </div>
</div>

<r:script>
  $(".nav li").removeClass('active');
  $("#mainLinkContexts").addClass('active');
</r:script>
</body>
</html>
