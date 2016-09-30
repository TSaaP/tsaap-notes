<h4>${statementInstance.title}</h4>
<p>
    <g:set var="attachment" value="${statementInstance?.attachment}"/>
    <g:if test="${attachment != null}">
        <tsaap:viewAttachement width="650" height="380" attachement="${attachment}"/>
    </g:if>
    ${raw(statementInstance.content)}
</p>
