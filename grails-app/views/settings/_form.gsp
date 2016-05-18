<%@ page import="org.tsaap.directory.Settings" %>



<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'dailyNotifications', 'error')} ">
	<label for="dailyNotifications">
		<g:message code="settings.dailyNotifications.label" default="Daily Notifications" />
		
	</label>
	<g:checkBox name="dailyNotifications" value="${settingsInstance?.dailyNotifications}" />
</div>

<div class="fieldcontain ${hasErrors(bean: settingsInstance, field: 'mentionNotifications', 'error')} ">
	<label for="mentionNotifications">
		<g:message code="settings.mentionNotifications.label" default="Mention Notifications" />
		
	</label>
	<g:checkBox name="mentionNotifications" value="${settingsInstance?.mentionNotifications}" />
</div>

