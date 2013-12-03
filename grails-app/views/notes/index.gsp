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
  <g:if test="${params.inline && params.inline == 'on'}">
    <meta name="layout" content="inline"/>
  </g:if>
  <g:else>
    <meta name="layout" content="main"/>
  </g:else>
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>

<div class="container note-edition">
<g:render template="edit" model='[context:context, fragmentTag:fragmentTag]'/>
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
        <g:if test="${fragmentTag}">
          <span class="badge"
                          id="button_fragment_tag">#${fragmentTag.name} <g:link controller="notes" params='[contextId:"${params.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}"]'><span class="glyphicon glyphicon-remove-sign"></span></g:link></span>
        </g:if>
      </div>
    </g:if>
    <div class="note-list-selector pull-right">
      <g:form controller="notes" action="index" method="get">
        <g:hiddenField name="contextId" value="${context?.id}"/>
        <label class="checkbox-inline">
          <g:checkBox name="displaysMyNotes" checked="${params.displaysMyNotes=='on'?true:false}"
                      onchange="submit();"/> My notes
        </label>
        <label class="checkbox-inline">
          <g:checkBox name="displaysMyFavorites"
                      checked="${params.displaysMyFavorites=='on'?true:false}"
                      onchange="submit();"/> My favorites
        </label>
        <label class="checkbox-inline">
          <g:if test="${context}">
          <g:checkBox name="displaysAll" checked="${params.displaysAll=='on'?true:false}"
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
        <g:set var="showDiscussionNote" value="${showDiscussion[note.id]}"/>
        <div id="note${note.id}" class="${showDiscussionNote?'note-discussion':'note-only'}">
          <g:render template="detail" model="[note:note,context:context,showDiscussionNote:showDiscussionNote]"/>
        </div>
      </g:each>
    </ul>
  </div>

  <div class="note-list-pagination">
    <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${notes.totalCount}" params='[contextId:"${params.contextId}",fragmentTagId:"${params.fragmentTagId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}", inline:"${params.inline}"]'/>
  </div>
</div>
<g:if test="${context}">
  <r:script>
  $('#button_context').popover({
                                 content: "<p><strong>url</strong>: <a href='${context.url}' target='blank'>${context.url}</a></p><p>${context.descriptionAsNote?.replaceAll('[\n\r]',' ')}</p>",
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

  $(".note-content").linkify({
                               target:"_blank"
                             });
  $(".nav li").removeClass('active');
  $("#mainLinkNotes").addClass('active');
</r:script>
</body>
</html>