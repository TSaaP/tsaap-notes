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

<h3 class="ui header">
  ${statementInstance.title}
  <g:if test="showQuestionType">
    
    [<span style="font-variant: all-small-caps; font-weight: normal; font-size: 0.8em; vertical-align: text-top">
      <g:message code="statement.questionType.${statementInstance.questionType}"/>
    </span>]
  </g:if>
</h3>