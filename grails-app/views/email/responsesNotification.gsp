<%--
  Created by IntelliJ IDEA.
  User: dylan
  Date: 26/05/15
  Time: 08:59
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.response.notification.page.title")}</title>
</head>

<body>

<p>${message(code: "email.content.hi")} ${user.first_name}</p>

<g:if test="${questionMap != null}">
<p>${message(code: "email.response.notification.message")}</p>
<g:set var="keys" value="${questionMap.keySet()}"/>
<g:set var="keyTab" value="${keys.toArray()}"/>
<g:each in="${keyTab}" var="key">
    <hr/>
    <p>${message(code: "email.response.notification.question")}<br>
        ${message(code: "email.response.notification.context")} ${key.context_name}<br>
        ${message(code: "email.response.notification.tag")} ${key.fragment_tag_name}<br>
        ${message(code: "email.response.notification.content")} ${key.question_content}<br><br>

        ${message(code: "email.response.notification.responses")} <br><br>

    <g:each in="${questionMap.get(key)}" var="response">
        ${message(code: "email.response.notification.sender")} ${response.response_author}<br>
        ${message(code: "email.response.notification.content")} ${response.response_content} <br><br>
    </g:each>
        ${message(code: "email.notification.beforeLink")} <g:createLink absolute="true"
                                     params="[displaysAll: 'on', displaysMyNotes: 'on', contextName: key.context_name, contextId: key.context_id, fragmentTagId: key.fragment_tag]"
                                     controller="notes"/>
    </p>
    <br>
</g:each>
</g:if>
<g:if test="${mentionsList != null}">
    <p>${message(code: "email.mention.notification.message")}</p>

    <g:each in="${mentionsList}" var="mention">
        <hr>
        <p> ${message(code: "email.response.notification.note")}<br>
        ${message(code: "email.response.notification.author")} ${mention.mention_author}<br>
        ${message(code: "email.response.notification.context")} ${mention.context_name}<br>
        ${message(code: "email.response.notification.tag")} ${mention.fragment_tag_name}<br>
        ${message(code: "email.response.notification.content")} ${mention.mention_content}<br>

        ${message(code: "email.notification.beforeLink")} <g:createLink absolute="true"
                                    params="[displaysAll: 'on', contextName: mention.context_name, contextId: mention.context_id, fragmentTagId: mention.fragment_tag]"
                                    controller="notes"/>
    </g:each>
</g:if>
</body>
</html>