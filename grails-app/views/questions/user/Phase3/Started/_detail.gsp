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
<g:set var="secondPhase" value="${sessionPhase.liveSession.findSecondPhase()}"/>
<g:set var="resultMatrix" value="${secondPhase.resultMatrix}"/>
<g:set var="sessionResponse" value="${secondPhase.getResponseForUser(user)}"/>
<g:set var="indexAnsBlock" value="${0}"/>
<div class="question" id="question_${note.id}">
    <p>${message(code: "questions.results")} <strong>${question.title}</strong> - (${message(code: "questions.responseCount")} : ${secondPhase.responseCount()})
    </p>
    <g:each var="block" in="${question.blockList}">
        <g:if test="${block instanceof TextBlock}">
            <p>${block.text}</p>
        </g:if>
        <g:else>
            <g:render template="/questions/${question.questionType.name()}AnswerBlockResult"
                      model="[block: block, resultMap: resultMatrix[indexAnsBlock], userAnswerBlock: sessionResponse?.userResponse?.userAnswerBlockList?.get(indexAnsBlock++)]"/>
        </g:else>
    </g:each>

    <g:if test="${sessionResponse}">
        ${message(code: "questions.user.score")} : ${sessionResponse.percentCredit}%
    </g:if>
    <g:set var="responsesToEvaluate" value="${secondPhase.findAllResponsesToEvaluateForResponse(sessionResponse)}"/>

    <hr/>
    <g:if test="${!responsesToEvaluate || responsesToEvaluate[0]?.explanation?.hasBeenAlreadyEvaluatedByUser(user)}">
        <div class="alert alert-success">
            ${message(code: "questions.user.phase3.started.wait")} &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refresh" controller="question"
                          params="[noteId: note.id]"
                          title="Refresh" update="question_${note.id}"
                          onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span
                    class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
        </div>
    </g:if>
    <g:else>
        <p>${message(code: "questions.user.phase3.started.evaluateExplanations")}</p>
        <g:form>
            <g:hiddenField name="noteId" value="${note.id}"/>
            <g:hiddenField name="phaseId" value="${sessionPhase.id}"/>
            <g:each in="${responsesToEvaluate}" var="responseToEval" status="i">
                <g:hiddenField name="explanationIds[${i}]" value="${responseToEval?.explanation?.id}"/>
                <p class="alert alert-info">
                    ${responseToEval?.explanation?.content} <g:select name="grades[${i}]" from="[1, 2, 3, 4, 5]"
                                                                      style="display: block;"/>
                </p>
            </g:each>
            <g:submitToRemote action="evaluateResponses" controller="question" update="question_${note.id}"
                              class="btn btn-primary btn-xs" value="${message(code: "questions.user.submit")}"
                              onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"/>
        </g:form>
    </g:else>
</div>