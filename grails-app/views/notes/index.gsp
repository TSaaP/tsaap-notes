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



<html xmlns="http://www.w3.org/1999/html">
<head>
  <meta name="layout" content="main"/>
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>

<div class="container note-edition">
<g:render template="edit" model='[context:context]'/>
</div>

<div class="divider"></div>

<div class="container note-list">
  <div class="note-list-header">
    <g:if test="${context}">
      <div class="note-list-context pull-left">
        <button type="button" class="btn btn-default btn-xs"
                id="button_context">
          ${context.contextName}
        </button>
      </div>
    </g:if>
    <div class="note-list-selector pull-right">
      <g:form controller="notes" action="index" method="get">
        <g:hiddenField name="contextId" value="${context?.id}"/>
        <label class="checkbox-inline">
          <g:checkBox name="displaysMyNotes" checked="${displaysMyNotes}"
                      onchange="submit();"/> My notes
        </label>
        <label class="checkbox-inline">
          <g:checkBox name="displaysMyFavorites"
                      checked="${displaysMyFavorites}"
                      onchange="submit();"/> My favorites
        </label>
        <label class="checkbox-inline">
          <g:if test="${context}">
          <g:checkBox name="displaysAll" checked="${displaysAll}"
                      onchange="submit();"/>  All
          </g:if>
          <g:else>
            <input type="checkbox" name="displaysAll" disabled/> <span style="color: gainsboro">All</span>
          </g:else>
        </label>
      </g:form>
    </div>
  </div>

  <div class="note-list-content">
    <ul class="list-group">
      <g:each in="${notes.list}" var="note">
        <g:render template="detail" model="[note:note,context:context,displaysMyNotes:displaysMyNotes, displaysMyFavorites:displaysMyFavorites, displaysAll:displaysAll]"/>
      </g:each>
    </ul>
  </div>

  <div class="note-list-pagination">
    <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${notes.totalCount}" params='[contextId:"${context ? context.id :''}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]'/>
  </div>
</div>
<g:if test="${context}">
  <r:script>
  $('#button_context').popover({
                                 title: "${context.contextName}",
                                 content: "<p><strong>url</strong>: <a href='${context.url}' target='blank'>${context.url}</a></p><p>${context.descriptionAsNote}</p>",
                                 html: true
                               })

  </r:script>
</g:if>
<r:script>
  function displaysReplyField(noteId) {
    $('#replyEdition'+noteId).toggle();
    var contentElement =  $('#noteContent'+noteId) ;
    var content = contentElement.val() ;
    contentElement.focus().val('').val(content) ;
  }
</r:script>
</body>
</html>