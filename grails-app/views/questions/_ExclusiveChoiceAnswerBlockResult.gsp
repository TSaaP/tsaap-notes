<%@ page import="org.tsaap.questions.impl.gift.GiftQuestionService" %>
<g:each var="answer" in="${block.answerList}">
    <g:set var="answerStatus" value="${answer.percentCredit == 100 ? 'success' : 'danger'}"/>
    <g:set var="percentResult" value="${resultMap[answer.textValue]}"/>
    <div>${answer.textValue}</div>
    <div class="progress">
        <div class="progress-bar progress-bar-${answerStatus}" role="progressbar" aria-valuenow="${percentResult}" aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult}%">
            <span>${percentResult}%</span>
        </div>
    </div>
</g:each>
<g:set var="percentResult" value="${resultMap[GiftQuestionService.NO_RESPONSE]}"/>
Do not know
<div class="progress">
    <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${percentResult}" aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult}%">
        <span>${percentResult}%</span>
    </div>
</div>