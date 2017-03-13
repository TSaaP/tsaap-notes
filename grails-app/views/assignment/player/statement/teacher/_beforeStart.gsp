<div style="display: flex;">
    <h4>${statementInstance.title}</h4>
    <div style="display: flex; margin: auto 5px;">
        <span style="line-height: 3em">-</span>
        <h5 style="margin: auto 5px">
            <g:message code="statement.questionType.${statementInstance.questionType}"></g:message>
        </h5>
    </div>
</div>
<p>
    <g:set var="attachment" value="${statementInstance?.attachment}"/>
    <g:if test="${attachment != null}">
        <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
    </g:if>
    ${raw(statementInstance.content)}
</p>
