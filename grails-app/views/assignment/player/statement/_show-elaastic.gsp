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


<div class="ui accordion segment" id="statement-accordion" style="padding-bottom: 0;">
  <div class="ui ${isClosed ? '' : 'active'} title header">
    <i class="dropdown icon"></i> ${statementInstance.title}
    <g:if test="showQuestionType">

      [<span style="font-variant: all-small-caps; font-weight: normal; font-size: 0.8em; vertical-align: text-top">
      <g:message code="statement.questionType.${statementInstance.questionType}"/>
    </span>]
    </g:if>
  </div>

  <div class="ui basic padded large text segment ${isClosed ? '' : 'active'} content">
    <g:if test="${!hideStatement}">
      <p>
        <g:set var="attachment" value="${statementInstance?.attachment}"/>
        <g:if test="${attachment != null}">
          <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
        </g:if>
        ${raw(statementInstance.content)}
      </p>
    </g:if>
  </div>

</div>

<r:script>
  $(document).ready(function () {
    $('#statement-accordion').accordion();
  });
</r:script>
