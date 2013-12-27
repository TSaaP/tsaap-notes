<g:each var="answer" in="${block.answerList}">
    <g:set var="answerStatus" value="${answer.percentCredit == 100 ? 'success' : 'danger'}"/>
    <div class="progress">
        <div class="progress-bar progress-bar-${answerStatus}" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 40%">
            <span>40% ${answer.textValue}</span>
        </div>
    </div>
</g:each>