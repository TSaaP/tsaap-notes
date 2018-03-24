%{--
  - Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
  -
  -     This program is free software: you can redistribute it and/or modify
  -     it under the terms of the GNU Affero General Public License as published by
  -     the Free Software Foundation, either version 3 of the License, or
  -     (at your option) any later version.
  -
  -     This program is distributed in the hope that it will be useful,
  -     but WITHOUT ANY WARRANTY; without even the implied warranty of
  -     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -     GNU Affero General Public License for more details.
  -
  -     You should have received a copy of the GNU Affero General Public License
  -     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

<br>
<a href="${createLink(absolute: 'true', action: 'doUnsubscribeDaily', controller: 'unsubscribeKey', params: [key: key])}">
    ${message(code: "email.unsubscribeDaily.label", locale: new Locale(user.language))}
</a>

</body>
</html>