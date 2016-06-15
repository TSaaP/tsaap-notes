<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 14/06/16
  Time: 15:04
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.passwordReset.title", locale: new Locale(user.language))}</title>
</head>

<body>
<p>${message(code: "email.hi", locale: new Locale(user.language))} ${user.first_name}</p>
<p>${message(code: "email.password.reset.corp", locale: new Locale(user.language))}</p>

    <a href="${createLink(absolute: 'true', action: 'doPasswordReset', controller: 'passwordResetKey', params: [passwordResetKey: passwordResetKey])}">
        ${message(code: "email.passwordReset.title", locale: new Locale(user.language))} </a>

</body>
</html>