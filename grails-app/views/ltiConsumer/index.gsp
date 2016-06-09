<%@ page import="org.tsaap.lti.LtiConsumer" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'ltiConsumer.label', default: 'LtiConsumer')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div class="container">
    <a href="#list-ltiConsumer" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                      default="Skip to content&hellip;"/></a>

    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </ul>
    </div>
</div>

<div class="container">
    <div id="list-ltiConsumer" class="content scaffold-list" role="main">
        <h1><g:message code="default.list.label" args="[entityName]"/></h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <table class="table table-hover">
            <thead>
            <tr>

                <g:sortableColumn property="consumerName"
                                  title="${message(code: 'ltiConsumer.consumerName.label', default: 'Consumer Name')}"/>

                <g:sortableColumn property="secret"
                                  title="${message(code: 'ltiConsumer.secret.label', default: 'Secret')}"/>

                <g:sortableColumn property="ltiVersion"
                                  title="${message(code: 'ltiConsumer.ltiVersion.label', default: 'Lti Version')}"/>

                <g:sortableColumn property="productName"
                                  title="${message(code: 'ltiConsumer.productName.label', default: 'Product Name')}"/>

                <g:sortableColumn property="productVersion"
                                  title="${message(code: 'ltiConsumer.productVersion.label', default: 'Product Version')}"/>

                <g:sortableColumn property="productGuid"
                                  title="${message(code: 'ltiConsumer.productGuid.label', default: 'Product Guid')}"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${ltiConsumerInstanceList}" status="i" var="ltiConsumerInstance">
                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                    <td><g:link action="show"
                                id="${ltiConsumerInstance.id}">${fieldValue(bean: ltiConsumerInstance, field: "consumerName")}</g:link></td>

                    <td>${fieldValue(bean: ltiConsumerInstance, field: "secret")}</td>

                    <td>${fieldValue(bean: ltiConsumerInstance, field: "ltiVersion")}</td>

                    <td>${fieldValue(bean: ltiConsumerInstance, field: "productName")}</td>

                    <td>${fieldValue(bean: ltiConsumerInstance, field: "productVersion")}</td>

                    <td>${fieldValue(bean: ltiConsumerInstance, field: "productGuid")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>

        <div class="pagination">
            <g:paginate total="${ltiConsumerInstanceCount ?: 0}"/>
        </div>
    </div>
</div>
</body>
</html>
