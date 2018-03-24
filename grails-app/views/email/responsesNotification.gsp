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

<%--
  Created by IntelliJ IDEA.
  User: dylan
  Date: 26/05/15
  Time: 08:59
--%>

<%@ page import="org.tsaap.directory.User" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.response.title", locale: new Locale(user.language))}</title>
</head>

<body>

<p>${message(code: "email.hi", locale: new Locale(user.language))} ${user.first_name}</p>

<g:if test="${mentionsList != null}">
    <p>${message(code: "email.mention.notification.message", locale: new Locale(user.language))}</p>
    <hr>
    <g:each in="${mentionsList}" var="mention">

        <p>${message(code: "email.response.notification.note", locale: new Locale(user.language))}<br>
            ${message(code: "email.response.notification.author", locale: new Locale(user.language))} ${mention.mention_author}<br>
            ${message(code: "email.response.notification.context", locale: new Locale(user.language))} ${mention.context_name}<br>
            ${message(code: "email.response.notification.tag", locale: new Locale(user.language))} ${mention.fragment_tag_name}<br>
            ${message(code: "email.response.notification.content", locale: new Locale(user.language))} ${mention.mention_content}<br>

            ${message(code: "email.notification.beforeLink", locale: new Locale(user.language))} <g:createLink
                    absolute="true"
                    params="[displaysAll: 'on', contextName: mention.context_name, contextId: mention.context_id, fragmentTagId: mention.fragment_tag]"
                    controller="notes"/>
        <hr>
    </g:each>
</g:if>
<br>
<a href="${createLink(absolute: 'true', action: 'doUnsubscribeMention', controller: 'unsubscribeKey', params: [key: key])}">
    ${message(code: "email.unsubscribeMention.label", locale: new Locale(user.language))}
</a>
</body>
</html>