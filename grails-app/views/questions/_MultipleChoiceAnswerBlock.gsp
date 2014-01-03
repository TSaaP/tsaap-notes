<g:each var="answer" in="${block.answerList}" status="i">
    <g:checkBox name="answers[$i]" value="${answer.identifier}" checked="${false}"/> ${answer.textValue}<br/>
</g:each>