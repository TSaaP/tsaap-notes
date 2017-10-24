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
  <g:set var="entityName" value="${message(code: 'assignment.label', default: 'Assignment')}"/>
  <title><g:message code="assignment.creation.label" args="[entityName]"/></title>
</head>

<body>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>

<g:hasErrors bean="${assignmentInstance}">
  <div class="ui error message">
    <ul class="list">
      <g:eachError bean="${assignmentInstance}" var="error">
        <li><g:message error="${error}"/></li>
      </g:eachError>
    </ul>
  </div>
</g:hasErrors>


<div class="ui segment">
  <div class="ui top attached label"><g:message code="assignment.create.label" /></div>
  <g:form class="ui form" controller="assignment" action="save">
    <g:render template="assignment_form-elaastic" bean="${assignmentInstance}"/>

    <g:submitButton name="create" class="ui primary button"
                    value="${message(code: 'default.button.create.label', default: 'Create')}"/>

    <g:link controller="assignment" class="ui button">
      <g:message code="common.cancel" />
    </g:link>
  </g:form>
</div>

<r:script>
  $(document)
      .ready(function () {

    $('.message .close')
        .on('click', function () {
      $(this)
          .closest('.message')
          .transition('fade')
      ;
    });

  });

</r:script>

</body>
</html>
