<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<div class="question" id="question_${note.id}">
   <p>Results <strong>${question.title}</strong> - (response count : ${liveSession.responseCount()})</p>
</div>