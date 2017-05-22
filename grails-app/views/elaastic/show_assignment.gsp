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
<h4>${assignmentInstance.title}
  <g:if test="${scheduleInstance?.startDate}">
    <small>
      <span id="startDate-label" class="property-label"><g:message code="schedule.startdate.label"
                                                                   default="Start Date"/></span>
      <span class="property-value" aria-labelledby="startDate-label"><g:formatDate
          date="${scheduleInstance?.startDate}"/>.</span>
      <g:if test="${scheduleInstance?.endDate}">
        <span id="endDate-label" class="property-label"><g:message code="schedule.enddate.label"
                                                                   default="End Date"/></span>
        <span class="property-value" aria-labelledby="endDate-label"><g:formatDate
            date="${scheduleInstance?.endDate}"/>.</span>

      </g:if>
    </small>
  </g:if>
</h4>
<div class="well well-sm"><g:message code="player.assignment.registeredUserCount"/> <span
    id="registered_user_count">${assignmentInstance.registeredUserCount()}</span> <g:remoteLink
    controller="player" action="updateRegisteredUserCount" id="${assignmentInstance.id}" title="Refresh"
    update="registered_user_count"><span class="glyphicon glyphicon-refresh"
                                         aria-hidden="true"></span></g:remoteLink>
  <br/>
  <small><span title="${g.message(code: 'player.assignment.registration.link.tooltip')}"><g:createLink
      controller="player" action="register" absolute="true"
      params="[globalId: assignmentInstance.globalId]"/></span></small>
</div>
<ul class="list-group">
  <g:each in="${assignmentInstance.sequences}" status="i" var="sequenceInstance">
    <li class="list-group-item" id="sequence_${sequenceInstance.id}">
      <g:render template="/assignment/player/sequence/show"
                model="[userRole: userRole, sequenceInstance: sequenceInstance, user: user]"/>
    </li>
  </g:each>
</ul>

</body>
</html>