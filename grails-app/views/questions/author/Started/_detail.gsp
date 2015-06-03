<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<div class="question" id="question_${note.id}">

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
    <g:remoteLink action="stopLiveSession" controller="question" params="[liveSessId:liveSession.id,noteId:note.id]"
                  class="btn btn-warning btn-xs" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span class="glyphicon glyphicon-stop"></span> ${message(code: "questions.author.started.stop")}</g:remoteLink>
    (${message(code: "questions.responseCount")} : ${liveSession.responseCount()} <g:remoteLink action="refresh" controller="question" params="[noteId:note.id]" title="Refresh" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
    )
</div>