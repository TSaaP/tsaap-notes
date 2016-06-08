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
                            params='[contextId: "${params.contextId}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", inline: "${params.inline}", kind: "${params.kind}"]'>
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
                        (${message(code:'context.information')})
                    </g:if>
                </li>
            </g:else>
        </g:if>
    </ol>
    <g:if test="${context.noteTakingEnabled}">
        <ul class="nav nav-tabs pull-right">

            <g:if test="${params.kind != 'question'}">
                <li role="presentation" class="active"><a>${message(code: "notes.link")} (${notes.totalCount})</a></li>
                <li role="presentation">
                    <g:link controller="notes"
                            params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", kind: "question", inline: "${params.inline}"]'>
                        ${message(code: "notes.question.link")} (${countTotal})
                    </g:link>
                </li>
            </g:if>
            <g:else>
                <li role="presentation">
                    <g:link controller="notes"
                            params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", kind: "standard", inline: "${params.inline}"]'>
                        ${message(code: "notes.link")} (${countTotal})
                    </g:link>
                </li>
                <li role="presentation"
                    class="active"><a>${message(code: "notes.question.link")} (${notes.totalCount})</a>
                </li>
            </g:else>
        </ul>
    </g:if>

    <g:else>
        <g:if test="${params.kind != 'question' || countTotal}">
            <ul class="nav nav-tabs pull-right">
                <g:if test="${params.kind != 'question'}">

                    <li role="presentation" class="active" data-toggle="tooltip" data-html="true"
                        title="${message(code: "notes.disabled.link.message")}" data-placement="bottom">
                        <a class="text-muted">${message(code: "notes.link")} (${notes.totalCount})</a>
                    </li>
                    <li role="presentation">
                        <g:link controller="notes"
                                params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", kind: "question", inline: "${params.inline}"]'>
                            ${message(code: "notes.question.link")} (${countTotal})
                        </g:link>
                    </li>
                </g:if>
                <g:elseif test="${params.kind == 'question' && countTotal}">
                    <li role="presentation" data-toggle="tooltip" data-html="true"
                        title="${message(code: "notes.disabled.link.message")}" data-placement="bottom">
                        <g:link controller="notes" class="text-muted"
                                params='[contextId: "${params.contextId}", contextName: "${params.contextName}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", fragmentTagId: "${params.fragmentTagId}", kind: "standard", inline: "${params.inline}"]'>
                            ${message(code: "notes.link")} (${countTotal})
                        </g:link>
                    </li>
                    <li role="presentation"
                        class="active"><a>${message(code: "notes.question.link")} (${notes.totalCount})</a></li>
                </g:elseif>
            </ul>
        </g:if>
    </g:else>

</div>

<g:if test="${(context.noteTakingEnabled || params.kind == 'question') && context.isOpen()}">
    <div class="container note-edition">
        <g:render template="edit" model='[context: context, fragmentTag: fragmentTag]'/>
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
                <g:hiddenField id="noteKind" name="kind" value="${params.kind}"/>
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
                <g:if test="${(notes.list.get(0).author == user) && params.kind == 'standard'}">
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
    <g:if test="${(!(params.inline && params.inline == 'on' && params.fragmentTagId && params.fragmentTagId != 'null') || params.kind == 'question')}">
        <div class="note-list-pagination">
            <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${notes.totalCount}"
                            params='[contextId: "${params.contextId}", fragmentTagId: "${params.fragmentTagId}", displaysMyNotes: "${params.displaysMyNotes}", displaysMyFavorites: "${params.displaysMyFavorites}", displaysAll: "${params.displaysAll}", inline: "${params.inline}", kind: "${params.kind}"]'/>
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
        if($("#errorParam").val() == 'question'){
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

    if ($("#noteKind").val() == 'question') {
        $("#mainLinkQuestions").addClass('active');
    }
    else {
        $("#mainLinkNotes").addClass('active');
    }
</r:script>
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>
</body>
</html>