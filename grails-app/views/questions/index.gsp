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
<div class="container">
    <ol class="breadcrumb" style="display: inline-block;">
        <li>
            <g:link controller="context" params='[filter: "__FOLLOWED__"]'>
                ${message(code: 'notes.scope.link')}</g:link>
        </li>
        <g:if test="${context}">
            <g:if test="${fragmentTag}">
                <li>
                    <g:link controller="notes"
                            params='[contextId: "${params.contextId}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", inline: "${params.inline}"]'>
                        ${context.contextName}</g:link>
                </li>
                <li class="active">
                    ${fragmentTag.name}
                </li>
            </g:if>
            <g:else>
                <li class="active">
                    ${context.contextName}
                    <g:if test="${context.isClosed()}">
                        <span class="label label-danger">
                            ${message(code: 'context.scopeStatus.close')}
                        </span>
                    </g:if>
                </li>
            </g:else>
        </g:if>
    </ol>
    <g:if test="${context?.noteTakingEnabled}">
        <ul class="nav nav-tabs pull-right">
            <li role="presentation">
                <g:link controller="notes"
                        params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", inline: "${params.inline}"]'>
                    ${message(code: "notes.link")} <span class="badge">${countTotal}</span>
                </g:link>
            </li>
            <li role="presentation" class="active">
                <a>
                    ${message(code: "notes.question.link")} <span class="badge">${notes.totalCount}</span>
                </a>
            </li>
        </ul>
    </g:if>
    <g:elseif test="${countTotal}">
        <ul class="nav nav-tabs pull-right">
            <g:if test="${countTotal}">
                <li role="presentation" data-toggle="tooltip" data-html="true"
                    title="${message(code: "notes.disabled.link.message")}" data-placement="bottom">
                    <g:link controller="notes" class="text-muted"
                            params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", inline: "${params.inline}"]'>
                        ${message(code: "notes.link")} <span class="badge">${countTotal}</span>
                    </g:link>
                </li>
                <li role="presentation" class="active">
                    <a>
                        ${message(code: "notes.question.link")} <span class="badge">${notes.totalCount}</span>
                    </a>
                </li>
            </g:if>
        </ul>
    </g:elseif>
</div>
<g:if test="${context?.isOpen()}">
    <div class="container note-edition">
        <g:if test="${user == context.owner}">
            <button class="btn btn-primary btn-xs pull-right" data-toggle="modal" data-target="#modalQuestion">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                ${message(code: "notes.edit.add.question.button")}
            </button>
            <div class="modal fade" id="modalQuestion" role="dialog" aria-labelledby="exampleModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                    aria-hidden="true">&times;</span></button>
                        </div>
                        <div class="modal-body">
                            <div class="container note-edition">
                                <g:render template="/questions/edit"
                                          model='[context: context, fragmentTag: fragmentTag]'/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </g:if>
    </div>
</g:if>
<div class="divider"></div>
<div class="container note-list">
    <div class="note-list-header">
        <div class="note-list-selector pull-right">
            <g:form action="index" method="get">
                <g:hiddenField name="contextId" value="${context?.id}"/>
                <g:hiddenField name="fragmentTagId" value="${fragmentTag?.id}"/>
                <g:hiddenField name="inline" value="${params.inline}"/>
                <label class="checkbox-inline">
                    <g:checkBox name="displaysMyNotes" checked="${params.displaysMyNotes == 'on' ? true : false}"
                                onchange="submit();"/> ${message(code: "notes.index.myNotes.checkbox")}
                </label>
                <label class="checkbox-inline">
                    <g:checkBox name="displaysMyFavorites"
                                checked="${params.displaysMyFavorites == 'on' ? true : false}"
                                onchange="submit();"/> ${message(code: "notes.index.myFavorites.checkbox")}
                </label>
                <label class="checkbox-inline">
                    <g:if test="${context}">
                        <g:checkBox name="displaysAll" checked="${params.displaysAll == 'on' ? true : false}"
                                    onchange="submit();"/>  ${message(code: "notes.index.allNotes.checkbox")}
                    </g:if>
                    <g:else>
                        <input type="checkbox" name="displaysAll" disabled/> <span
                            style="color: gainsboro">${message(code: "notes.index.allNotes.checkbox")}</span>
                    </g:else>
                </label>
            </g:form>
        </div>
    </div>
    <div class="note-list-content">
        <ul class="list-group">
            <g:if test="${notes.list.isEmpty()}">
                <g:set var="separationLine" value="true"/>
            </g:if>
            <g:each in="${notes.list}" var="note">
                <g:if test="${(separationLine == "false") && (user != note.author) && params.inline == 'on' && params.fragmentTagId != null}">
                    <hr/>
                    <g:set var="separationLine" value="true"/>
                </g:if>
                <g:set var="showDiscussionNote" value="${showDiscussion[note.id]}"/>
                <div id="note${note.id}" class="${showDiscussionNote ? 'note-discussion' : 'note-only'}">
                    <g:render template="detail"
                              model="[note: note, context: context, showDiscussionNote: showDiscussionNote]"/>
                </div>
            </g:each>
        </ul>
    </div>
</div>
<g:if test="${context}">
    <r:script>
  $('#button_context').popover({
                                 content: "<p><strong>url</strong>: <a href='${context.url}' target='blank'>${context.url}</a></p><p>${context.descriptionAsNote?.replaceAll('[\n\r]', ' ')}</p>",
                                 html: true
                               })

    </r:script>
</g:if>
<g:if test="${params.error}">
    <g:hiddenField name="errorParam" id="errorParam" value="${params.error}"/>
    <r:script>
        if ($("#errorParam").val() == 'question') {
            alert("${message(code: "notes.edit.question.error")}");
        }
        else {
            alert("${message(code: "notes.edit.note.error")}");
        }
    </r:script>
</g:if>
<r:script>
    function displaysReplyField(noteId) {
        $('#replyEdition' + noteId).toggle();
        var contentElement = $('#noteContent' + noteId);
        var content = contentElement.val();
        contentElement.focus().val('').val(content);
    }

    $("#mainLinkQuestions").addClass('active');
</r:script>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>
</body>
</html>