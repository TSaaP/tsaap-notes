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

<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<g:set var="sessionPhase" value="${liveSession.findSecondPhase()}"/>
<g:if test="${sessionPhase}">
    <g:set var="resultMatrix" value="${sessionPhase.resultMatrix}"/>
    <g:set var="responseCount" value="${sessionPhase.responseCount()}"/>
</g:if>
<g:else>
    <g:set var="resultMatrix" value="${liveSession.resultMatrix}"/>
    <g:set var="responseCount" value="${liveSession.responseCount()}"/>
</g:else>

<g:set var="indexAnsBlock" value="${0}"/>
<div class="question" id="question_${note.id}">
    <p>${message(code: "questions.results")} <strong>${question.title}</strong> - (${message(code: "questions.responseCount")} : ${responseCount})
    </p>
    <g:each var="block" in="${question.blockList}">
        <g:if test="${block instanceof TextBlock}">
            <p>${block.text}</p>
        </g:if>
        <g:else>
            <g:render template="/questions/${question.questionType.name()}AnswerBlockResult"
                      model="[block: block, resultMap: resultMatrix[indexAnsBlock++]]"/>
        </g:else>
    </g:each>
</div>
<g:if test="${sessionPhase}">
    <hr/>
    <g:set var="responses" value="${liveSession.findAllGoodResponses(sessionPhase)}"/>
    <g:render template="/questions/ExplanationList" model="[responses: responses]"/>
    <g:link class="btn btn-success btn-xs" controller="questions" action="statistics"
            id="${liveSession.id}">${message(code: "questions.author.ended.generateStats")}</g:link>
    <g:link class="btn btn-success btn-xs" controller="questions" action="results"
            id="${liveSession.id}">${message(code: "questions.author.ended.results")}</g:link>
</g:if>