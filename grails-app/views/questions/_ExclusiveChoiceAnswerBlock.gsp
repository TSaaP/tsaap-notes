<g:each var="answer" in="${block.answerList}">
    <g:radio name="answers[0]" value="${answer.identifier}"/> ${answer.textValue}<br/>
</g:each>