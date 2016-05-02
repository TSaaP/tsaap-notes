<%@ page import="org.tsaap.questions.LiveSessionStatus" %>
<g:if test="${note.parentNote && showDiscussionNote}">
    <g:render template="detail"
              model="[note: note.parentNote, context: context, displaysMyNotes: displaysMyNotes, displaysMyFavorites: displaysMyFavorites, displaysAll: displaysAll, noteClassParent: 'note-parent-note', showDiscussionNote: showDiscussionNote, kind: kind]"/>
</g:if>
<li class="list-group-item ${noteClassParent}" style="padding-bottom: 20px">
    <g:set var="noteIsBookmarked" value="${note.isBookmarkedByUser(user)}"/>
    <g:set var="noteIsScored" value="${note.isScoredByUser(user)}"/>
    <g:set var="displayListParams"
           value="${[displaysMyNotes: params.displaysMyNotes, displaysMyFavorites: params.displaysMyFavorites, displaysAll: params.displaysAll, inline: params.inline, kind: params.kind]}"/>
    <h6 class="list-group-item-heading"><strong>@${note.author.username}</strong>

        <g:if test="${note.context}">

            <span class="badge">
                <g:link controller="notes" action="index"
                        params="${[contextId: note.contextId] + displayListParams}">
                    ${note.context.contextName}
                </g:link>
            </span>
            <g:if test="${note.fragmentTag}">
                <span class="badge">
                    <g:link controller="notes" action="index"
                            params="${[contextId: note.contextId, fragmentTagId: note.fragmentTagId] + displayListParams}">
                        #${note.fragmentTag.name}
                    </g:link>
                </span>
            </g:if>

            <g:if test="${note.noteUrl}"><span class="badge">
                <a href="${note.noteUrl}" target="_blank"><span
                        class="glyphicon glyphicon-share"></span></a>
            </span>
            </g:if>
        </g:if>
        <small class="pull-right"><g:formatDate date="${note.dateCreated}"
                                                type="datetime" style="LONG"
                                                timeStyle="SHORT"/></small>
        <g:if test="${noteIsBookmarked}"><span
                class="pull-right glyphicon glyphicon-star"
                style="color: orange; padding-right: 5px;"></span></g:if>

    </h6>
    <g:set var="attachment" value="${note.attachment}"/>
    <g:if test="${attachment != null}">
        <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
    </g:if>
    <g:if test="${note.isAQuestion()}">
        <g:set var="liveSession" value="${note.getLiveSession()}"/>
        <g:set var="sessionPhase" value="${liveSession ? liveSession.findCurrentPhase() : null}"/>
        <g:set var="userType" value="${(user == note.author || user ==  note?.context?.owner) ? 'author' : 'user'}"/>

        <g:if test="${!sessionPhase || liveSession.isStopped()}">
            <g:set var="sessionStatus" value="${liveSession ? liveSession.status : LiveSessionStatus.NotStarted.name()}"/>
            <g:render template="/questions/${userType}/${sessionStatus}/detail" model="[note: note,liveSession:liveSession,user:user]"/>
        </g:if>
        <g:else>
            <g:render template="/questions/${userType}/Phase${sessionPhase.rank}/${sessionPhase.status}/detail" model="[note: note,sessionPhase:sessionPhase,user:user]"/>
        </g:else>
    </g:if>
    <g:else>
        <p id="content${note.id}" class="note-content">${note.content}</p>
    </g:else>
    <div id="noteActions" class="pull-right note-actions">
        <g:set var="displayListParamsWithPagination"
               value="${[contextId: params.contextId, fragmentTagId: params.fragmentTagId, fragmentTagName: params.fragmentTagName, max: params.max ?: 7, offset: params.offset ?: 0] + displayListParams}"/>
        <small>
            <g:if test="${note.hasParent() && !noteClassParent && !showDiscussionNote}">
                <g:link controller="notes" action="showDiscussion"
                        params="${[noteId: note.id] + displayListParamsWithPagination}"
                        fragment="note${note.id}">
                    <span class="glyphicon glyphicon-circle-arrow-left"></span> ${message(code: "notes.detail.showDiscussion")}
                </g:link>
            </g:if>
            <g:if test="${note.hasParent() && !noteClassParent && showDiscussionNote}">
                <g:link controller="notes" action="hideDiscussion"
                        params="${displayListParamsWithPagination}"
                        fragment="note${note.id}">
                    <span class="glyphicon glyphicon-circle-arrow-right"></span> ${message(code: "notes.detail.hideDiscussion")}
                </g:link>
            </g:if>
            <a href="#note${note.id}" id="replyLink${note.id}"
               onclick="displaysReplyField(${note.id})"><span
                    class="glyphicon glyphicon-share"></span> ${message(code: "notes.detail.reply")}</a>
            <g:if test="${user == note.author || user ==  note?.context?.owner}">
                <g:link controller="notes" action="deleteNote"
                        params="${[noteId: note.id] + displayListParamsWithPagination}"><span
                        class="glyphicon glyphicon-trash"></span> ${message(code: "notes.detail.delete")}</g:link>
            </g:if>
            <g:if test="${noteIsBookmarked}">
                <g:link style="color: orange" controller="notes" action="unbookmarkNote"
                        params="${[noteId: note.id] + displayListParamsWithPagination}"
                        fragment="note${note.id}"><span
                        class="glyphicon glyphicon-star"></span> ${message(code: "notes.detail.favorite")}</g:link>
            </g:if>
            <g:else>
                <g:link controller="notes" action="bookmarkNote"
                        params="${[noteId: note.id] + displayListParamsWithPagination}"
                        fragment="note${note.id}"><span
                        class="glyphicon glyphicon-star"></span> ${message(code: "notes.detail.favorite")}</g:link>
            </g:else>
            <g:if test="${noteIsScored}">
                <span style="color: orange"
                        class="glyphicon glyphicon-thumbs-up"> ${message(code: "notes.detail.learn")}</span>
            </g:if>
            <g:else>
            <g:link controller="notes" action="markAsLikedNote"
                params="${[noteId: note.id] + displayListParamsWithPagination}"
                fragment="note${note.id}"><span
                class="glyphicon glyphicon-thumbs-up"></span> ${message(code: "notes.detail.learn")}</g:link>
            </g:else>
        </small>
    </div>
    <div>&nbsp;</div>
    <g:if test="${note.score > 1}">
        <div>
            <small class="pull-right">${note.score} ${message(code: "notes.detail.multipleLearnScore")}</small>
        </div>
    </g:if>
    <g:if test="${note.score == 1}">
        <div>
            <small class="pull-right">${note.score} ${message(code: "notes.detail.uniqueLearnScore")}</small>
        </div>
    </g:if>


    <div id="replyEdition${note.id}" style="display:none; padding-top:20px">
        <g:render template="edit"
                  model="[context: context, parentNote: note]"/>
    </div>
</li>
