<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui,vue_js,underscore_js,ckeditor_vue_js"/>
  <g:set var="entityName" value="${message(code: 'sequence.label', default: 'Question')}"/>
  <title>${sequenceInstance?.title}</title>
</head>

<body>

<g:set var="assignmentInstance" value="${sequenceInstance.assignment}"/>
<g:set var="statementInstance" value="${sequenceInstance.statement}"/>

<content tag="specificMenu">
  <g:link action="show"
          controller="assignment"
          id="${assignmentInstance.id}"
          class="item"
          data-tooltip="${g.message(code: 'assignment.back.to.assignment')}"
          data-position="right center"
          data-inverted="">

    <i class="yellow long arrow alternate left icon"></i>
  </g:link>


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
    <i class="${sequenceInstance?.statement?.attachment ? 'grey' : 'yellow'} attach icon"></i>
  </a>

  <g:link class="item"
          controller="player"
          action="playSequence"
          id="${sequenceInstance.id}"
          data-tooltip="${message(code: 'sequence.play')}"
          data-position="right center"
          data-inverted="">
    <i class="yellow play icon"></i>
  </g:link>
</content>

<div id="edit-sequence">
  <g:form name="sequenceForm"
          class="ui form"
          controller="sequence"
          action="updateSequence"
          method="post"
          enctype="multipart/form-data">

    <g:hiddenField name="sequence_instance_id" value="${sequenceInstance?.id}"/>

    <g:if test="${flash.message}">
      <div class="ui visible success message" role="status">
        <div class="header">${raw(flash.message)}</div>
      </div>
    </g:if>

    <g:hasErrors bean="${sequenceInstance}">
      <div class="ui visible error message">
        <div class="header">
          Veuillez corriger les erreurs suivantes pour pouvoir mettre à jour cette question :
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


    <h2 class="ui header">
      <i class="large icons">
        <i class="question circle outline icon"></i>
        <i class="corner pencil alternate icon"></i>
      </i>

      <div class="content" style="vertical-align: middle;">
        <g:message code="question.update" />
      </div>
    </h2>

    <h3 class="ui block top attached header">

      <div class="content">
        ${assignmentInstance?.title} – Question n°${sequenceInstance.rank}
      </div>
    </h3>

    <div class="ui bottom attached segment">

      <g:render template="/assignment/sequence/statement_form-elaastic" bean="${statementInstance}"
                model="[assignmentInstance: assignmentInstance, sequenceInstance: sequenceInstance]"/>

      <g:render template="/assignment/sequence/statement_question_type_form-elaastic"
                model="[sequenceInstance: sequenceInstance]"/>

      <div class="ui hidden divider"></div>

      <g:render template="/assignment/sequence/statement_explanations_form-elaastic"
                model="[sequenceInstance: sequenceInstance]"/>

    </div>

    <div class="ui hidden divider"></div>

    <button type="submit" class="ui primary button" name="returnOnSubject" value="true">
      ${message(code: 'question.updateAndReturnToSubject')}
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
