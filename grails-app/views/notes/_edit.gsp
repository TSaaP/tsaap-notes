
<g:set var="idControllSuffix" value="${parentNote ? parentNote.id : 0}"/>
<g:form method="post" controller="notes" action="addNote">
    <g:hiddenField name="contextId" value="${context?.id}"
                   id="contextIdInAddForm${idControllSuffix}"/>
    <g:hiddenField name="fragmentTagId" value="${fragmentTag?.id}"/>
    <g:hiddenField name="parentNoteId" value="${parentNote?.id}"/>
    <g:hiddenField name="displaysMyNotes" id="displaysMyNotesInAddForm${idControllSuffix}"/>
    <g:hiddenField name="displaysMyFavorites"
                   id="displaysMyFavoritesInAddForm${idControllSuffix}"/>
    <g:hiddenField name="displaysAll" id="displaysAllInAddForm${idControllSuffix}"/>
    <textarea class="form-control note-editable-content" rows="3" id="noteContent${idControllSuffix}" name="noteContent"
              maxlength="280">${parentNote ? '@'+parentNote.author?.username+' ' : ''}</textarea>
      <span class="character_counter" id="character_counter${idControllSuffix}"></span><button type="submit"
                                                class="btn btn-primary btn-xs pull-right" id="buttonAddNote${idControllSuffix}" disabled><span
            class="glyphicon glyphicon-plus"></span> Add note</button>
  </g:form>


<r:script>
  $(document).ready(function () {

    // set character counters
    //-----------------------

    // Get the textarea field
    $("#noteContent${idControllSuffix}")

      // Bind the counter function on keyup and blur events
            .bind('keyup blur', function () {
                    // Count the characters and set the counter text
                    var counter =  $("#character_counter${idControllSuffix}");
                    counter.text($(this).val().length + '/280 characters');
                    if ($(this).val().length >1) {
                      $("#buttonAddNote${idControllSuffix}").removeAttr('disabled');
                    } else {
                      $("#buttonAddNote${idControllSuffix}").attr('disabled','disabled');
                    }
                  })

      // Trigger the counter on first load
            .keyup();

    // set hidden field value
    //----------------------
    $("#displaysMyNotesInAddForm${idControllSuffix}").val($("#displaysMyNotes").attr('checked') ? 'on' : '');
    $("#displaysMyFavoritesInAddForm${idControllSuffix}").val($("#displaysMyFavorites").attr('checked') ? 'on' : '');
    $("#displaysAllInAddForm${idControllSuffix}").val($("#displaysAll").attr('checked') ? 'on' : '');

  });
</r:script>