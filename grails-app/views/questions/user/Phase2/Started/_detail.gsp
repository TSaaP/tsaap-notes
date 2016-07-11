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

<%@ page import="org.tsaap.questions.LiveSessionResponse; org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>

<div class="question" id="question_${note.id}">
    <g:if test="${sessionPhase.getResponseForUser(user)}">
        <div class="alert alert-success">
            ${message(code: "questions.user.phase2.started.wait")} &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refreshPhase" controller="questions"
                          params="[noteId: note.id, phaseId: sessionPhase.id]" title="Refresh"
                          update="question_${note.id}"
                          onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span
                    class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
            (${message(code: "questions.responseCount")} :${sessionPhase.responseCount()})
        </div>
    </g:if>
    <g:else>
        <g:set var="firstPhase" value="${sessionPhase.liveSession.findFirstPhase()}"/>
        <g:set var="firstResponse" value="${firstPhase.getResponseForUser(user)}"/>
        <g:set var="altResponse" value="${firstPhase.findConflictResponseForResponse(firstResponse)}"/>
        <g:if test="${altResponse}">
            <p>${message(code: "questions.user.phase2.started.alternative")}</p>

            <div class="alert alert-info">
                <p>
                    <g:each in="${altResponse?.userResponse?.userAnswerBlockList}" var="answerBlock">
                        <g:each in="${answerBlock.answerList}" var="answer">
                            ${answer.textValue}<br/>
                        </g:each>
                    </g:each>

                ${raw(altResponse?.explanation?.content)}
            </div>
            <hr/>
        </g:if>
        <g:else>
            <p>${message(code: "questions.user.phase2.started.newChance")}</p>
        </g:else>
        <g:form>
            <g:hiddenField name="phaseId" value="${sessionPhase.id}"/>
            <g:hiddenField name="noteId" value="${note.id}"/>

            <p><strong>${question.title}</strong></p>
            <g:each var="block" in="${question.blockList}">
                <g:set var="indexAnswerBlock" value="${0}"/>
                <p>
                    <g:if test="${block instanceof TextBlock}">
                        ${block.text}
                    </g:if>
                    <g:else>
                        <g:render template="/questions/${question.questionType.name()}AnswerBlock"
                                  model="[block: block, userAnswerBlock: firstResponse?.userResponse?.userAnswerBlockList?.get(indexAnswerBlock++)]"/>
                    </g:else>
                </p>
            </g:each>
            <p>${message(code: "questions.explanation")}</p>
            <ckeditor:editor name="explanation" toolbar="Basic" id="explanation">${firstResponse?.explanation?.content}</ckeditor:editor>

            <p>${message(code: "questions.confidenceDegree")} <g:select name="confidenceDegree" from="[1, 2, 3, 4, 5]"
                                                                        value="${firstResponse?.confidenceDegree}"/></p>
            <g:submitToRemote action="submitResponseInAPhase" controller="questions" update="question_${note.id}"
                              class="btn btn-primary btn-xs" value="${message(code: "questions.user.submit")}"
                              onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"
                              before="document.getElementById('explanation').textContent = CKEDITOR.instances.explanation.getData()"/>
        </g:form>
    </g:else>
</div>