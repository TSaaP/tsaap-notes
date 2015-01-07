<g:each var="answer" in="${block.answerList}">
    <g:if test="${userAnswerBlock?.answerList?.contains(answer)}">
        <g:radio name="answers[0]" value="${answer.identifier}" checked="checked"/> ${answer.textValue}<br/>
    </g:if>
    <g:else>
        <g:radio name="answers[0]" value="${answer.identifier}"/> ${answer.textValue}<br/>
    </g:else>
</g:each>