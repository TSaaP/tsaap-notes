<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<div class="question" id="question_${note.id}">
   <p>Results <strong>${question.title}</strong> - (response count : ${liveSession.responseCount()})</p>
    <g:each var="block" in="${question.blockList}">
        <p>
            <g:if test="${block instanceof TextBlock}">
                ${block.text}
            </g:if>
            <g:else>
                <g:render template="/questions/${question.questionType.name()}AnswerBlockResult" model="[block: block]"/>
            </g:else>
        </p>
    </g:each>
</div>