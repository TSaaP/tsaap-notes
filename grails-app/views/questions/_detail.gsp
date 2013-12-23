<%@ page import="org.tsaap.questions.TextBlock" %>

<div class="question">
    <g:form>
        <h3>${question.title}</h3>
        <g:each var="block" in="${question.questionBlockList}">
            <p>
                <g:if test="${block instanceof TextBlock}">
                    ${block.text}
                </g:if>
                <g:else>
                    <g:render template="/questions/${question.questionType.name()}AnswerBlock" model="[block: block]"/>
                </g:else>
            </p>
            <button type="submit"
                    class="btn btn-primary btn-xs">Submit</button>
        </g:each>
    </g:form>
</div>