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
  <title><g:message code="default.edit.label" args="[entityName]"/></title>
</head>

<body>

<content tag="specificMenu">
  <a href="#" class="item"
     onclick="$('#lti-consumer-form').submit();"
     data-tooltip="${message(code: 'common.save')}"
     data-position="right center"
     data-inverted="">
    <i class="yellow save icon"></i>
  </a>
</content>

<h2 class="ui header">
  <i class="edit icon"></i>

  <div class="content">
    <g:message code="default.edit.label" args="[entityName]"/>
  </div>
</h2>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>


<g:hasErrors bean="${ltiConsumerInstance}">
  <div class="ui error message">
    <ul class="list">
      <g:eachError bean="${ltiConsumerInstance}" var="error">
        <li><g:message error="${error}"/></li>
      </g:eachError>
    </ul>
  </div>
</g:hasErrors>

<g:form url="[resource: ltiConsumerInstance, action: 'update']"
        name="lti-consumer-form"
        method="PUT"
        class="ui form">
  <g:hiddenField name="version" value="${ltiConsumerInstance?.version}"/>
  <g:render template="form-elaastic"/>


  <g:actionSubmit class="ui primary button"
                  action="update"
                  value="${message(code: 'default.button.update.label', default: 'Update')}"/>

</g:form>

</body>
</html>
