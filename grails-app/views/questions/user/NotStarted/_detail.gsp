<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<div class="question" id="question_${note.id}">
        <div class="alert alert-warning">
            ${message(code: "questions.user.wait")} &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refresh" controller="question" params="[noteId:note.id]" title="Refresh" update="question_${note.id}" onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
        </div>
</div>