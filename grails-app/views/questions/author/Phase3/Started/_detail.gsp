%{--
  - Copyright 2015 Tsaap Development Group
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%


<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<g:set var="secondPhase" value="${sessionPhase.liveSession.findSecondPhase()}"/>
<g:set var="resultMatrix" value="${secondPhase.resultMatrix}"/>
<g:set var="indexAnsBlock" value="${0}"/>
<div class="question" id="question_${note.id}">
    <p>${message(code: "questions.results")} <strong>${question.title}</strong> - (${message(code: "questions.responseCount")} : ${secondPhase.responseCount()})</p>
    <g:each var="block" in="${question.blockList}">
        <g:if test="${block instanceof TextBlock}">
            <p>${block.text}</p>
        </g:if>
        <g:else>
            <g:render template="/questions/${question.questionType.name()}AnswerBlockResult" model="[block: block,resultMap:resultMatrix[indexAnsBlock++]]"/>
        </g:else>
    </g:each>
    <g:remoteLink action="stopPhase" controller="question" params="[phaseId:sessionPhase.id,noteId:note.id]"
                  class="btn btn-warning btn-xs" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])">
        <span class="glyphicon glyphicon-stop"></span> ${message(code: "questions.author.phase3.started.endPhase")}</g:remoteLink>
    (${message(code: "questions.responseCount")} : ${sessionPhase.responseCount()} <g:remoteLink action="refreshPhase" controller="question" params="[noteId:note.id,phaseId:sessionPhase.id]" title="Refresh" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])">
    <span class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>)
</div>