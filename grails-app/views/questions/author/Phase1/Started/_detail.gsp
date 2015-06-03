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
<div class="question" id="question_${note.id}">

        <p><strong>${question.title}</strong></p>
        <g:each var="block" in="${question.blockList}">
            <p>
                <g:if test="${block instanceof TextBlock}">
                    ${block.text}
                </g:if>
                <g:else>
                    <g:render template="/questions/${question.questionType.name()}AnswerBlock" model="[block: block]"/>
                </g:else>
            </p>
        </g:each>
    <g:remoteLink action="stopPhase" controller="question" params="[phaseId:sessionPhase.id,noteId:note.id]"
                  class="btn btn-warning btn-xs" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span class="glyphicon glyphicon-stop"></span> ${message(code: "questions.author.phase1.started.endPhase")}</g:remoteLink>
    (${message(code: "questions.responseCount")} : ${sessionPhase.responseCount()} <g:remoteLink action="refreshPhase" controller="question" params="[noteId:note.id,phaseId:sessionPhase.id]" title="Refresh" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])">
    <span class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
    )
</div>