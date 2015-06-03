<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<g:set var="sessionPhase" value="${liveSession.findSecondPhase()}"/>
<g:if test="${sessionPhase}">
    <g:set var="resultMatrix" value="${sessionPhase.resultMatrix}"/>
    <g:set var="responseCount" value="${sessionPhase.responseCount()}"/>
    <g:set var="sessionResponse" value="${sessionPhase.getResponseForUser(user)}"/>
</g:if>
<g:else>
    <g:set var="resultMatrix" value="${liveSession.resultMatrix}"/>
    <g:set var="responseCount" value="${liveSession.responseCount()}"/>
    <g:set var="sessionResponse" value="${liveSession.getResponseForUser(user)}"/>
</g:else>


<g:set var="indexAnsBlock" value="${0}"/>
<div class="question" id="question_${note.id}">
    <p>${message(code: "questions.results")} <strong>${question.title}</strong> - (${message(code: "questions.responseCount")} : ${responseCount})</p>
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
    ${message(code: "questions.user.score")} : ${sessionResponse.percentCredit}%
</g:if>
<g:if test="${sessionPhase}">
    <hr/>
    <g:set var="responses" value="${liveSession.findAllGoodResponses(sessionPhase)}"/>
    <g:render template="/questions/ExplanationList" model="[responses:responses, note:note]"/>
</g:if>