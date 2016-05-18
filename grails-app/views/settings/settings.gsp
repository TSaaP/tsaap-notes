<%--
  Created by IntelliJ IDEA.
  User: akacimi
  Date: 18/05/16
  Time: 08:40
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>
<div class="container context-nav">

    <ol class="breadcrumb">
        <li class="active">${message(code: "settings.title")}</li>
    </ol>

    <div class="body-content">

    <!-- Example row of columns -->
        <div class="row">

            <div class="col-lg-6">
                <g:form controller="settings" action="doUpdate">
                    <fieldset>
                        <div class="checkbox cheked">
                            <label>
                                <input type="checkbox" checked="checked" value="dailyNotification">
                                <span data-toggle="tooltip" data-html="true" title="${message(code: "settings.checkbox.dailyNotification.message")}" data-placement="top" >
                                    ${message(code: "settings.checkbox.dailyNotification.label")}
                                </span>
                            </label>
                        </div>
                        <div class="checkbox"  >
                            <label>
                                <input type="checkbox" checked="checked" value="mentionNotification">
                                <span data-toggle="tooltip" data-html="true" title="${message(code: "settings.checkbox.mentionNotification.message")}" data-placement="top">
                                    ${message(code: "settings.checkbox.mentionNotification.label")}
                                </span>
                            </label>
                        </div>


                        <button type="submit"
                                class="btn btn-primary">${message(code: "useraccount.save.button")} &raquo;</button><br/>

                    </fieldset>
                </g:form>

            </div>

        </div>

    </div>
    <hr>
</div> <!-- /container -->
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>

</body>
</html>