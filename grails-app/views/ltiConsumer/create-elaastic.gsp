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

<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui"/>
  <g:set var="entityName" value="${message(code: 'ltiConsumer.label', default: 'LtiConsumer')}"/>
  <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<h2 class="ui header">
  <i class="add icon"></i>

  <div class="content">
    <g:message code="default.create.label" args="[entityName]"/>
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


<g:form url="[resource: ltiConsumerInstance, action: 'save']" class="ui form">
  <g:render template="form-elaastic"/>


  <g:submitButton name="create"
                  class="ui primary button"
                  value="${message(code: 'default.button.create.label', default: 'Create')}"/>

  <div class="ui hidden divider"></div>
</g:form>

</body>
</html>
