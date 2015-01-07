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
            <g:render template="/questions/${question.questionType.name()}AnswerBlockResult" model="[block: block,resultMap:resultMatrix[indexAnsBlock],userAnswerBlock:sessionResponse?.userResponse?.userAnswerBlockList?.get(indexAnsBlock++)]"/>
        </g:else>
    </g:each>
</div>
<g:if test="${sessionResponse}">
    Your score : ${sessionResponse.percentCredit}%
</g:if>