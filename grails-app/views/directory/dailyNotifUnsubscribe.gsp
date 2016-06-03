<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 24/05/16
  Time: 11:41
--%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>${message(code: "settings.unsubscribedMention.page.title")}</title>
    <meta name='layout' content='anonymous'/>
    <r:require module="tsaap_ui_signin"/>
</head>

<body>
<div class="container">
    <div class="body-content">
                <div class="alert alert-info" role="alert">
                    <p>${message(code: "settings.unsubscribedDaily.message")}</p>
                </div>
    </div>
</div>

</body>
</html>