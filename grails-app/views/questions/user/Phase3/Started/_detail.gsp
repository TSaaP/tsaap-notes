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
<g:set var="sessionResponse" value="${secondPhase.getResponseForUser(user)}"/>
<g:set var="indexAnsBlock" value="${0}"/>
<div class="question" id="question_${note.id}">
    <p>Results <strong>${question.title}</strong> - (response count : ${secondPhase.responseCount()})</p>
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
        Your score : ${sessionResponse.percentCredit}%
    </g:if>
    <g:set var="responsesToEvaluate" value="${secondPhase.findAllResponsesToEvaluateForResponse(sessionResponse)}"/>

    <hr/>
    <g:if test="${!responsesToEvaluate || responsesToEvaluate[0]?.explanation?.hasBeenAlreadyEvaluatedByUser(user)}">
        <div class="alert alert-success">
            Waiting for the end of phase 3 for the question &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refreshPhase" controller="question"
                          params="[noteId: note.id, phaseId: sessionPhase.id]"
                          title="Refresh" update="question_${note.id}"
                          onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span
                    class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
        </div>
    </g:if>
    <g:else>
        <p>A last work waits for you: please give a grade to the explanations given for the good answer (1: "not usefull" to 5: "very usefull").</p>
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
                              class="btn btn-primary btn-xs" value="Submit"
                              onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"/>
        </g:form>
    </g:else>
</div>