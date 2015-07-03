<%--
  Created by IntelliJ IDEA.
  User: dorian
  Date: 01/07/15
  Time: 09:45
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Tsaap-Notes</title>
    <meta name="layout" content="home">
    <r:require module="tsaap_ui_home"/>
</head>

<body>
<div class="container">

    <div class="alert">
        <p>${message(code: exception.message)}</p>
    </div>
    <g:if env="development">
        <p>
            <a href="#"
               onclick="showException()">${message(code: "error.development.errorDetail.link")}</a>
        </p>

        <div id="exception" style="display:none">
            <g:renderException exception="${exception}"/>
        </div>
    </g:if>
    <r:script>
        function showException() {
            $('#exception').show()
        }

    </r:script>
</body>
</html>