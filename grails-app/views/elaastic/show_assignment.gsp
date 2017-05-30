<%--
  Created by IntelliJ IDEA.
  User: qsaieb
  Date: 22/05/2017
  Time: 15:10
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="elaastic">
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
  <g:set var="entityName" value="${message(code: 'player.assignment.label', default: 'Play Assignment')}"/>
  <title><g:message code="assignment.label" args="[entityName]"/></title>
</head>

<body>
  <g:set var="userRole" value="${user == null ? 'anonymous': 'authenticate'}"/>
  <g:render template="/elaastic/$userRole/assignment"/>
</body>
</html>