%{--
  -
  -  Copyright (C) 2017 Ticetime
  -
  -      This program is free software: you can redistribute it and/or modify
  -      it under the terms of the GNU Affero General Public License as published by
  -      the Free Software Foundation, either version 3 of the License, or
  -      (at your option) any later version.
  -
  -      This program is distributed in the hope that it will be useful,
  -      but WITHOUT ANY WARRANTY; without even the implied warranty of
  -      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -      GNU Affero General Public License for more details.
  -
  -      You should have received a copy of the GNU Affero General Public License
  -      along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -
  --}%

<g:set var="sequenceInstanceList" value="${sequenceInstance.assignment.sequences}"/>

<a class="ui right floated icon red tiny button"
   href="#"
   onclick="$('#confirm-delete-sequence-modal-${sequenceInstance.id}').modal('show');"
   data-tooltip="${g.message(code: 'common.delete')}"
   data-inverted="">
  <i class="trash icon"></i>
</a>

<g:if test="${sequenceInstance != sequenceInstanceList.last()}">
  <g:link class="ui right floated icon tiny button"
          action="downSequence"
          id="${sequenceInstance.id}"
          controller="sequence"
          data-tooltip="${g.message(code: 'common.moveDown')}"
          data-inverted="">
    <i class="arrow down icon"></i>
  </g:link>
</g:if>

<g:if test="${sequenceInstance != sequenceInstanceList[0]}">
  <g:link class="ui right floated icon tiny button"
          action="upSequence"
          id="${sequenceInstance.id}"
          controller="sequence"
          data-tooltip="${g.message(code: 'common.moveUp')}"
          data-inverted="">
    <i class="arrow up icon"></i>
  </g:link>
</g:if>

<g:link class="ui right floated icon secondary tiny button"
        action="editSequence"
        controller="sequence"
        id="${sequenceInstance.id}"
        data-tooltip="${g.message(code: 'common.update')}"
        data-inverted="">
  <i class="edit icon"></i>
</g:link>

<g:link class="ui right floated icon primary tiny button"
        action="playSequence"
        controller="player"
        id="${sequenceInstance.id}"
        data-tooltip="${g.message(code: 'sequence.play')}"
        data-inverted="">
  <i class="play icon"></i>
</g:link>

%{-- Delete confirm modal --}%
<div>
  <div id="confirm-delete-sequence-modal-${sequenceInstance.id}" class="ui tiny modal">
    <div class="header">
      <g:message code="common.delete"/>
    </div>

    <div class="content">
      <p>
        <g:message code="sequence.delete.confirm" args="[sequenceInstance.title]"/>
      </p>
    </div>

    <div class="actions">
      <div class="ui red approve button">
        <g:message code="common.yes"/>
      </div>

      <div class="ui cancel button">
        <g:message code="common.no"/>
      </div>
    </div>
  </div>
  <r:script>
  $('#confirm-delete-sequence-modal-${sequenceInstance.id}').modal({
    onApprove: function() {
      window.location = '${createLink(controller: "sequence", action: "deleteSequence", id: sequenceInstance.id)}';
    }
  });
  </r:script>
</div>