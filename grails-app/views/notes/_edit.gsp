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
    <g:form method="post" controller="notes" action="${update ? "update" : "add"}" enctype="multipart/form-data">
        <g:if test="${note}">
            <g:hiddenField name="noteId" value="${note.id}"/>
        </g:if>
        <g:hiddenField name="kind" value="standard"/>
        <g:hiddenField name="inline" value="${params.inline}"/>
        <g:hiddenField name="contextId" value="${context?.id}"
                       id="contextIdInAddForm${idControllSuffix}"/>
        <g:hiddenField name="fragmentTagId" value="${fragmentTag?.id}"/>
        <g:hiddenField name="parentNoteId" value="${parentNote?.id}"/>
        <g:hiddenField name="displaysMyNotes" id="displaysMyNotesInAddForm${idControllSuffix}"/>
        <g:hiddenField name="displaysMyFavorites"
                       id="displaysMyFavoritesInAddForm${idControllSuffix}"/>
        <g:hiddenField name="displaysAll" id="displaysAllInAddForm${idControllSuffix}"/>

        <textarea class="form-control note-editable-content" rows="3" id="noteContent${idControllSuffix}"
                  name="noteContent"
                  maxlength="560">${update ? note.content : (parentNote ? '@' + parentNote.author?.username + ' ' : '')}</textarea>

        <g:if test="${update}">
            <div id="attach${idControllSuffix}">
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
            </div>
        </g:if>

        <div class="row">
            <span class="character_counter pull-left" style="margin-left: 15px"
                  id="character_counter${idControllSuffix}"></span>
        </div>

        <div class="row">
            <div class="btn-toolbar">
                <div class="col-xs-12 col-sm-9 col-md-9 col-lg-9">
                    <g:if test="${!attachment}">
                        <input type="file" name="myFile" title="Image: gif, jpeg and png only"/>
                    </g:if>
                </div>

                <div class="col-xs-12 col-sm-3 col-md-3 col-lg-3">
                    <button type="submit"
                            class="btn btn-primary btn-xs pull-right"
                            id="buttonAddNote${idControllSuffix}"
                            disabled>
                        <g:if test="${update}">
                            ${message(code: "notes.edit.update.note.button")}
                        </g:if>
                        <g:else>
                            <span class="glyphicon glyphicon-plus"></span> ${message(code: "notes.edit.add.note.button")}
                        </g:else>
                    </button>
                </div>
            </div>
        </div>
    </g:form>
</div>

<r:script>
    $("#noteContent${idControllSuffix}").bind('keyup blur', function () {
        charCount('${idControllSuffix}', this);
    }).keyup();

    $("#displaysMyNotesInAddForm${idControllSuffix}").val($("#displaysMyNotes").attr('checked') ? 'on' : '');
    $("#displaysMyFavoritesInAddForm${idControllSuffix}").val($("#displaysMyFavorites").attr('checked') ? 'on' : '');
    $("#displaysAllInAddForm${idControllSuffix}").val($("#displaysAll").attr('checked') ? 'on' : '');
</r:script>