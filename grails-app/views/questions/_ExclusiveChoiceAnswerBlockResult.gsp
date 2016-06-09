<%@ page import="org.tsaap.questions.impl.gift.GiftQuestionService" %>
<g:each var="answer" in="${block.answerList}">
    <g:set var="answerStatus" value="${answer.percentCredit > 0 ? 'success' : 'danger'}"/>
    <g:set var="percentResult" value="${resultMap[answer.textValue]}"/>
    <div class="label label-${answerStatus}">
        <g:if test="${userAnswerBlock?.answerList?.contains(answer)}">
            <g:set var="suffix" value="${answer.percentCredit > 0 ? 'up' : 'down'}"/>
            <span class="glyphicon glyphicon-thumbs-${suffix}"></span>
        </g:if>
        ${answer.textValue}
    </div>

    <div class="progress">
        <div class="progress-bar progress-bar-${answerStatus}" role="progressbar" aria-valuenow="${percentResult}"
             aria-valuemin="0" aria-valuemax="100" style="width: ${percentResult}%">
            <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
        </div>
    </div>
</g:each>
<g:set var="percentResult" value="${resultMap[GiftQuestionService.NO_RESPONSE]}"/>
<div class="label label-warning">${message(code: "questions.exclusive.result.doNotKnow")}</div>

<div class="progress">
    <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="${percentResult}" aria-valuemin="0"
         aria-valuemax="100" style="width: ${percentResult}%">
        <span><g:formatNumber number="${percentResult}" type="number" maxFractionDigits="2"/>%</span>
    </div>
</div>