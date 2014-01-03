<g:each var="answer" in="${block.answerList}" status="i">
    <g:checkBox name="answers[$i]" value="${answer.identifier}"/> ${answer.textValue}<br/>
</g:each>