%{--
  - Copyright 2013 Tsaap Development Group
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%


<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.notification.page.title", locale: new Locale(user.language))}</title>
</head>

<body>

<p>${message(code: "email.hi", locale: new Locale(user.language))} ${user.first_name}</p>


<g:each in="${contextList}" var="context">
    <g:set var="noteCount" value="${context.count_notes}"/>
    <g:if test="${noteCount > 1}">
        <p>
            ${message(code: "email.notification.severalNotes.message1", locale: new Locale(user.language))} ${noteCount} ${message(code: "email.notification.severalNotes.message2", locale: new Locale(user.language))} <strong>${context.context_name}</strong> ${message(code: "email.notification.endMessage", locale: new Locale(user.language))} <br>
            ${message(code: "email.notification.beforeLink", locale: new Locale(user.language))} <g:createLink
                    absolute="true"
                    params="[displaysAll: 'on', contextName: context.context_name, contextId: context.context_id]"
                    controller="notes"/>
        </p>
    </g:if>
    <g:elseif test="${noteCount == 1}">
        <p>
            ${message(code: "email.notification.oneNote.message", locale: new Locale(user.language))} <strong>${context.context_name}<strong>${message(code: "email.notification.endMessage", locale: new Locale(user.language))} <br>
            ${message(code: "email.notification.beforeLink", locale: new Locale(user.language))} <g:createLink
                    absolute="true"
                    params="[displaysAll: 'on', contextName: context.context_name, contextId: context.context_id]"
                    controller="notes"/>
        </p>
    </g:elseif>

    <hr>
</g:each>

<a href="${createLink(absolute: 'true', action: 'doSettings', controller: 'settings', params: [key: key])}">
    ${message(code: "email.manage", locale: new Locale(user.language))}
</a>
<br>
<a href="${createLink(absolute: 'true', action: 'doUnsubscribeDaily', controller: 'unsubscribeKey', params: [key: key])}">
    ${message(code: "email.unsubscribeDaily.label", locale: new Locale(user.language))}
</a>

</body>
</html>