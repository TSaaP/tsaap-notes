<g:set var="idControllSuffix" value="${parentNote ? parentNote.id : 0}"/>

<div id="edit_tab_${idControllSuffix}">

    <g:form method="post" controller="notes" action="addNote" enctype="multipart/form-data">
        <g:hiddenField name="kind" value="${params.kind}"/>
        <g:hiddenField name="inline" value="${params.inline}"/>
        <g:hiddenField name="contextId" value="${context?.id}"
                       id="contextIdInAddForm${idControllSuffix}"/>
        <g:hiddenField name="fragmentTagId" value="${fragmentTag?.id}"/>
        <g:hiddenField name="parentNoteId" value="${parentNote?.id}"/>
        <g:hiddenField name="displaysMyNotes" id="displaysMyNotesInAddForm${idControllSuffix}"/>
        <g:hiddenField name="displaysMyFavorites"
                       id="displaysMyFavoritesInAddForm${idControllSuffix}"/>
        <g:hiddenField name="displaysAll" id="displaysAllInAddForm${idControllSuffix}"/>
        <label>Image (gif, jpeg and png only)<input style="display: inline-block" type="file" name="myFile"/></label>
        <textarea class="form-control note-editable-content" rows="3" id="noteContent${idControllSuffix}"
                  name="noteContent"
                  maxlength="560">${parentNote ? '@' + parentNote.author?.username + ' ' : ''}</textarea>
        <span class="character_counter" id="character_counter${idControllSuffix}"></span>
        <div id="prewiew_tab_${idControllSuffix}" class="pull-right">
            <button type="button" class="btn btn-default btn-xs"
                    id="preview_button_${idControllSuffix}">
                Show preview
            </button>
        </div>
        <button type="submit"
                                                                                                 class="btn btn-primary btn-xs pull-right"
                                                                                                 id="buttonAddNote${idControllSuffix}"
                                                                                                 disabled><span
                class="glyphicon glyphicon-plus"></span> Add note</button>
    </g:form>
</div>


<r:script>
  $(document).ready(function () {

    var contentPreview

    // preview management
    //--------
    $('#preview_button_${idControllSuffix}').popover({
                                 content: function() {return getNotePreview()},
                                 html: true,
                                 placement: 'bottom'
                               }).on('shown.bs.popover', function() {
                                 MathJax.Hub.Queue(['Typeset',MathJax.Hub,'prewiew_tab_${idControllSuffix}'])
                               })

    function getNotePreview() {
        var noteInput = $("#noteContent${idControllSuffix}").val() ;
        contentPreview = noteInput
        if (noteInput.lastIndexOf('::', 0) === 0) {
            $.ajax({
                type: "POST",
                url: '<g:createLink action="evaluateContentAsNote" controller="notes"/>',
                data: {content:noteInput},
                async: false
            }).done(function( data ) {
                contentPreview = data ;
            });
         }
         return contentPreview
    }



    // set character counters
    //-----------------------

    // Get the textarea field
    $("#noteContent${idControllSuffix}")

      // Bind the counter function on keyup and blur events
            .bind('keyup blur', function () {
                    // Count the characters and set the counter text
                    var counter =  $("#character_counter${idControllSuffix}");
                    counter.text($(this).val().length + '/560 characters');
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