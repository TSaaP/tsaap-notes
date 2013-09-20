<%@ page import="org.tsaap.notes.Context" %>



<div class="fieldcontain ${hasErrors(bean: context, field: 'contextName', 'error')} required">
  <label for="contextName">
    <g:message code="context.contextName.label" default="Context Name"/>
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="contextName" required="" value="${context?.contextName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: context, field: 'url', 'error')} ">
  <label for="url">
    <g:message code="context.url.label" default="Url"/>

  </label>
  <g:field type="url" name="url" value="${context?.url}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: context, field: 'descriptionAsNote', 'error')} ">
  <label for="descriptionAsNote">
    <g:message code="context.descriptionAsNote.label"
               default="Description As Note"/>

  </label>
  <g:textArea name="descriptionAsNote" cols="40" rows="5" maxlength="280"
              value="${context?.descriptionAsNote}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: context, field: 'owner', 'error')} required">
  <label for="owner">
    <g:message code="context.owner.label" default="Owner"/>
    <span class="required-indicator">*</span>
  </label>
  <g:select id="owner" name="owner.id" from="${org.tsaap.directory.User.list()}"
            optionKey="id" required="" value="${context?.owner?.id}"
            class="many-to-one"/>
</div>

<div class="fieldcontain ${hasErrors(bean: context, field: 'ownerIsTeacher', 'error')} ">
  <label for="ownerIsTeacher">
    <g:message code="context.ownerIsTeacher.label" default="Owner Is Teacher"/>

  </label>
  <g:checkBox name="ownerIsTeacher" value="${context?.ownerIsTeacher}"/>
</div>

