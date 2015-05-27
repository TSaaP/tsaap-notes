<%--
  Created by IntelliJ IDEA.
  User: dylan
  Date: 26/05/15
  Time: 08:59
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Response notification Tsaap-Notes</title>
</head>

<body>

<p>Hi ${user.firstName}</p>

<p>There is some responses to your questions:</p>
<g:set var="keys" value="${questionMap.keySet()}"/>
<g:set var="keyTab" value="${keys.toArray()}"/>
<g:each in="${keyTab}" var="key">
    <p>Your question: <br>
    Context: ${key.context_name}<br>
    ${key.question_content}<br>

    Responses: <br><br>

    <g:each in="${questionMap.get($key)}" var="response">
        From: ${response.response_author}<br>
        Message : ${response.response_content} <br><br>
    </g:each>
        Come and see : <g:createLink absolute="true"
                                     params="[displaysAll: 'on', displaysMyNotes: 'on', contextName: key.context_name, contextId: key.context_id, fragmentTagId: key.fragment_tag]"
                                     controller="notes"/>
    </p>
    <br>
</g:each>
</body>
</html>