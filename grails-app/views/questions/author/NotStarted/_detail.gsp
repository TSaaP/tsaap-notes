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
    <g:remoteLink action="startLiveSession" controller="question" params="[liveSessId:liveSession?.id,noteId:note.id]"
            class="btn btn-success btn-xs" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])">
        <span class="glyphicon glyphicon-play"></span> Start a simple session</g:remoteLink>
    <g:remoteLink action="startNPhasesLiveSession" controller="question" params="[liveSessId:liveSession?.id,noteId:note.id]"
                  class="btn btn-success btn-xs" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])">
        <span class="glyphicon glyphicon-play"></span> Start a n-phases session</g:remoteLink>
</div>