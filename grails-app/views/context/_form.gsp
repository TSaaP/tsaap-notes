%{--
  - Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
  -
  -     This program is free software: you can redistribute it and/or modify
  -     it under the terms of the GNU Affero General Public License as published by
  -     the Free Software Foundation, either version 3 of the License, or
  -     (at your option) any later version.
  -
  -     This program is distributed in the hope that it will be useful,
  -     but WITHOUT ANY WARRANTY; without even the implied warranty of
  -     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -     GNU Affero General Public License for more details.
  -
  -     You should have received a copy of the GNU Affero General Public License
  -     along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<%@ page import="org.tsaap.notes.Context" %>



<div class="form-group fieldcontain ${hasErrors(bean: context, field: 'contextName', 'error')} required">
    <label for="contextName">
        <g:message code="context.scopeName.label" default="Scope Name"/>
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
        <g:message code="context.form.description.label"
                   default="Description As Note"/>

    </label>
    <g:textArea id="descriptionAsNote" name="descriptionAsNote" cols="40" rows="3" maxlength="280"
                value="${context?.descriptionAsNote}" class="form-control"/>
    <span class="character_counter" id="character_counter"></span>
</div>


<div class="checkbox fieldcontain ${hasErrors(bean: context, field: 'ownerIsTeacher', 'error')} ">
    <g:checkBox name="ownerIsTeacher" value="${context?.ownerIsTeacher}"/>
    <g:message code="context.form.teach.checkbox" default="You teach on this context"/>
</div>

<div class="checkbox fieldcontain ${hasErrors(bean: context, field: 'noteTakingEnabled', 'error')}">
    <g:checkBox name="noteTakingEnabled" value="${context?.noteTakingEnabled}"/>
    <g:message code="context.form.notes.checkbox"/>
</div>

<g:hiddenField name="owner" value="${context?.owner?.id}"/>

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