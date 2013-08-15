%{--
  - Copyright 2013 Tsaap Development Group
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%



<html>
<head>
  <meta name="layout" content="main"/>
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>
<div class="container note-edition">
  <g:if test="${note?.hasErrors()}">
    <div class="alert alert-danger">
      <g:eachError bean="${note}">
        <li><g:message error="${it}"/></li>
      </g:eachError>
    </div>
  </g:if>
  <g:form method="post" controller="notes" action="addNote">
    <g:hiddenField name="contextId" value="${context?.id}"/>
    <g:hiddenField name="displaysMyNotes" value="true"/>
    <textarea class="form-control" rows="3" id="noteContent" name="noteContent"
              maxlength="280"
              value="${fieldValue(bean: note, field: 'content')}"></textarea>
    <span id="character_counter"></span><button type="submit"
                                                class="btn btn-primary btn-xs pull-right"><span
            class="glyphicon glyphicon-plus"></span> Add note</button>
  </g:form>
</div>

<div class="divider"></div>

<div class="container note-list">
  <div class="note-list-header">
    <g:if test="${context}">
    <div class="note-list-context pull-left">
      <button type="button" class="btn btn-default btn-xs" id="button_context">
        &${context.contextName}
      </button>
    </div>
    </g:if>
    <div class="note-list-selector pull-right">
      <form>
        <label class="checkbox-inline">
          <input type="checkbox" id="displays_my_notes"
                 value="displays_my_notes"
                 checked="checked"> My notes
        </label>
        <label class="checkbox-inline">
          <input type="checkbox" id="displays_my_favorite"
                 value="displays_my_favorite"> My favorites
        </label>
        <label class="checkbox-inline">
          <input type="checkbox" id="displays_other"
                 value="displays_other"> Others
        </label>
      </form>
    </div>
  </div>

  <div class="note-list-content">
    <ul class="list-group">
      <g:each in="${notes}" var="note">
      <li class="list-group-item" style="padding-bottom: 20px">
        <h6 class="list-group-item-heading"><strong>${user.fullname}</strong> <small>@<sec:username/></small>
          <small class="pull-right"><g:formatDate date="${note.lastUpdated}" type="datetime" style="LONG" timeStyle="SHORT"/></small></h6>

        <p>${note.content}</p>

        <small class="pull-right">
          <a href="#"><span
                  class="glyphicon glyphicon-share"></span> Reply</a>
          <a href="#"><span
                  class="glyphicon glyphicon-star"></span> Favorite</a>
        </small>
      </li>
      </g:each>
    </ul>
  </div>

  <div class="note-list-pagination">
    <ul class="pagination pull-right">
      <li><a href="#">&laquo;</a></li>
      <li><a href="#">1</a></li>
      <li><a href="#">2</a></li>
      <li><a href="#">3</a></li>
      <li><a href="#">4</a></li>
      <li><a href="#">5</a></li>
      <li><a href="#">&raquo;</a></li>
    </ul>
  </div>
</div>
<g:if test="${context}">
<r:script>
  $('#button_context').popover({
                                 title: "${context.contextName}",
                                 content: "<p><strong>url</strong>: <a href='${context.url}' target='blank'>${context.url}</a></p> <p>${context.descriptionAsNote}</p>",
                                 html: true
                               })

</r:script>
</g:if>
<r:script>
  jQuery(document).ready(function ($) {

    // Get the textarea field
    $('#noteContent')

      // Bind the counter function on keyup and blur events
            .bind('keyup blur', function () {
                    // Count the characters and set the counter text
                    $('#character_counter').text($(this).val().length + '/280 characters');
                  })

      // Trigger the counter on first load
            .keyup();
  });
</r:script>
</body>
</html>