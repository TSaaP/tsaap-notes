<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 24/05/16
  Time: 11:41
--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>${message(code: "springSecurity.login.page.title")}</title>
    <meta name='layout' content='home'/>
    <r:require module="tsaap_ui_signin"/>
</head>

<body>
<div class="container">
    <div class="jumbotron">
        <h1>${message(code: "settings.unsubscribedDaily.title")}</h1>
        <p>${message(code: "settings.unsubscribedDaily.message")}</p>
    </div>
</div>

</body>
</html>