<%@ page import="org.tsaap.lti.LtiConsumer" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main">
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <g:set var="entityName" value="${message(code: 'ltiConsumer.label', default: 'LtiConsumer')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div class="container">
    <a href="#show-ltiConsumer" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                      default="Skip to content&hellip;"/></a>

    <div class="nav" role="navigation">
        <ul>
            <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li><g:link class="list" action="index"><g:message code="default.list.label"
                                                               args="[entityName]"/></g:link></li>
            <li><g:link class="create" action="create"><g:message code="default.new.label"
                                                                  args="[entityName]"/></g:link></li>
        </ul>
    </div>
</div>

<div class="container">
    <div id="show-ltiConsumer" class="content scaffold-show" role="main">
        <h1><g:message code="default.show.label" args="[entityName]"/></h1>
        <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
        </g:if>
        <dl class="dl-horizontal">
            <g:if test="${ltiConsumerInstance?.id}">
                <dt class="fieldcontain">
                    <span id="consumerKey-label" class="property-label"><g:message code="ltiConsumer.consumerKey.label"
                                                                                   default="Consumer Key"/></span>
                </dt>
                <dd>
                    <span class="property-value" aria-labelledby="consumerKey-label"><g:fieldValue
                            bean="${ltiConsumerInstance}" field="id"/></span>
                </dd>
            </g:if>

            <g:if test="${ltiConsumerInstance?.consumerName}">
                <dt class="fieldcontain">
                    <span id="consumerName-label" class="property-label"><g:message
                            code="ltiConsumer.consumerName.label" default="Consumer Name"/></span>
                </dt>
                <dd>
                    <span class="property-value" aria-labelledby="consumerName-label"><g:fieldValue
                            bean="${ltiConsumerInstance}" field="consumerName"/></span>
                </dd>
            </g:if>

            <g:if test="${ltiConsumerInstance?.secret}">
                <dt class="fieldcontain">
                    <span id="secret-label" class="property-label"><g:message code="ltiConsumer.secret.label"
                                                                              default="Secret"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="secret-label"><g:fieldValue bean="${ltiConsumerInstance}"
                                                                                          field="secret"/></span>
            </dd>
            </g:if>

            <g:if test="${ltiConsumerInstance?.ltiVersion}">
                <dt class="fieldcontain">
                    <span id="ltiVersion-label" class="property-label"><g:message code="ltiConsumer.ltiVersion.label"
                                                                                  default="Lti Version"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="ltiVersion-label"><g:fieldValue
                        bean="${ltiConsumerInstance}" field="ltiVersion"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.productName}">
                <dt class="fieldcontain">
                    <span id="productName-label" class="property-label"><g:message code="ltiConsumer.productName.label"
                                                                                   default="Product Name"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="productName-label"><g:fieldValue
                        bean="${ltiConsumerInstance}" field="productName"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.productVersion}">
                <dt class="fieldcontain">
                    <span id="productVersion-label" class="property-label"><g:message
                            code="ltiConsumer.productVersion.label" default="Product Version"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="productVersion-label"><g:fieldValue
                        bean="${ltiConsumerInstance}" field="productVersion"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.productGuid}">
                <dt class="fieldcontain">
                    <span id="productGuid-label" class="property-label"><g:message code="ltiConsumer.productGuid.label"
                                                                                   default="Product Guid"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="productGuid-label"><g:fieldValue
                        bean="${ltiConsumerInstance}" field="productGuid"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.cssPath}">
                <dt class="fieldcontain">
                    <span id="cssPath-label" class="property-label"><g:message code="ltiConsumer.cssPath.label"
                                                                               default="Css Path"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="cssPath-label"><g:fieldValue bean="${ltiConsumerInstance}"
                                                                                           field="cssPath"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.isProtected}">
                <dt class="fieldcontain">
                    <span id="isProtected-label" class="property-label"><g:message code="ltiConsumer.isProtected.label"
                                                                                   default="Is Protected"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="isProtected-label"><g:fieldValue
                        bean="${ltiConsumerInstance}" field="isProtected"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.isEnabled}">
                <dt class="fieldcontain">
                    <span id="isEnabled-label" class="property-label"><g:message code="ltiConsumer.isEnabled.label"
                                                                                 default="Is Enabled"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="isEnabled-label"><g:fieldValue
                        bean="${ltiConsumerInstance}" field="isEnabled"/></span>

            </dd>
            </g:if>

            <g:if test="${ltiConsumerInstance?.enableFrom}">
                <dt class="fieldcontain">
                    <span id="enableFrom-label" class="property-label"><g:message code="ltiConsumer.enableFrom.label"
                                                                                  default="Enable From"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="enableFrom-label"><g:formatDate
                        date="${ltiConsumerInstance?.enableFrom}"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.enableUntil}">
                <dt class="fieldcontain">
                    <span id="enableUntil-label" class="property-label"><g:message code="ltiConsumer.enableUntil.label"
                                                                                   default="Enable Until"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="enableUntil-label"><g:formatDate
                        date="${ltiConsumerInstance?.enableUntil}"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.dateCreated}">
                <dt class="fieldcontain">
                    <span id="dateCreated-label" class="property-label"><g:message code="ltiConsumer.dateCreated.label"
                                                                                   default="Date Created"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate
                        date="${ltiConsumerInstance?.dateCreated}"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.lastUpdated}">
                <dt class="fieldcontain">
                    <span id="lastUpdated-label" class="property-label"><g:message code="ltiConsumer.lastUpdated.label"
                                                                                   default="Last Updated"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate
                        date="${ltiConsumerInstance?.lastUpdated}"/></span>
            </dd>

            </g:if>

            <g:if test="${ltiConsumerInstance?.lastAccess}">
                <dt class="fieldcontain">
                    <span id="lastAccess-label" class="property-label"><g:message code="ltiConsumer.lastAccess.label"
                                                                                  default="Last Access"/></span>
                </dt><dd>
                <span class="property-value" aria-labelledby="lastAccess-label"><g:formatDate
                        date="${ltiConsumerInstance?.lastAccess}"/></span>
            </dd>

            </g:if>

        </dl>
        <g:form url="[resource: ltiConsumerInstance, action: 'delete']" method="DELETE">
            <fieldset class="buttons">
                <g:link class="btn btn-primary" action="edit" resource="${ltiConsumerInstance}"><g:message
                        code="default.button.edit.label" default="Edit"/></g:link>
                <g:actionSubmit class="btn btn-danger" action="delete"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
            </fieldset>
        </g:form>
    </div>
</div>
</body>
</html>
