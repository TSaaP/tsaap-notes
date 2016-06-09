<%@ page import="org.tsaap.questions.TextBlock" %>
<div class="question">
    <p><strong>${question.title}</strong></p>
    <g:each var="block" in="${question.blockList}">
        <p>
            <g:if test="${block instanceof TextBlock}">
                ${block.text}
            </g:if>
            <g:else>
                <g:render template="/questions/${question.questionType.name()}AnswerBlock" model="[block: block]"/>
            </g:else>
        </p>
    </g:each>
</div>