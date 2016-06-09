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

<div class="question" id="question_${note.id}">
    <g:if test="${liveSession.getResponseForUser(user)}">
        <div class="alert alert-success">
            ${message(code: "questions.user.started.wait")} &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refresh" controller="question" params="[noteId: note.id]" title="Refresh"
                          update="question_${note.id}"
                          onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span
                    class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
            (response count :${liveSession.responseCount()})
        </div>
    </g:if>
    <g:else>
        <g:form>
            <g:hiddenField name="liveSessId" value="${liveSession.id}"/>
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
            <g:submitToRemote action="submitResponse" controller="question" update="question_${note.id}"
                              class="btn btn-primary btn-xs" value="${message(code: "questions.user.submit")}"
                              onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"/>
        </g:form>
    </g:else>
</div>