<%@ page import="org.tsaap.notes.Context" %>



<div class="form-group fieldcontain ${hasErrors(bean: context, field: 'contextName', 'error')} required">
  <label for="contextName">
    <g:message code="context.contextName.label" default="Context Name"/>
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="contextName" required="" value="${context?.contextName}" class="form-control"/>
</div>

<div class="form-group fieldcontain ${hasErrors(bean: context, field: 'url', 'error')} ">
  <label for="url">
    <g:message code="context.url.label" default="Url"/>

  </label>
  <g:field type="url" name="url" value="${context?.url}" class="form-control"/>
</div>

<div class="form-group fieldcontain ${hasErrors(bean: context, field: 'descriptionAsNote', 'error')} ">
  <label for="descriptionAsNote">
    <g:message code="context.descriptionAsNote.label"
               default="Description As Note"/>

  </label>
  <g:textArea id="descriptionAsNote" name="descriptionAsNote" cols="40" rows="3" maxlength="280"
              value="${context?.descriptionAsNote}" class="form-control"/>
  <span class="character_counter" id="character_counter"></span>
</div>


<div class="checkbox fieldcontain ${hasErrors(bean: context, field: 'ownerIsTeacher', 'error')} ">
  <g:checkBox name="ownerIsTeacher" value="${context?.ownerIsTeacher}"/>
  <g:message code="context.ownerIsTeacher.label" default="You teach on this context"/>
</div>

<g:hiddenField name="owner" value="${user.id}"/>

<r:script>
  $(document).ready(function () {

    // set character counters
    //-----------------------

    // Get the textarea field
    $("#descriptionAsNote")
      // Bind the counter function on keyup and blur events
            .bind('keyup blur', function () {
                    // Count the characters and set the counter text
                    var counter =  $("#character_counter${idControllSuffix}");
                    counter.text($(this).val().length + '/280 characters');
                  })

      // Trigger the counter on first load
            .keyup();

  });
</r:script>