<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui,vue_js,underscore_js,ckeditor_vue_js"/>
  <g:set var="entityName" value="${message(code: 'sequence.label', default: 'Question')}"/>
  <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>

<div id="edit-sequence">

  <g:form name="sequenceForm"
          class="ui form"
          controller="sequence"
          action="saveSequence"
          method="post"
          enctype="multipart/form-data">

    <g:hiddenField name="assignment_instance_id" value="${assignmentInstance?.id}"/>

    <content tag="specificMenu">
      <a href="#" class="item"
         onclick="$('#sequenceForm').submit();"
         data-tooltip="${message(code: 'common.save')}"
         data-position="right center"
         data-inverted="">
        <i class="yellow save icon"></i>
      </a>

      <a href="#"
         class="item"
         onclick="$('#myFile').click();"
         data-tooltip="${message(code: 'question.attachment.select')}"
         data-position="right center"
         data-inverted="">
        <i class="yellow attach icon"></i>
      </a>
    </content>

    <g:if test="${flash.message}">
      <div class="ui visible success message" role="status">
        <div class="header">${raw(flash.message)}</div>
      </div>
    </g:if>

    <g:hasErrors bean="${sequenceInstance}">
      <div class="ui visible error message">
        <div class="header">
          Veuillez corriger les erreurs suivantes pour pouvoir créer cette question :
        </div>
        <ul class="list">
          <g:eachError bean="${sequenceInstance}" var="error">
            <li <g:if
                    test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                error="${error}"/></li>
          </g:eachError>
        </ul>
      </div>
    </g:hasErrors>

    <h2 class="ui block top attached header">

      <i class="book icon"></i>

      <div class="content">
        ${assignmentInstance?.title}
        <g:link action="show"
                controller="assignment"
                id="${assignmentInstance.id}"
                class="ui small compact button"
                style="margin-left: 2em;
        vertical-align: text-bottom">

          <i class="caret left icon"></i>
          <g:message code="common.back"/>
        </g:link>

        <div class="sub header">
          <div class="sub header">Création d'une question</div>
        </div>
      </div>
    </h2>

    <div class="ui bottom attached segment">

      <g:render template="/assignment/sequence/statement_form-elaastic" bean="${statementInstance}"
                model="[assignmentInstance: assignmentInstance, sequenceInstance: sequenceInstance]"/>

      <g:render template="/assignment/sequence/statement_question_type_form-elaastic"
                model="[sequenceInstance: sequenceInstance]"/>

      <g:render template="/assignment/sequence/statement_explanations_form-elaastic"
                model="[sequenceInstance: sequenceInstance]"/>

    </div>

    <div class="ui hidden divider"></div>

    <button type="submit" class="ui primary button">
      ${message(code: 'default.button.create.label', default: 'Create')}
    </button>

    <g:link action="show"
            controller="assignment"
            id="${assignmentInstance.id}"
            class="ui button">

      <g:message code="common.cancel"/>
    </g:link>

    <div class="ui hidden divider"></div>
  </g:form>
</div>
</body>
</html>
