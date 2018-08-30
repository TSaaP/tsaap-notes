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
  <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>

<content tag="specificMenu">
  <g:link class="item"
          action="create"
          data-tooltip="${message(code: 'default.new.label', args: [entityName])}"
          data-position="right center"
          data-inverted=""
          resource="${assignmentInstance}">
    <i class="yellow plus square outline icon"></i>
  </g:link>
</content>

<h2 class="ui header">
  <i class="settings icon"></i>

  <div class="content">
    <g:message code="default.list.label" args="[entityName]"/>
  </div>
</h2>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>


<table class="ui very basic striped table">
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
    <tr>

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

<div class="ui right aligned basic segment">
  <elaastic:paginate class="ui tiny pagination menu"
                     action="index"
                     prev="&laquo;"
                     next="&raquo;"
                     total="${ltiConsumerInstanceCount ?: 0}"/>
</div>

</body>
</html>
