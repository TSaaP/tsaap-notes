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

<g:set var="idControllSuffix" value="${parentNote ? parentNote.id : 0}${note ? "_" + note.id : ""}"/>

<div class="panel-body">
    <g:form method="post" action="${note ? 'update' : 'add'}"
            enctype="multipart/form-data">
        <g:if test="${note}">
            <g:hiddenField name="noteId" value="${note.id}"/>
        </g:if>
        <g:hiddenField name="inline" value="${params.inline}"/>
        <g:hiddenField name="contextId" value="${context?.id}"
                       id="contextIdInAddForm${idControllSuffix}"/>
        <g:hiddenField name="fragmentTagId" value="${fragmentTag?.id}"/>
        <g:hiddenField name="parentNoteId" value="${parentNote?.id}"/>
        <g:hiddenField name="displaysMyNotes" id="displaysMyNotesInAddForm${idControllSuffix}"/>
        <g:hiddenField name="displaysMyFavorites"
                       id="displaysMyFavoritesInAddForm${idControllSuffix}"/>
        <g:hiddenField name="displaysAll" id="displaysAllInAddForm${idControllSuffix}"/>
        <a id="question_sample${idControllSuffix}"
           style="margin-top: 15px">${message(code: "notes.edit.sampleQuestion")}</a>
        <textarea class="form-control note-editable-content" rows="3"
                  id="noteContent${idControllSuffix}"
                  name="noteContent"
                  onkeyup="" onblur="">${note ? note.content : ""}</textarea>

        <div class="row">
            <div class="btn-toolbar">
                <div id="attach${idControllSuffix}" class="col-sm-8 col-md-8 col-lg-8">
                    <g:set var="attachment"/>
                    <g:if test="${note}">
                        <g:set var="attachment" value="${note.attachment}"/>
                        <g:if test="${attachment != null}">
                            <tsaap:viewAttachement width="150" height="150" attachement="${attachment}"/>
                            <g:remoteLink controller="notes" action="removeAttachement"
                                          params="[noteId: note.id]"
                                          update="attach${idControllSuffix}">
                                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            </g:remoteLink>
                        </g:if>
                    </g:if>
                    <g:if test="${!attachment}">
                        <input type="file" name="myFile"
                               title="Image: gif, jpeg and png only"/>
                    </g:if>
                </div>

                <div class="col-sm-4 col-md-4 col-lg-4">
                    <div class="pull-right">
                        <button type="button"
                                class="btn btn-default btn-xs"
                                onclick="showPreview('${idControllSuffix}', '<g:createLink action="evaluateContentAsQuestion"/>')">
                            ${message(code: "notes.edit.preview")}
                        </button>
                        <button type="submit"
                                class="btn btn-primary btn-xs"
                                id="buttonAddNote${idControllSuffix}"
                                disabled>
                                ${message(code: "notes.edit.update.question.button")}
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div id="preview_${idControllSuffix}"></div>
    </g:form>
</div>

<r:script>
    $("#noteContent${idControllSuffix}").bind('keyup blur', function () {
        charCount('${idControllSuffix}', this);
    }).keyup();

    $("#displaysMyNotesInAddForm${idControllSuffix}").val($("#displaysMyNotes").attr('checked') ? 'on' : '');
    $("#displaysMyFavoritesInAddForm${idControllSuffix}").val($("#displaysMyFavorites").attr('checked') ? 'on' : '');
    $("#displaysAllInAddForm${idControllSuffix}").val($("#displaysAll").attr('checked') ? 'on' : '');

    $('#question_sample${idControllSuffix}').popover({
        content: function() {
            return getQuestionSample('${idControllSuffix}','<g:createLink action="getSamples"
                                                                          params="[questionSample: 'question_sample' + idControllSuffix, toUpdate: 'noteContent' + idControllSuffix]"/>')},
        html: true,
        placement: 'bottom'
    }).on('shown.bs.popover', function() {
        MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_sample'])
    });
</r:script>