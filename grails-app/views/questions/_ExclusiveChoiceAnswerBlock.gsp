<g:each var="answer" in="${block.answerList}">
    <g:radio name="answers" value="answer.identifier"/> ${answer.textValue}<br/>
</g:each>