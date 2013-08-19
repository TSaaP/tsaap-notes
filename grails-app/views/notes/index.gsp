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
<g:render template="edit" model='[editedNote:editedNote,context:context]'/>
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
        <li class="list-group-item" style="padding-bottom: 20px" id="note${note.id}">
          <g:set var="noteIsBookmarked" value="${note.isBookmarkedByUser(user)}"/>
          <h6 class="list-group-item-heading"><strong>${note.author.fullname}</strong> <small>@${note.author.username}</small>

            <g:if test="${note.context}">
              <span class="badge">
                <g:if test="${context}">
                  ${note.context.contextName}
                </g:if>
                <g:else>
                  <g:link controller="notes" action="index"
                          params='[contextId:"${note.contextId}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]'>${note.context.contextName}
                  </g:link>
                </g:else>
              </span>
            </g:if>
            <small class="pull-right"><g:formatDate date="${note.dateCreated}"
                                                    type="datetime" style="LONG"
                                                    timeStyle="SHORT"/></small>
            <g:if test="${noteIsBookmarked}"><span class="pull-right glyphicon glyphicon-star" style="color: orange; padding-right: 5px;"></span> </g:if>

          </h6>

          <p>${note.content}</p>

          <small class="pull-right">
            <g:if test="${note.hasParent()}">
              <a href="#note${note.id}"><span class="glyphicon glyphicon-circle-arrow-left"></span> Show discussion</a>
            </g:if>
            <a href="#note${note.id}" id="replyLink${note.id}" onclick="displaysReplyField(${note.id})"><span
                    class="glyphicon glyphicon-share"></span> Reply</a>
            <g:if test="${noteIsBookmarked}">
              <g:link style="color: orange" controller="notes" action="unbookmarkNote" params='[noteId:"${note.id}",contextId:"${context ? context.id :''}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]' fragment="note${note.id}"><span
                                  class="glyphicon glyphicon-star"></span> Favorite</g:link>
            </g:if>
            <g:else>
            <g:link controller="notes" action="bookmarkNote" params='[noteId:"${note.id}",contextId:"${context ? context.id :''}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]' fragment="note${note.id}"><span
                    class="glyphicon glyphicon-star"></span> Favorite</g:link>
            </g:else>
          </small>
          <div id="replyEdition${note.id}" style="display:none">
            <g:render template="edit" model="[editedNote:editedNote,context:context, parentNote:note]"/>
          </div>
        </li>
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