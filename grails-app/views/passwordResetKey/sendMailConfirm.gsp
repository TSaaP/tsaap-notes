<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 16/06/16
  Time: 15:28
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
        <g:if test='${flash.message}'>

        <div class="alert alert-info" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            ${flash.message}
        </div>
        </g:if>
        <g:link class="btn btn-primary" controller="passwordResetKey"
                action="goIndex">${message(code: "password.redirect.home.page")}</g:link>

    </div>
</div>

</body>
</html>