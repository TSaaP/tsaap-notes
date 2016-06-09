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

<%@ page import="org.tsaap.questions.impl.gift.GiftQuestionService" %>
<g:each var="answer" in="${block.answerList}">
    <g:set var="answerStatus" value="${answer.percentCredit > 0 ? 'success' : 'danger'}"/>
    <g:set var="percentResult" value="${resultMap[answer.textValue]}"/>
    <div class="label label-${answerStatus}">
        <g:if test="${userAnswerBlock?.answerList?.contains(answer)}">
            <g:set var="suffix" value="${answer.percentCredit > 0 ? 'up' : 'down'}"/>
            <span class="glyphicon glyphicon-thumbs-${suffix}"></span>
        </g:if>
        ${answer.textValue}
    </div>

    <div class="progress">
        <div class="progress-bar progress-bar-${answerStatus}" role="progressbar" aria-valuenow="${percentResult}"
             aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult}%">
            <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
        </div>
    </div>
</g:each>
<g:set var="percentResult" value="${resultMap[GiftQuestionService.NO_RESPONSE]}"/>
<div class="label label-warning">${message(code: "questions.exclusive.result.doNotKnow")}</div>

<div class="progress">
    <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${percentResult}" aria-valuemin="0"
         aria-valuemax="100" style="width: ${percentResult}%">
        <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
    </div>
</div>