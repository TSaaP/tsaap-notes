<%@ page import="org.tsaap.questions.TextBlock" %>

<div class="question">
    <g:form>
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
        <button type="submit"
                class="btn btn-primary btn-xs">Submit</button>
    </g:form>
</div>