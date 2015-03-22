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
   <p>Results <strong>${question.title}</strong> - (response count : ${responseCount})</p>
    <g:each var="block" in="${question.blockList}">
            <g:if test="${block instanceof TextBlock}">
                <p>${block.text}</p>
            </g:if>
            <g:else>
                <g:render template="/questions/${question.questionType.name()}AnswerBlockResult" model="[block: block,resultMap:resultMatrix[indexAnsBlock++]]"/>
            </g:else>
    </g:each>
</div>
<g:if test="${sessionPhase}">
    <hr/>
    <g:set var="responses" value="${liveSession.findAllGoodResponses(sessionPhase)}"/>
    <g:render template="/questions/ExplanationList" model="[responses:responses]"/>
    <g:link class="btn btn-success btn-xs" controller="question" action="statistics" id="${liveSession.id}">Generate stats</g:link>
</g:if>