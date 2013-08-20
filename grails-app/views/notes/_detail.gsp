<g:if test="${note.parentNote && showDiscussionNote}">
  <g:render template="detail" model="[note:note.parentNote,context:context,displaysMyNotes:displaysMyNotes, displaysMyFavorites:displaysMyFavorites, displaysAll:displaysAll, noteClassParent:'note-parent-note', showDiscussionNote:showDiscussionNote]"/>
</g:if>
<li class="list-group-item ${noteClassParent}" style="padding-bottom: 20px">
  <g:set var="noteIsBookmarked" value="${note.isBookmarkedByUser(user)}"/>
  <h6 class="list-group-item-heading"><strong>${note.author.fullname}</strong> <small>@${note.author.username}</small>

    <g:if test="${note.context}">
      <span class="badge">
        <g:if test="${context}">
          ${note.context.contextName}
        </g:if>
        <g:else>
          <g:link controller="notes" action="index"
                  params='[contextId:"${note.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}"]'>${note.context.contextName}
          </g:link>
        </g:else>
      </span>
    </g:if>
    <small class="pull-right"><g:formatDate date="${note.dateCreated}"
                                            type="datetime" style="LONG"
                                            timeStyle="SHORT"/></small>
    <g:if test="${noteIsBookmarked}"><span
            class="pull-right glyphicon glyphicon-star"
            style="color: orange; padding-right: 5px;"></span></g:if>

  </h6>

  <p id="content${note.id}" class="note-content">${note.content}</p>

  <div id="noteActions" class="pull-right note-actions">
  <small>
    <g:if test="${note.hasParent() && !noteClassParent && !showDiscussionNote}">
      <g:link controller="notes" action="showDiscussion" params='[noteId:"${note.id}",contextId:"${params.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}",max:"${params.max?:7}", offset:"${params.offset?:0}"]' fragment="note${note.id}">
       <span class="glyphicon glyphicon-circle-arrow-left"></span> Show discussion
      </g:link>
    </g:if>
    <g:if test="${note.hasParent() && !noteClassParent && showDiscussionNote}">
          <g:link controller="notes" action="hideDiscussion" params='[contextId:"${params.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}",max:"${params.max?:7}", offset:"${params.offset?:0}"]' fragment="note${note.id}">
           <span class="glyphicon glyphicon-circle-arrow-right"></span> Hide discussion
          </g:link>
        </g:if>
    <a href="#note${note.id}" id="replyLink${note.id}"
       onclick="displaysReplyField(${note.id})"><span
            class="glyphicon glyphicon-share"></span> Reply</a>
    <g:if test="${user == note.author}">
      <g:link controller="notes" action="deleteNote"
              params='[noteId:"${note.id}",contextId:"${params.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}"]'><span
              class="glyphicon glyphicon-trash"></span> Delete</g:link>
    </g:if>
    <g:if test="${noteIsBookmarked}">
      <g:link style="color: orange" controller="notes" action="unbookmarkNote"
              params='[noteId:"${note.id}",contextId:"${params.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}",max:"${params.max?:7}", offset:"${params.offset?:0}"]'
              fragment="note${note.id}"><span
              class="glyphicon glyphicon-star"></span> Favorite</g:link>
    </g:if>
    <g:else>
      <g:link controller="notes" action="bookmarkNote"
              params='[noteId:"${note.id}",contextId:"${params.contextId}",displaysMyNotes:"${params.displaysMyNotes}",displaysMyFavorites:"${params.displaysMyFavorites}", displaysAll:"${params.displaysAll}",max:"${params.max?:7}", offset:"${params.offset?:0}"]'
              fragment="note${note.id}"><span
              class="glyphicon glyphicon-star"></span> Favorite</g:link>
    </g:else>
  </small>
  </div>
  <div id="replyEdition${note.id}" style="display:none; padding-top:20px">
    <g:render template="edit"
              model="[context: context, parentNote: note]"/>
  </div>
</li>
