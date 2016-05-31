<%--
  Created by IntelliJ IDEA.
  User: dylan
  Date: 26/05/15
  Time: 08:59
--%>

<%@ page import="org.tsaap.directory.User" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.response.title",locale: new Locale(user.language))}</title>
</head>

<body>

<p>${message(code: "email.hi",locale: new Locale(user.language))} ${user.first_name}</p>

<g:if test="${mentionsList != null}">
    <p>${message(code: "email.mention.notification.message",locale: new Locale(user.language))}</p>
    <hr>
    <g:each in="${mentionsList}" var="mention">

        <p> ${message(code: "email.response.notification.note",locale: new Locale(user.language))}<br>
        ${message(code: "email.response.notification.author",locale: new Locale(user.language))} ${mention.mention_author}<br>
        ${message(code: "email.response.notification.context",locale: new Locale(user.language))} ${mention.context_name}<br>
        ${message(code: "email.response.notification.tag",locale: new Locale(user.language))} ${mention.fragment_tag_name}<br>
        ${message(code: "email.response.notification.content",locale: new Locale(user.language))} ${mention.mention_content}<br>

        ${message(code: "email.notification.beforeLink",locale: new Locale(user.language))} <g:createLink absolute="true"
                                    params="[displaysAll: 'on', contextName: mention.context_name, contextId: mention.context_id, fragmentTagId: mention.fragment_tag]"
                                    controller="notes"/>
        <hr>
    </g:each>
</g:if>
<br>
<a href = "${createLink (absolute:'true', action:'doSettings',controller:'settings', params: [key: key])}">${message(code: "email.manage",locale: new Locale(user.language))} </a>
<br>
<a href="${createLink (absolute:'true', action:'doUnsubscribeMention', controller:'unsubscribeKey', params: [key: key])}">
    ${message(code: "email.unsubscribeMention.label",locale: new Locale(user.language))}
</a>
</body>
</html>