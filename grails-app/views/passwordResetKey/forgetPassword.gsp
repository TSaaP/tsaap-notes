<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 13/06/16
  Time: 15:24
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <meta name='layout' content='home'/>
    <r:require module="tsaap_ui_signin"/>
</head>

<body>
<div class="container">
    <div class="col-xs-12" style="height:70px;"></div>
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3" >
            <div class="panel panel-primary" style="background-color:transparent">
                <div class="panel-heading">${message(code: "login.lost.password.title")}</div>
                <div class="panel-body">
                    <g:form class="form-horizontal" action="doReset" controller="login">

                        <%--<g:if test='${flash.message}'>
                            <div class='alert'>${flash.message}</div>
                        </g:if>--%>
                        <div class="input-group">
                            <span class="input-group-addon" id="sizing-addon2">@</span>
                            <input type="text" class="form-control" placeholder="Email"
                                   aria-describedby="sizing-addon2">
                        </div>
                        <br>
                        <button class="btn btn-primary"
                                type="submit"
                                id="submit">${message(code: "login.lost.password.button")}</button>
                    </g:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>