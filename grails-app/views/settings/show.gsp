
<%@ page import="org.tsaap.directory.Settings" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'settings.label', default: 'Settings')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-settings" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-settings" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list settings">
			
				<g:if test="${settingsInstance?.dailyNotifications}">
				<li class="fieldcontain">
					<span id="dailyNotifications-label" class="property-label"><g:message code="settings.dailyNotifications.label" default="Daily Notifications" /></span>
					
						<span class="property-value" aria-labelledby="dailyNotifications-label"><g:formatBoolean boolean="${settingsInstance?.dailyNotifications}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${settingsInstance?.mentionNotifications}">
				<li class="fieldcontain">
					<span id="mentionNotifications-label" class="property-label"><g:message code="settings.mentionNotifications.label" default="Mention Notifications" /></span>
					
						<span class="property-value" aria-labelledby="mentionNotifications-label"><g:formatBoolean boolean="${settingsInstance?.mentionNotifications}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:settingsInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${settingsInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
