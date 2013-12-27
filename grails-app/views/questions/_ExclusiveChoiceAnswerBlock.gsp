<g:each var="answer" in="${block.answerList}">
    <g:radio name="answer" value="${answer.textValue}"/> ${answer.textValue}<br/>
</g:each>