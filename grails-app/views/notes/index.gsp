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
    <ul class="nav nav-tabs pull-right">
        <li role="presentation" class="active" data-toggle="tooltip" data-html="true"
            title="${context.noteTakingEnabled ? '' : message(code: "notes.disabled.link.message")}" data-placement="bottom">
            <a class="text-muted">
                ${message(code: "notes.link")} <span class="badge">${notes.totalCount}</span>
            </a>
        </li>
        <li role="presentation">
            <g:link controller="questions"
                    params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", inline: "${params.inline}"]'>
                ${message(code: "notes.question.link")} <span class="badge">${countTotal}</span>
            </g:link>
        </li>
    </ul>
</div>

<g:if test="${context?.noteTakingEnabled && context?.isOpen()}">
    <div class="container note-edition">
        <g:render template="/notes/edit" model='[context: context, fragmentTag: fragmentTag]'/>
    </div>
</g:if>

<div class="divider"></div>

<div class="container note-list">
    <div class="note-list-header">
        <div class="note-list-selector pull-right">
            <g:form controller="notes" action="index" method="get">
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
            <g:if test="${!(notes.list.isEmpty())}">
                <g:if test="${notes.list.get(0).author == user}">
                    <g:set var="separationLine" value="false"/>
                </g:if>
            </g:if>
            <g:else>
                <g:set var="separationLine" value="true"/>
            </g:else>
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
    <g:if test="${!(params.inline && params.inline == 'on' && params.fragmentTagId && params.fragmentTagId != 'null')}">
        <div class="note-list-pagination">
            <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${notes.totalCount}"
                            params='[contextId: "${params.contextId}", fragmentTagId: "${params.fragmentTagId}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", inline: "${params.inline}"]'/>
        </div>
    </g:if>
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
        alert("${message(code: "notes.edit.note.error")}");
    </r:script>
</g:if>

<r:script>
    function displaysReplyField(noteId) {
        $('#replyEdition' + noteId).toggle();
        var contentElement = $('#noteContent' + noteId);
        var content = contentElement.val();
        contentElement.focus().val('').val(content);
    }
</r:script>
%{--Functions for note edition--}%
<r:script>
    function charCount(idControllSuffix, content) {
        var counter =  $("#character_counter" + idControllSuffix);
        counter.text($(content).val().length + '/560 ${message(code: "notes.edit.characters")}');
        if ($(content).val().length >1) {
            $("#buttonAddNote" + idControllSuffix).removeAttr('disabled');
        } else {
            $("#buttonAddNote" + idControllSuffix).attr('disabled','disabled');
        }
    }
</r:script>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>
</body>
</html>