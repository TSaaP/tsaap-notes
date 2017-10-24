<%@ page import="groovy.json.StringEscapeUtils; org.tsaap.assignments.Assignment" %>

<div class="ui error message"></div>

<div class="required field">
    <label><g:message code="assignment.title.label" default="Title"/></label>
    <g:textField name="title" value="${assignmentInstance?.title}" autofocus="autofocus" />
</div>

<r:script>
    $(document)
        .ready(function () {
          $('.ui.form')
  .form({
    fields: {
      title     : {
        identifier: 'title',
        rules: [
          {
            type   : 'empty',
            prompt : '${groovy.json.StringEscapeUtils.escapeJavaScript(g.message(code: 'assignment.title.mandatory'))}'
          }
        ]
      }
    }
  })
     
    });
</r:script>



