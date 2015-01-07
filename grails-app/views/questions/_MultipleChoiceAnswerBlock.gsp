<g:each var="answer" in="${block.answerList}" status="i">
    <g:checkBox name="answers[$i]" value="${answer.identifier}" checked="${userAnswerBlock?.answerList?.contains(answer)}"/> ${answer.textValue}<br/>
</g:each>