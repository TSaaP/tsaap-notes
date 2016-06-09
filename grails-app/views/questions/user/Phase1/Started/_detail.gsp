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
    <g:if test="${sessionPhase.getResponseForUser(user)}">
        <div class="alert alert-success">
            ${message(code: "questions.user.phase1.started.wait")} &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refreshPhase" controller="question"
                          params="[noteId: note.id, phaseId: sessionPhase.id]" title="Refresh"
                          update="question_${note.id}"
                          onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span
                    class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
            (${message(code: "questions.responseCount")} :${sessionPhase.responseCount()})
        </div>
    </g:if>
    <g:else>
        <g:form>
            <g:hiddenField name="phaseId" value="${sessionPhase.id}"/>
            <g:hiddenField name="noteId" value="${note.id}"/>
            <p><strong>${question.title}</strong></p>
            <g:each var="block" in="${question.blockList}">
                <p>
                    <g:if test="${block instanceof TextBlock}">
                        ${block.text}
                    </g:if>
                    <g:else>
                        <g:render template="/questions/${question.questionType.name()}AnswerBlock"
                                  model="[block: block]"/>
                    </g:else>
                </p>
            </g:each>
            <p>${message(code: "questions.explanation")}</p>
            <g:textArea class="form-control note-editable-content" rows="3" name="explanation"/>
            <p>${message(code: "questions.confidenceDegree")} <g:select name="confidenceDegree"
                                                                        from="[1, 2, 3, 4, 5]"/></p>
            <g:submitToRemote action="submitResponseInAPhase" controller="question" update="question_${note.id}"
                              class="btn btn-primary btn-xs" value="${message(code: "questions.user.submit")}"
                              onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"/>
        </g:form>
    </g:else>
</div>