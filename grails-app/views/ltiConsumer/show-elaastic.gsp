%{--
  -
  -  Copyright (C) 2017 Ticetime
  -
  -      This program is free software: you can redistribute it and/or modify
  -      it under the terms of the GNU Affero General Public License as published by
  -      the Free Software Foundation, either version 3 of the License, or
  -      (at your option) any later version.
  -
  -      This program is distributed in the hope that it will be useful,
  -      but WITHOUT ANY WARRANTY; without even the implied warranty of
  -      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -      GNU Affero General Public License for more details.
  -
  -      You should have received a copy of the GNU Affero General Public License
  -      along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -
  --}%

<%@ page import="org.tsaap.lti.LtiConsumer" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui"/>
  <g:set var="entityName" value="${message(code: 'ltiConsumer.label', default: 'LtiConsumer')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<h2 class="ui header">
  <g:message code="default.show.label" args="[entityName]"/>
</h2>


<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>

<table class="ui definition table">
  <tbody>
  <g:if test="${ltiConsumerInstance?.id}">
    <tr>
      <td class="fieldcontain">
        <span id="consumerKey-label" class="property-label"><g:message code="ltiConsumer.consumerKey.label"
                                                                       default="Consumer Key"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="consumerKey-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="id"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.consumerName}">
    <tr>
      <td class="fieldcontain">
        <span id="consumerName-label" class="property-label"><g:message
            code="ltiConsumer.consumerName.label" default="Consumer Name"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="consumerName-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="consumerName"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.secret}">
    <tr>
      <td class="fieldcontain">
        <span id="secret-label" class="property-label"><g:message code="ltiConsumer.secret.label"
                                                                  default="Secret"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="secret-label"><g:fieldValue bean="${ltiConsumerInstance}"
                                                                                  field="secret"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.ltiVersion}">
    <tr>
      <td class="fieldcontain">
        <span id="ltiVersion-label" class="property-label"><g:message code="ltiConsumer.ltiVersion.label"
                                                                      default="Lti Version"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="ltiVersion-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="ltiVersion"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.productName}">
    <tr>
      <td class="fieldcontain">
        <span id="productName-label" class="property-label"><g:message code="ltiConsumer.productName.label"
                                                                       default="Product Name"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="productName-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="productName"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.productVersion}">
    <tr>
      <td class="fieldcontain">
        <span id="productVersion-label" class="property-label"><g:message
            code="ltiConsumer.productVersion.label" default="Product Version"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="productVersion-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="productVersion"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.productGuid}">
    <tr>
      <td class="fieldcontain">
        <span id="productGuid-label" class="property-label"><g:message code="ltiConsumer.productGuid.label"
                                                                       default="Product Guid"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="productGuid-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="productGuid"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.cssPath}">
    <tr>
      <td class="fieldcontain">
        <span id="cssPath-label" class="property-label"><g:message code="ltiConsumer.cssPath.label"
                                                                   default="Css Path"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="cssPath-label"><g:fieldValue bean="${ltiConsumerInstance}"
                                                                                   field="cssPath"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.isProtected}">
    <tr>
      <td class="fieldcontain">
        <span id="isProtected-label" class="property-label"><g:message code="ltiConsumer.isProtected.label"
                                                                       default="Is Protected"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="isProtected-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="isProtected"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.isEnabled}">
    <tr>
      <td class="fieldcontain">
        <span id="isEnabled-label" class="property-label"><g:message code="ltiConsumer.isEnabled.label"
                                                                     default="Is Enabled"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="isEnabled-label"><g:fieldValue
            bean="${ltiConsumerInstance}" field="isEnabled"/></span>

      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.enableFrom}">
    <tr>
      <td class="fieldcontain">
        <span id="enableFrom-label" class="property-label"><g:message code="ltiConsumer.enableFrom.label"
                                                                      default="Enable From"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="enableFrom-label"><g:formatDate
            date="${ltiConsumerInstance?.enableFrom}"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.enableUntil}">
    <tr>
      <td class="fieldcontain">
        <span id="enableUntil-label" class="property-label"><g:message code="ltiConsumer.enableUntil.label"
                                                                       default="Enable Until"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="enableUntil-label"><g:formatDate
            date="${ltiConsumerInstance?.enableUntil}"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.dateCreated}">
    <tr>
      <td class="fieldcontain">
        <span id="dateCreated-label" class="property-label"><g:message code="ltiConsumer.dateCreated.label"
                                                                       default="Date Created"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate
            date="${ltiConsumerInstance?.dateCreated}"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.lastUpdated}">
    <tr>
      <td class="fieldcontain">
        <span id="lastUpdated-label" class="property-label"><g:message code="ltiConsumer.lastUpdated.label"
                                                                       default="Last Updated"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate
            date="${ltiConsumerInstance?.lastUpdated}"/></span>
      </td>
    </tr>
  </g:if>

  <g:if test="${ltiConsumerInstance?.lastAccess}">
    <tr>
      <td class="fieldcontain">
        <span id="lastAccess-label" class="property-label"><g:message code="ltiConsumer.lastAccess.label"
                                                                      default="Last Access"/></span>
      </td>
      <td>
        <span class="property-value" aria-labelledby="lastAccess-label"><g:formatDate
            date="${ltiConsumerInstance?.lastAccess}"/></span>
      </td>
    </tr>
  </g:if>
  </tbody>
</table>

<g:form url="[resource: ltiConsumerInstance, action: 'delete']" method="DELETE" class="ui form">
  <g:link class="ui primary button" action="edit" resource="${ltiConsumerInstance}"><g:message
      code="default.button.edit.label" default="Edit"/></g:link>
  <g:actionSubmit class="ui negative button" action="delete"
                  value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                  onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>

</g:form>

<div class="ui hidden divider"></div>
</body>
</html>
