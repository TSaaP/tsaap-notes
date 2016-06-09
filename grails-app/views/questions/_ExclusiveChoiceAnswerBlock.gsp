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

<g:each var="answer" in="${block.answerList}">
    <g:if test="${userAnswerBlock?.answerList?.contains(answer)}">
        <g:radio name="answers[0]" value="${answer.identifier}" checked="checked"/> ${answer.textValue}<br/>
    </g:if>
    <g:else>
        <g:radio name="answers[0]" value="${answer.identifier}"/> ${answer.textValue}<br/>
    </g:else>
</g:each>