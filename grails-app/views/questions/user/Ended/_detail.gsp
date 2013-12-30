<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<g:set var="resultMatrix" value="${liveSession.resultMatrix()}"/>
<g:set var="sessionResponse" value="${liveSession.getResponseForUser(user)}"/>
<g:set var="indexAnsBlock" value="${0}"/>
<div class="question" id="question_${note.id}">
    <p>Results <strong>${question.title}</strong> - (response count : ${liveSession.responseCount()})</p>
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
