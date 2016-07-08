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

<%@ page import="org.tsaap.questions.LiveSessionStatus" %>
<g:if test="${note.parentNote && showDiscussionNote}">
    <g:render template="detail"
              model="[note: note.parentNote, context: context, displaysMyNotes: displaysMyNotes, displaysMyFavorites: displaysMyFavorites, displaysAll: displaysAll, noteClassParent: 'note-parent-note', showDiscussionNote: showDiscussionNote, kind: kind]"/>
</g:if>
<li class="list-group-item ${noteClassParent}" style="padding-bottom: 20px" xmlns="http://www.w3.org/1999/html">
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
        <small><g:formatDate date="${note.dateCreated}"
                             type="datetime" style="LONG"
                             timeStyle="SHORT"/></small>
        <g:if test="${noteIsBookmarked}"><span
                class="glyphicon glyphicon-star"
                style="color: orange; padding-right: 5px;"></span></g:if>

        <g:set var="displayListParamsWithPagination"
               value="${[contextId: params.contextId, fragmentTagId: params.fragmentTagId, fragmentTagName: params.fragmentTagName, max: params.max ?: 7, offset: params.offset ?: 0] + displayListParams}"/>
        <span class="pull-right">
            <g:if test="${user == note.author || user == note?.context?.owner}">
                <g:link onclick="return confirm('${message(code: "notes.detail.delete.confirmation")}');"
                        controller="notes" action="deleteNote"
                        params="${[noteId: note.id] + displayListParamsWithPagination}"><span
                        class="glyphicon glyphicon-trash" data-toggle="tooltip"
                        title="${message(code: "notes.detail.delete")}"
                        data-placement="bottom"></span></g:link>
                <g:if test="${(context.noteTakingEnabled || note.kind == org.tsaap.notes.NoteKind.QUESTION.ordinal()) && context.isOpen() && !note.liveSession}">
                    <g:link data-toggle="modal" data-target="#modalNote${note.id}">
                        <span class="glyphicon glyphicon-pencil" data-toggle="tooltip"
                              title="${message(code: "notes.detail.edit")}"
                              data-placement="bottom"></span>
                    </g:link>
                    <div class="modal fade" id="modalNote${note.id}" role="dialog" aria-labelledby="exampleModalLabel">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                            aria-hidden="true">&times;</span></button>
                                </div>

                                <div class="modal-body">

                                    <div class="container note-edition">
                                        <g:if test="${note.kind == org.tsaap.notes.NoteKind.QUESTION.ordinal()}">
                                            <g:render template="/questions/edit"
                                                      model='[note: note, context: context, fragmentTag: note.fragmentTag, update: true]'/>
                                        </g:if>
                                        <g:else>
                                            <g:render template="/notes/edit"
                                                      model='[note: note, context: context, fragmentTag: note.fragmentTag, update: true]'/>
                                        </g:else>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </g:if>
                <g:if test="${note.kind == org.tsaap.notes.NoteKind.QUESTION.ordinal()}">
                    <g:if test="${!note.isFirstQuestionInContext}">
                        <g:link controller="notes" action="moveQuestionUp"
                                params="${[noteId: note.id] + displayListParamsWithPagination}"><span
                                class="glyphicon glyphicon-arrow-up" data-toggle="tooltip"
                                title="${message(code: "notes.detail.up")}"
                                data-placement="bottom"></span></g:link>
                    </g:if>
                    <g:if test="${!note.isLastQuestionInContext}">
                        <g:link controller="notes" action="moveQuestionDown"
                                params="${[noteId: note.id] + displayListParamsWithPagination}"><span
                                class="glyphicon glyphicon-arrow-down" data-toggle="tooltip"
                                title="${message(code: "notes.detail.down")}"
                                data-placement="bottom"></span></g:link>
                    </g:if>
                </g:if>
            </g:if>
        </span>
    </h6>
    <g:set var="attachment" value="${note.attachment}"/>
    <g:if test="${attachment != null}">
        <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
    </g:if>
    <g:if test="${note.isAQuestion()}">
        <g:set var="liveSession" value="${note.getLiveSession()}"/>
        <g:set var="sessionPhase" value="${liveSession ? liveSession.findCurrentPhase() : null}"/>
        <g:set var="userType" value="${(user == note.author || user == note?.context?.owner) ? 'author' : 'user'}"/>

        <g:if test="${!sessionPhase || liveSession.isStopped()}">
            <g:set var="sessionStatus"
                   value="${liveSession ? liveSession.status : LiveSessionStatus.NotStarted.name()}"/>
            <g:render template="/questions/${userType}/${sessionStatus}/detail"
                      model="[note: note, liveSession: liveSession, user: user]"/>
        </g:if>
        <g:else>
            <g:render template="/questions/${userType}/Phase${sessionPhase.rank}/${sessionPhase.status}/detail"
                      model="[note: note, sessionPhase: sessionPhase, user: user]"/>
        </g:else>
    </g:if>
    <g:else>
        <p id="content${note.id}" class="note-content">${note.content}</p>
    </g:else>
    <div id="noteActions" class="pull-right note-actions">
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
            <g:if test="${context.noteTakingEnabled && context.isOpen() && !note.isAQuestion()}">
                <a href="#note${note.id}" id="replyLink${note.id}"
                   onclick="displaysReplyField(${note.id})"><span
                        class="glyphicon glyphicon-share"></span> ${message(code: "notes.detail.reply")}</a>
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
                      class="glyphicon glyphicon-thumbs-up">${message(code: "notes.detail.learn")}</span>
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

    <g:if test="${context.noteTakingEnabled && context.isOpen() && !note.isAQuestion()}">
        <div id="replyEdition${note.id}" style="display:none; padding-top:20px">
            <g:render template="/notes/edit"
                      model="[context: context, parentNote: note]"/>
        </div>
    </g:if>
</li>
