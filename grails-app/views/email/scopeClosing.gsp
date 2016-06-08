<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 07/06/16
  Time: 16:01
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>${message(code: "email.closingContext.page.title", locale: new Locale(user.language))}</title>
</head>
<body>

<p>${message(code: "email.hi", locale: new Locale(user.language))} ${user.first_name}</p>

<p>${message(code: 'email.closingContext.message', args: [context_name], locale: new Locale(user.language))}</p>
</body>
</html>