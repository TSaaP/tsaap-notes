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
    <g:if test="${noteIsBookmarked}"><span
            class="pull-right glyphicon glyphicon-star"
            style="color: orange; padding-right: 5px;"></span></g:if>

  </h6>

  <p>${note.content}</p>

  <small class="pull-right">
    <g:if test="${note.hasParent()}">
      <a href="#note${note.id}"><span
              class="glyphicon glyphicon-circle-arrow-left"></span> Show discussion
      </a>
    </g:if>
    <a href="#note${note.id}" id="replyLink${note.id}"
       onclick="displaysReplyField(${note.id})"><span
            class="glyphicon glyphicon-share"></span> Reply</a>
    <g:if test="${user == note.author}">
      <g:link controller="notes" action="deleteNote"
              params='[noteId:"${note.id}",contextId:"${context ? context.id : ''}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]'><span
              class="glyphicon glyphicon-trash"></span> Delete</g:link>
    </g:if>
    <g:if test="${noteIsBookmarked}">
      <g:link style="color: orange" controller="notes" action="unbookmarkNote"
              params='[noteId:"${note.id}",contextId:"${context ? context.id : ''}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]'
              fragment="note${note.id}"><span
              class="glyphicon glyphicon-star"></span> Favorite</g:link>
    </g:if>
    <g:else>
      <g:link controller="notes" action="bookmarkNote"
              params='[noteId:"${note.id}",contextId:"${context ? context.id : ''}",displaysMyNotes:"${displaysMyNotes ? 'on' : ''}",displaysMyFavorites:"${displaysMyFavorites ? 'on' : ''}", displaysAll:"${displaysAll ? 'on' : ''}"]'
              fragment="note${note.id}"><span
              class="glyphicon glyphicon-star"></span> Favorite</g:link>
    </g:else>
  </small>

  <div id="replyEdition${note.id}" style="display:none; padding-top:20px">
    <g:render template="edit"
              model="[context: context, parentNote: note]"/>
  </div>
</li>